package ericminio.http.servlet;

import com.sun.net.httpserver.HttpServer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.lang.annotation.Annotation;

public class AddContextFrom {
    private HttpServlet httpServlet;

    public AddContextFrom(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
    }

    public void in(HttpServer server) {
        Annotation[] annotations = httpServlet.getClass().getAnnotations();
        WebServlet annotation = (WebServlet) annotations[0];
        String[] value = annotation.value();
        String url = value[0];
        server.createContext( url, exchange -> {
            new WebServletWrapper(httpServlet).handle(exchange);
        } );
    }
}
