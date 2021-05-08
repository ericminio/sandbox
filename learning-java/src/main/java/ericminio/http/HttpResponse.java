package ericminio.http;

public class HttpResponse {

    private int statusCode;
    private String body;
    private String contentType;
    private byte[] binaryBody;
    private String contentDisposition;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setBinaryBody(byte[] binaryBody) {
        this.binaryBody = binaryBody;
    }

    public byte[] getBinaryBody() {
        return binaryBody;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }
}
