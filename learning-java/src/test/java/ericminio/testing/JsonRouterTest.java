package ericminio.testing;

import org.junit.Test;
import support.FakeHttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class JsonRouterTest {

    private JsonRouter from(String filename) {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(filename);
        String config = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining());
        return new JsonRouter(config, new HashMap<>(), new HashMap<>());
    }

    @Test
    public void selectsFirstMatchingRoute() {
        JsonRouter router = from("first-matching.json");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/ping");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("first"));
    }

    @Test
    public void matchesMethod() {
        JsonRouter router = from("matching-method.json");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("method", "POST");
            setAttribute("uri", "/ping");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("second"));
    }

    @Test
    public void selectEmptyCriteria() {
        JsonRouter router = from("empty-when.json");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("selected"));
    }

    @Test
    public void dynamicBodyCanDigestString() {
        JsonRouter router = from("dynamic-body-string.json");
        router.getFunctions().put("greetings", (incoming, variables, parameters) -> "world");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("hello world :)"));
    }

    @Test
    public void dynamicBodyCanDigestStringInsideJson() {
        JsonRouter router = from("dynamic-body-string-inside-json.json");
        router.getFunctions().put("greetings", (incoming, variables, parameters) -> "hello world");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/string-inside-json");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("{\"message\":\"hello world\"}"));
    }

    @Test
    public void dynamicBodyCanDigestCombinedStringInsideJson() {
        JsonRouter router = from("dynamic-body-string-inside-json.json");
        router.getFunctions().put("greetings", (incoming, variables, parameters) -> "world");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/combined-string-inside-json");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("{\"message\":\"hello world :)\"}"));
    }

    @Test
    public void dynamicBodyCanDigestObject() {
        JsonRouter router = from("dynamic-body-object.json");
        router.getFunctions().put("greetings", (incoming, variables, parameters) -> new HashMap<String, Object>() {{ put("message", "hello"); }} );
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("{\"message\":\"hello\"}"));
    }

    @Test
    public void dynamicBodyCanDigestObjectInsideJson() {
        JsonRouter router = from("dynamic-body-object-inside-json.json");
        router.getFunctions().put("greetings", (incoming, variables, parameters) -> new HashMap<String, Object>() {{ put("message", "welcome"); }} );
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("{\"greetings\":{\"message\":\"welcome\"}}"));
    }

    @Test
    public void dynamicBodyCanDigestEmptyList() {
        JsonRouter router = from("dynamic-body-list.json");
        router.getFunctions().put("list", (incoming, variables, parameters) -> new ArrayList() );
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("[]"));
    }

    @Test
    public void dynamicBodyCanDigestListOfObjects() {
        JsonRouter router = from("dynamic-body-list.json");
        router.getFunctions().put("list", (incoming, variables, parameters) -> {
            List<Map> attributes = new ArrayList<>();

            Map<String, Object> first = new HashMap<>();
            first.put("old", Boolean.TRUE);
            attributes.add(first);

            Map<String, Object> second = new HashMap<>();
            second.put("obsolete", Boolean.FALSE);
            attributes.add(second);

            return attributes;
        });
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("[{\"old\":true},{\"obsolete\":false}]"));
    }

    @Test
    public void dynamicBodyCanDigestNull() {
        JsonRouter router = from("dynamic-body-null.json");
        router.getFunctions().put("nullify", (incoming, variables, parameters) -> null);
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("{\"message\":null}"));
    }

    @Test
    public void dynamicBodyOffersSpyingMechanism() {
        class Spy {
            private JsonRouter.Incoming incoming;
            public void record(JsonRouter.Incoming incoming) {
                this.incoming = incoming;
            }
            public boolean wasCalled() {
                return this.incoming != null;
            }
        }
        Spy spy = new Spy();
        JsonRouter router = from("dynamic-body-spy.json");
        router.getFunctions().put("spy", (incoming, variables, parameters) -> {
            spy.record(incoming);
            return "anything";
        });
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/brag-about-spying");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("{\"message\":\"anything\"}"));

        assertThat(spy.wasCalled(), equalTo(true));
        assertThat(spy.incoming.getUri(), equalTo("/brag-about-spying"));
    }

    @Test
    public void dynamicBodyCanTakeParameters() {
        JsonRouter router = from("dynamic-body-with-parameters.json");
        router.getFunctions().put("greetings", (incoming, variables, parameters) ->
                parameters.get(0).toString()
                        + " times "
                        + parameters.get(1).toString() + " "
                        + parameters.get(2).toString() + " :)"
        );
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("200 times true hello world :)"));
    }

    @Test
    public void selectionCanBeOnDynamicUrl() {
        JsonRouter router = from("dynamic-url.json");
        router.getFunctions().put("hidden", (incoming, variables, parameters) -> parameters.get(0));
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/select/42");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("found"));
    }

    @Test
    public void canExtractFunctionNameWithoutParameters() {
        assertThat(JsonRouter.extractFunctionName("~call~greetings()"), equalTo("greetings"));
    }

    @Test
    public void canExtractFunctionNameWithParameters() {
        assertThat(JsonRouter.extractFunctionName("~call~greetings(200, 'hello world')"), equalTo("greetings"));
    }

    @Test
    public void canExtractParameters() {
        List<Object> parameters = JsonRouter.extractParameters(
                "anything before ~call~greetings(200, false, 'hello world') anything after even parenthesis )");

        assertThat(parameters.size(), equalTo(3));
        assertThat(parameters.get(0), equalTo(new Integer(200)));
        assertThat(parameters.get(1), equalTo(Boolean.FALSE));
        assertThat(parameters.get(2), equalTo("hello world"));
    }

    @Test
    public void defaultParameterListIsEmpty() {
        List<Object> parameters = JsonRouter.extractParameters("~call~greetings()");

        assertThat(parameters.size(), equalTo(0));
    }

    @Test
    public void canExtractCall() {
        String call = JsonRouter.extractStatement(
                "anything before ~call~greetings(200, false, 'hello world') anything after even parenthesis )");

        assertThat(call, equalTo("~call~greetings(200, false, 'hello world')"));
    }
    @Test
    public void canExtractStandaloneCall() {
        String call = JsonRouter.extractStatement("~call~greetings()");

        assertThat(call, equalTo("~call~greetings()"));
    }
    @Test
    public void offersSeveralAnswersForConsecutiveCalls() {
        JsonRouter router = from("one-url-several-answers.json");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/ping");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("first"));

        answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("second"));
    }
    @Test
    public void lastAnswerIsForever() {
        JsonRouter router = from("one-url-several-answers.json");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/ping");
        }};
        JsonRouter.Answer answer = router.digest(exchange);
        answer = router.digest(exchange);
        answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("second"));
    }
    @Test
    public void severalAnswersDefinitionWinsAgainstSingleAnswerDefinition() {
        JsonRouter router = from("several-answers-definition-win.json");
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/ping");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("text/plain"));
        assertThat(answer.getEvaluateBody(), equalTo("I win"));
    }
}
