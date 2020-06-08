package ericminio.portlet;

import javax.portlet.*;
import java.io.IOException;

public class HelloWorldPortlet extends GenericPortlet {

    public void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(
                "/view.jsp");
        rd.include(request, response);
    }
}
