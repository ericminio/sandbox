package ericminio.http.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/any")
public class ServiceUsingOutputStream extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String body = "hello world";
        response.setContentType("text/plain");
        response.setContentLength(body.length());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(body.getBytes());
        response.getOutputStream().close();
    }
}
