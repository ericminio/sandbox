package ericminio.demo.filter;

import ericminio.support.HttpServletRequestAcceptingAdditionalHeaders;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Profile("filtered")
public class FilterAddingHeader implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequestAcceptingAdditionalHeaders expandedRequest = new HttpServletRequestAcceptingAdditionalHeaders((HttpServletRequest) request);
        expandedRequest.addHeader("X-HEADER", "this-value");
        chain.doFilter(expandedRequest, response);
    }
}
