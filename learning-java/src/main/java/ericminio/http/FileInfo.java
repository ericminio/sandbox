package ericminio.http;

public class FileInfo extends FormData {
    private String fileName;

    public FileInfo() {
    }
    public FileInfo(String fieldName, String fileName, String value) {
        setName(fieldName);
        setValue(value);
        setFileName(fileName);
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
}
