package support;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class FakeHttpExchange extends HttpExchange {
    private Map<String, Object> attributes;

    public FakeHttpExchange() {
        attributes = new HashMap<>();
        setAttribute("method", "GET");
        setAttribute("body", "");
    }

    @Override
    public Headers getRequestHeaders() {
        return null;
    }

    @Override
    public Headers getResponseHeaders() {
        return null;
    }

    @Override
    public URI getRequestURI() {
        String value = (String) attributes.get("uri");
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getRequestMethod() {
        return (String) attributes.get("method");
    }

    @Override
    public HttpContext getHttpContext() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getRequestBody() {
        return new ByteArrayInputStream(((String)attributes.get("body")).getBytes());
    }

    @Override
    public OutputStream getResponseBody() {
        return null;
    }

    @Override
    public void sendResponseHeaders(int i, long l) throws IOException {

    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public int getResponseCode() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }

    @Override
    public void setStreams(InputStream inputStream, OutputStream outputStream) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }
}
