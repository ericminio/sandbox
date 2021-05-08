package ericminio.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UploadProtocol {
    public static String boundary = "token";
    public static String hyphens = "-----";
    public static String twoHyphens = "--";
    public static String end = "\n";

    public String getRequestContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    public String getFileContentType() {
        return "Content-Type:application/octet-stream" + end;
    }

    public String fileStart() {
        return hyphens + boundary + end;
    }

    public static String fileEnd() {
        return end;
    }

    public String payloadEnd() {
        return hyphens + boundary + twoHyphens + end;
    }

    public String getFileContentDisposition(String fieldName, String fileName) {
        return "Content-Disposition:form-data;name="+fieldName+";filename=" + fileName + end;
    }

    public void send(UploadPayload uploadPayload, HttpURLConnection request) throws IOException {
        request.setRequestProperty("Content-Type", getRequestContentType());
        OutputStream outputStream = request.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        for (int i=0; i<uploadPayload.size(); i++) {
            UploadedFile uploadedFile = uploadPayload.get(i);
            dataOutputStream.writeBytes(fileStart());
            dataOutputStream.writeBytes(getFileContentDisposition(uploadedFile.getFieldName(), uploadedFile.getFileName()));
            dataOutputStream.writeBytes(getFileContentType());
            dataOutputStream.writeBytes(end);
            dataOutputStream.write(uploadedFile.getContent().getBytes(), 0, uploadedFile.getContent().getBytes().length);
            dataOutputStream.writeBytes(fileEnd());
        }

        dataOutputStream.writeBytes(payloadEnd());
        dataOutputStream.flush();
    }

    public UploadPayload parse(String incomingBody) {
        UploadPayload uploadPayload = new UploadPayload();
        String token = incomingBody.substring(0, incomingBody.indexOf("\n")).trim();
        String remaining = incomingBody.substring(token.length()).trim();

        while (!remaining.equalsIgnoreCase(UploadProtocol.twoHyphens)) {
            String filePayload = remaining.substring(0, remaining.indexOf(token));

            String contentDisposition = filePayload.substring(filePayload.indexOf("Content-Disposition"));
            contentDisposition = contentDisposition.substring(0, contentDisposition.indexOf("\n")).trim();
            String filename = contentDisposition.substring(contentDisposition.indexOf(";filename="));
            filename = filename.substring(";filename=".length());
            String fieldname = contentDisposition.substring(contentDisposition.indexOf(";name="));
            fieldname = fieldname.substring(";name=".length());
            fieldname = fieldname.substring(0, fieldname.indexOf(";"));

            while (filePayload.indexOf("\n") > 1) {
                filePayload = filePayload.substring(filePayload.indexOf("\n") + 1);
            }

            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setFileName(filename);
            uploadedFile.setFieldName(fieldname);
            uploadedFile.setContent(filePayload.trim());
            uploadPayload.add(uploadedFile);

            remaining = remaining.substring(remaining.indexOf(token) + token.length()).trim();
        }

        return uploadPayload;
    }
}
