package ericminio.http.servlet;

import com.sun.net.httpserver.HttpServer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.lang.annotation.Annotation;

public class AddContextFrom {
    private HttpServlet httpServlet;
    private WebServletWrapper wrapper;

    public AddContextFrom(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
        this.wrapper = new WebServletWrapper(httpServlet);
    }

    public void in(HttpServer server) throws ServletException {
        in(server, null);
    }

    public void in(HttpServer server, ServletConfig servletConfig) throws ServletException {
        wrapper.init(servletConfig);
        server.createContext(url(), exchange -> wrapper.handle(exchange));
    }

    private String url() {
        Annotation[] annotations = httpServlet.getClass().getAnnotations();
        WebServlet annotation = (WebServlet) annotations[0];
        String[] value = annotation.value();
        return value[0];
    }
}
