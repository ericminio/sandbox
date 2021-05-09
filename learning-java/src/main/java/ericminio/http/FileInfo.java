package ericminio.http;

public class FileInfo extends FormData {
    private String fileName;
    private String contentType;

    public FileInfo() {
    }
    public FileInfo(String name, String fileName, String value) {
        setName(name);
        setValue(value);
        setFileName(fileName);
        setContentType("text/plain");
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return getValue();
    }

    public void setContent(String content) {
        setValue(content);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
