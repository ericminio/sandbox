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

public class CorsTest {

    private HttpServer origin;
    private HttpServer api;
    private WebClient client;

    @Before
    public void startOrigin() throws IOException {
        origin = HttpServer.create( new InetSocketAddress( 8000 ), 0 );
        origin.createContext( "/", exchange -> {
            String body = "<html>"
                    + "<head>"
                    + "<script>"
                    + "    change = function() { "
                    + "         var xhr = new XMLHttpRequest();"
                    + "         xhr.open('GET', 'http://localhost:8001', true);"
                    + "         xhr.onreadystatechange = function () {"
                    + "             document.getElementById('status').innerHTML=xhr.status; "
                    + "         };"
                    + "         xhr.send();"
                    + "}"
                    + "</script>"
                    + "</head>"
                    + "<body>"
                    + "     <button id=\"go\" onclick=\"change();\">click me</button>"
                    + "     <label id=\"status\">waiting...</label>"
                    + "</body>"
                    + "</html>";
            exchange.getResponseHeaders().add( "content-type", "text/html" );
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        origin.start();
    }

    public void startApiBlockingCrossOriginRequests() throws IOException {
        api = HttpServer.create( new InetSocketAddress( 8001 ), 0 );
        api.createContext( "/", exchange -> {
            String body = "ok";
            exchange.getResponseHeaders().add( "content-type", "text/plain" );
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        api.start();
    }

    public void startApiAllowingAnyOrigin() throws IOException {
        api = HttpServer.create( new InetSocketAddress( 8001 ), 0 );
        api.createContext( "/", exchange -> {
            String body = "ok";
            exchange.getResponseHeaders().add( "content-type", "text/plain" );
            exchange.getResponseHeaders().add( "Access-Control-Allow-Origin", "*" );
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        api.start();
    }

    @After
    public void stopServer() {
        client.close();
        origin.stop( 0 );
        api.stop( 0 );
    }

    @Test
    public void isActiveByDefault() throws Exception {
        startApiBlockingCrossOriginRequests();
        client = openBrowser();
        HtmlPage page = client.getPage("http://localhost:8000/");
        find("#go", page).click();
        Thread.sleep(333);

        assertThat(find("#status", page).getTextContent(), equalTo("0"));
    }

    @Test
    public void canBeOverriden() throws Exception {
        startApiAllowingAnyOrigin();
        client = openBrowser();
        HtmlPage page = client.getPage("http://localhost:8000/");
        find("#go", page).click();
        Thread.sleep(333);

        assertThat(find("#status", page).getTextContent(), equalTo("200"));
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
