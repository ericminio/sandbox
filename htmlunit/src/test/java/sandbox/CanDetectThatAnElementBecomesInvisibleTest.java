package sandbox;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.fishtank.css.selectors.Selectors;
import se.fishtank.css.selectors.dom.W3CNode;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class CanDetectThatAnElementBecomesInvisibleTest {

    private HttpServer server;
    private WebClient client;

    @Before
    public void startServer() throws IOException {
        server = HttpServer.create( new InetSocketAddress( 8000 ), 0 );
        server.createContext( "/", exchange -> {
            String body = "<html>"
                    + "<head>"
                    + "<script>"
                    + "    hide = function() { "
                    + "        document.getElementById('hiddable').style.display='none'; "
                    + "}"
                    + "</script>"
                    + "</head>"
                    + "<body>"
                    + "<label id=\"hiddable\" onclick=\"hide();\">click me</label>"
                    + "</body>"
                    + "</html>";
            exchange.getResponseHeaders().add( "content-type", "text/html" );
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        server.start();
    }

    @After
    public void stopServer() {
        client.close();
        server.stop( 0 );
    }

    @Test
    public void yesWeCan() throws Exception {
        client = openBrowser();
        HtmlPage page = client.getPage("http://localhost:8000/");
        HtmlElement label = find("label#hiddable", page);

        assertThat(getComputedStyle(label).getDisplay(), equalTo("inline"));
        label.click();
        assertThat(getComputedStyle(label).getDisplay(), equalTo("none"));
    }

    private ComputedCSSStyleDeclaration getComputedStyle(HtmlElement element) {
        WebWindow window = client.getCurrentWindow();
        Window jscript = (Window) window.getScriptableObject();
        HTMLElement scriptable = (HTMLElement) jscript.makeScriptableFor(element);

        return jscript.getComputedStyle(scriptable, null);
    }

    private WebClient openBrowser() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);

        return webClient;
    }

    private HtmlElement find(String selector, HtmlPage page) {
        HtmlElement document = page.getDocumentElement();
        Selectors selectors = new Selectors(new W3CNode(document));

        return (HtmlElement) selectors.querySelector(selector);
    }
}
