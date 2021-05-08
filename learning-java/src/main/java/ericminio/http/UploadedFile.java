package ericminio.http;

public class UploadedFile {
    private String fileName;
    private String fieldName;
    private String content;

    public UploadedFile() {

    }
    public UploadedFile(String fieldName, String fileName, String content) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
