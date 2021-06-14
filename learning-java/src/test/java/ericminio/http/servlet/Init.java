package ericminio.http.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/init")
public class Init extends HttpServlet {
    private String label;
    private int callCount;

    public Init() {
        this.callCount = 0;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.label = config.getServletName();
        this.callCount ++;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String body = this.label + callCount;
        response.setContentType("text/plain");
        response.setContentLength(body.length());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(body.getBytes());
        response.getOutputStream().close();
    }
}
