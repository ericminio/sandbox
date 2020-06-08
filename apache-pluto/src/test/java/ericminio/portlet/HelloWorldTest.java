package ericminio.portlet;

import org.apache.pluto.container.driver.PortletServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class HelloWorldTest {

    private Server server;
    private static final int SERVER_PORT = 8080;

    @Before
    public void setUp() throws Exception {
        System.setProperty("org.apache.pluto.embedded.portletIds", "HelloWorldPortlet");
        server = new Server(SERVER_PORT);
        WebAppContext webapp = new WebAppContext("src/main/webapp", "/test");
        webapp.setDefaultsDescriptor("/WEB-INF/jetty-pluto-web-default.xml");
        ServletHolder portletServlet = new ServletHolder(new PortletServlet());
        portletServlet.setInitParameter("portlet-name", "HelloWorldPortlet");
        portletServlet.setInitOrder(1);
        webapp.addServlet(portletServlet, "/PlutoInvoker/HelloWorldPortlet");
        server.addHandler(webapp);
        server.start();
        getTestContext().setBaseUrl("http://localhost:" + SERVER_PORT + "/test");
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testDoView() {
        beginAt("/portal/index.jsp");
        assertTextPresent("Hello World!");
        assertElementPresent("HelloWorldDiv");
    }
}
