package ericminio.http.servlet;

import com.sun.net.httpserver.HttpExchange;
import support.FakeHttpServletRequest;
import support.FakeHttpServletResponse;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class WebServletWrapper {
    private HttpServlet httpServlet;

    public WebServletWrapper(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        httpServlet.init(servletConfig);
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            httpServlet.service(
                    new FakeHttpServletRequest(exchange),
                    new FakeHttpServletResponse(exchange));
        }
        catch (Exception raised) {
            raised.printStackTrace();
            exchange.getResponseHeaders().add("content-type", "text/plain");
            exchange.sendResponseHeaders(500, raised.getMessage().length());
            exchange.getResponseBody().write(raised.getMessage().getBytes());
            exchange.close();
        }
    }
}
