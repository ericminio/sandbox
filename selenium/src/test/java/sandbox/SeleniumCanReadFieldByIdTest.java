package sandbox;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import support.Drivers;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class SeleniumCanReadFieldByIdTest {

    private HttpServer server;
    private WebDriver browser;

    @Before
    public void startServer() throws IOException {
        server = HttpServer.create( new InetSocketAddress( 8000 ), 0 );
        server.createContext( "/", exchange -> {
            String body = "<html>"
                    + "<body>"
                    + "<label id=\"greetings\">hello</label>"
                    + "</body>"
                    + "</html>";
            exchange.getResponseHeaders().add( "content-type", "text/html" );
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        server.start();
        browser = Drivers.htmlunit();
    }

    @After
    public void closeClientAndStopServer() {
        browser.quit();
        server.stop( 0 );
    }
    @Test
    public void forExampleALabel() {
        browser.navigate().to( "http://localhost:8000/" );
        WebElement element = browser.findElement(By.id("greetings"));

        assertThat(element.getText(), equalTo("hello"));
    }
}
