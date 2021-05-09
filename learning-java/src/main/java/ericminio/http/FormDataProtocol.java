package ericminio.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FormDataProtocol {
    public static String boundary = "token";
    public static String hyphens = "-----";
    public static String twoHyphens = "--";
    public static String end = "\n";

    public void post(FormDataSet set, HttpURLConnection request) throws IOException {
        request.setRequestProperty("Content-Type", getRequestContentType());
        OutputStream outputStream = request.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        for (int i = 0; i< set.size(); i++) {
            FormData formData = set.get(i);
            dataOutputStream.writeBytes(fileStart());
            if (formData instanceof FileInfo) {
                dataOutputStream.writeBytes(getFileContentDisposition(formData.getName(), ((FileInfo) formData).getFileName()));
                dataOutputStream.writeBytes(getFileContentType());
            }
            else {
                dataOutputStream.writeBytes(getFileContentDisposition(formData.getName()));
            }
            dataOutputStream.writeBytes(end);
            dataOutputStream.write(formData.getValue().getBytes(), 0, formData.getValue().getBytes().length);
            dataOutputStream.writeBytes(fileEnd());
        }

        dataOutputStream.writeBytes(payloadEnd());
        dataOutputStream.flush();
    }

    public FormDataSet parse(String incomingBody) {
        FormDataSet formDataSet = new FormDataSet();
        String separator = extractFormDataSeparator(incomingBody);
        String remaining = skipFirstSeparator(incomingBody, separator);
        while (hasMore(remaining)) {
            String dataPayload = remaining.substring(0, remaining.indexOf(separator));
            String contentDisposition = extractContentDisposition(dataPayload);
            String fieldname = extractFieldName(contentDisposition);
            while (dataPayload.indexOf("\n") > 1) {
                dataPayload = dataPayload.substring(dataPayload.indexOf("\n") + 1);
            }
            String value = dataPayload.trim();

            if (contentDisposition.indexOf("filename=") != -1) {
                String filename = contentDisposition.substring(contentDisposition.indexOf("filename="));
                filename = unquote(filename.substring("filename=".length()).trim());
                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(fieldname);
                fileInfo.setContent(value);
                fileInfo.setFileName(filename);
                formDataSet.add(fileInfo);
            }
            else {
                FormData formData = new FormData();
                formData.setName(fieldname);
                formData.setValue(value);
                formDataSet.add(formData);
            }
            remaining = remaining.substring(remaining.indexOf(separator) + separator.length()).trim();
        }
        return formDataSet;
    }

    private String getRequestContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    private String getFileContentType() {
        return "Content-Type:application/octet-stream" + end;
    }

    private String fileStart() {
        return hyphens + boundary + end;
    }

    private static String fileEnd() {
        return end;
    }

    private String payloadEnd() {
        return hyphens + boundary + twoHyphens + end;
    }

    private String getFileContentDisposition(String fieldName, String fileName) {
        return "Content-Disposition:form-data;name="+fieldName+";filename=" + fileName + end;
    }

    private String getFileContentDisposition(String fieldName) {
        return "Content-Disposition:form-data;name="+fieldName + end;
    }

    private String unquote(String input) {
        String unquoted = input;
        if (unquoted.startsWith("\"")) {
            unquoted = unquoted.substring(1);
        }
        if (unquoted.endsWith("\"")) {
            unquoted = unquoted.substring(0, unquoted.length()-1);
        }
        return unquoted;
    }

    private boolean hasMore(String remaining) {
        return !remaining.equalsIgnoreCase(FormDataProtocol.twoHyphens);
    }

    private String skipFirstSeparator(String incomingBody, String separator) {
        return incomingBody.substring(separator.length()).trim();
    }

    private String extractFormDataSeparator(String incomingBody) {
        return incomingBody.substring(0, incomingBody.indexOf("\n")).trim();
    }

    private String extractFieldName(String contentDisposition) {
        String fieldname = contentDisposition.substring(contentDisposition.indexOf("name="));
        fieldname = fieldname.substring("name=".length());
        if (fieldname.indexOf(";") != -1) {
            fieldname = fieldname.substring(0, fieldname.indexOf(";"));
        }
        fieldname = unquote(fieldname).trim();
        return fieldname;
    }

    private String extractContentDisposition(String dataPayload) {
        String contentDisposition = dataPayload.substring(dataPayload.indexOf("Content-Disposition"));
        contentDisposition = contentDisposition.substring(0, contentDisposition.indexOf("\n")).trim();
        return contentDisposition;
    }
}
