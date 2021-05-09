package ericminio.zip;

public class FileInfo {
    private String contentType;
    private String fileName;
    private String content;

    public FileInfo(String fileName, String content) {
        setFileName(fileName);
        setContent(content);
        setContentType("plain/text");
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
