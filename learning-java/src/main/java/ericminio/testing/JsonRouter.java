package ericminio.testing;

import com.sun.net.httpserver.HttpExchange;
import ericminio.json.JsonToMapsParser;
import ericminio.json.MapsToJsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonRouter {

    private List<Route> routes;
    private final Map<String, Object> variables;
    private final Map<String, Function> functions;
    private static String STATEMENT_PREFIX = "~call~";

    public JsonRouter(String config, Map<String, Object> variables, Map<String, Function> functions) {
        this.variables = variables;
        this.functions = functions;
        routes = ((List<Map<String, Object>>) new JsonToMapsParser().parseObject(config).get("routes"))
                .stream().map(e -> new JsonRouter.Route(e)).collect(Collectors.toList());
    }

    public Answer digest(HttpExchange exchange) {
        Incoming incoming = new Incoming(exchange, variables);
        Route route = this.route(incoming);
        Answer answer = route.getAnswer();
        answer.evaluateStatusCode(incoming);
        answer.evaluateBody(incoming);

        return answer;
    }

    public Route route(Incoming incoming) {
        return routes.stream().filter(route -> route.isOpen(incoming)).findFirst().get();
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }

    public void insertRoute(String url, int statusCode, String contentType, String body) {
        Map<String, Object> definition = new HashMap<>();
        definition.put("when", new HashMap<String, Object>());
        definition.put("answer", new HashMap<String, Object>());
        ((Map<String, Object>) definition.get("when")).put("urlStartsWith", url);
        ((Map<String, Object>) definition.get("answer")).put("statusCode", new Integer(statusCode));
        ((Map<String, Object>) definition.get("answer")).put("contentType", contentType);
        ((Map<String, Object>) definition.get("answer")).put("body", body);
        routes.add(0, new Route(definition));
    }

    public class Incoming {
        private String uri;
        private String method;
        private String body;
        private HttpExchange exchange;
        private Map<String, Object> variables;

        public Incoming(HttpExchange exchange, Map<String, Object> variables) {
            this.exchange = exchange;
            this.variables = variables;

            this.uri = exchange.getRequestURI().toString();
            this.method = exchange.getRequestMethod().toUpperCase();
            this.body = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines().collect(Collectors.joining());
        }

        public String getUri() {
            return this.uri;
        }

        public String getMethod() {
            return this.method;
        }

        public String getBody() {
            return this.body;
        }

        public boolean hasUriStartingWith(String expectedUrlPrefix) {
            return this.getUri().startsWith(expectedUrlPrefix);
        }

        public boolean hasUriMatching(String expectedUrlMatch) {
            Pattern pattern = Pattern.compile(expectedUrlMatch);
            Matcher matcher = pattern.matcher(this.getUri());
            if (matcher.matches()) {
                this.variables.put("groupCount", matcher.groupCount());
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    this.variables.put("group-"+i, matcher.group(i));
                }
                return true;
            }
            else {
                return false;
            }
        }

        public boolean hasMethod(String expectedMethod) {
            return this.getMethod().equalsIgnoreCase(expectedMethod);
        }

        public boolean hasBodyContaining(String expectedBody) {
            return this.getBody().contains(expectedBody);
        }
    }
    class Route {
        Map<String, Object> definition;
        int answerIndex;

        public Route(Map<String, Object> definition) {
            this.definition = definition;
            this.answerIndex = -1;
        }

        public boolean isOpen(Incoming incoming) {
            return getGate().isOpen(incoming);
        }

        public Gate getGate() {
            return new Gate((Map<String, Object>) this.definition.get("when"));
        }

        public Answer getAnswer() {
            if (this.definition.get("answers") != null) {
                List answers = (List) this.definition.get("answers");
                if (answerIndex < answers.size()-1) {
                    answerIndex++;
                }
                return new Answer((Map<String, Object>) answers.get(answerIndex));
            }
            return new Answer((Map<String, Object>) this.definition.get("answer"));
        }
    }
    class Gate {
        private Map<String, Object> definition;

        public Gate(Map<String, Object> definition) {
            this.definition = definition;
        }

        public boolean isOpen(Incoming incoming) {
            if (this.definition == null) { return true; }

            String expectedUrlPrefix = (String) this.definition.get("urlStartsWith");
            if (expectedUrlPrefix != null) {
                if (expectedUrlPrefix.contains(STATEMENT_PREFIX)) {
                    String statement = extractStatement(expectedUrlPrefix);
                    String value = evaluate(statement, incoming).toString();
                    expectedUrlPrefix = expectedUrlPrefix.replace(statement, value);
                }
                if (!incoming.hasUriStartingWith(expectedUrlPrefix)) { return false; }
            }

            String expectedMethod = (String) this.definition.get("methodIs");
            if (expectedMethod != null && !incoming.hasMethod(expectedMethod)) { return false; }

            String expectedBody = (String) this.definition.get("bodyContains");
            if (expectedBody != null && !incoming.hasBodyContaining(expectedBody)) { return false; }

            String expectedUrlMatch = (String) this.definition.get("urlMatches");
            if (expectedUrlMatch != null && !incoming.hasUriMatching(expectedUrlMatch)) { return false; }

            return true;
        }
    }
    public class Answer {

        private Map<String, Object> definition;
        private Object statusCodeDefinition;
        private Integer statusCode;
        private String contentType;
        private String evaluatedBody;
        private Integer millisecondsDelay;

        public Answer(Map<String, Object> definition) {
            this.definition = definition;
            this.contentType = (String) this.definition.get("contentType");
            this.statusCodeDefinition = this.definition.get("statusCode");
            this.millisecondsDelay = (Integer) this.definition.get("millisecondsDelay");
        }

        public String getContentType() {
            return contentType;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public String getBody() {
            Object body = this.definition.get("body");
            if (body instanceof String) { return body.toString(); }

            return MapsToJsonParser.stringify(body);
        }

        public String getEvaluateBody() {
            return evaluatedBody;
        }

        public void evaluateBody(Incoming incoming) {
            this.evaluatedBody = this.getBody();
            for (String key :variables.keySet()) {
                this.evaluatedBody = this.evaluatedBody.replace("#"+key, variables.get(key).toString());
            }
            if (this.evaluatedBody.contains(STATEMENT_PREFIX)) {
                try {
                    String statement = extractStatement(this.evaluatedBody);
                    Object value = evaluate(statement, incoming);
                    String valueAsString = value == null? "null" : value.toString();
                    if ( value != null && ! (value instanceof String)) {
                        valueAsString = MapsToJsonParser.stringify(value);
                    }
                    if ((value != null && !(value instanceof String) || value == null)
                            && this.evaluatedBody.contains("\"" + statement + "\"")) {
                        statement = "\"" + statement + "\"";
                    }
                    this.evaluatedBody = this.evaluatedBody.replace(statement, valueAsString);
                }
                catch (Exception e) {
                    this.statusCode = 500;
                    this.contentType = "text/plain";
                    this.evaluatedBody = e.getMessage();
                }
            }
        }

        public void evaluateStatusCode(Incoming incoming) {
            if (statusCodeDefinition instanceof Integer) {
                this.statusCode = (Integer) statusCodeDefinition;
            }
            else {
                String stringDefinition = (String) statusCodeDefinition;
                if (stringDefinition.contains(STATEMENT_PREFIX)) {
                    String name = extractFunctionName(stringDefinition);
                    Function function = functions.get(name);
                    this.statusCode = (Integer) function.execute(incoming, variables, new ArrayList<>());
                }
            }
        }

        public boolean isDelayed() {
            return millisecondsDelay != null;
        }

        public long getDelay() {
            return millisecondsDelay;
        }
    }

    public interface Function {
        Object execute(Incoming incoming, Map<String, Object> variables, List<Object> parameters);
    }

    protected static String extractStatement(String expression) {
        String call = "";
        if (expression.contains(STATEMENT_PREFIX)) {
            int index = expression.indexOf(STATEMENT_PREFIX);
            String trailing = expression.substring(index);
            index = trailing.indexOf(")");
            call = trailing.substring(0, index+1);
        }
        return call;
    }

    protected Object evaluate(String call, Incoming incoming) {
        String name = extractFunctionName(call);
        List<Object> parameters = extractParameters(call);
        Function function = functions.get(name);

        return function.execute(incoming, variables, parameters);
    }

    protected static String extractFunctionName(String call) {
        Pattern pattern = Pattern.compile(".*~call~(.*)\\(.*");
        Matcher matcher = pattern.matcher(call);
        if (matcher.matches()) {
            String name = matcher.group(1);
            return name;
        }
        return null;
    }

    public static List<Object> extractParameters(String call) {
        Pattern pattern = Pattern.compile(".*~call~(.*)\\(([^\\)]*)\\).*");
        Matcher matcher = pattern.matcher(call);
        if (matcher.matches()) {
            String parameters = matcher.group(2);
            if (parameters.length() == 0) { return new ArrayList<>(); }

            return Arrays.asList(parameters.split(",")).stream()
                    .map(e -> {
                        String candidate = e.trim();
                        if (candidate.startsWith("'") && candidate.endsWith("'")) {
                            return candidate.substring(1, candidate.length() -1 );
                        }
                        if ("false".equalsIgnoreCase(candidate)) {
                            return Boolean.FALSE;
                        }
                        if ("true".equalsIgnoreCase(candidate)) {
                            return Boolean.TRUE;
                        }
                        try {
                            return new Integer(candidate);
                        }
                        catch(NumberFormatException notAnInteger) {
                            return candidate;
                        }
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }
}
