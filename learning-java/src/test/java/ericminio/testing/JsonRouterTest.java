package ericminio.testing;

import org.junit.Test;
import support.FakeHttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
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
        router.getFunctions().put("greetings", (incoming, variables) -> "world");
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
        router.getFunctions().put("greetings", (incoming, variables) -> "hello world");
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
        router.getFunctions().put("greetings", (incoming, variables) -> "world");
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
        router.getFunctions().put("greetings", (incoming, variables) -> new HashMap<String, Object>() {{ put("message", "hello"); }} );
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
        router.getFunctions().put("greetings", (incoming, variables) -> new HashMap<String, Object>() {{ put("message", "welcome"); }} );
        FakeHttpExchange exchange = new FakeHttpExchange() {{
            setAttribute("uri", "/anything");
        }};
        JsonRouter.Answer answer = router.digest(exchange);

        assertThat(answer.getStatusCode(), equalTo(200));
        assertThat(answer.getContentType(), equalTo("application/json"));
        assertThat(answer.getEvaluateBody(), equalTo("{\"greetings\":{\"message\":\"welcome\"}}"));
    }

    @Test
    public void dynamicBodyCanDigestNull() {
        JsonRouter router = from("dynamic-body-null.json");
        router.getFunctions().put("nullify", (incoming, variables) -> null);
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
        router.getFunctions().put("spy", (incoming, variables) -> {
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
}
