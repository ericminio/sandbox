package ericminio.http;

public class UploadPayloadParser {

    public UploadPayload parse(String incomingBody) {
        UploadPayload uploadPayload = new UploadPayload();
        String token = incomingBody.substring(0, incomingBody.indexOf("\n")).trim();
        String remaining = incomingBody.substring(token.length()).trim();

        String filePayload = remaining.substring(0, remaining.indexOf(token));

        String contentDisposition = filePayload.substring(filePayload.indexOf("Content-Disposition"));
        contentDisposition = contentDisposition.substring(0, contentDisposition.indexOf("\n")).trim();
        String filename = contentDisposition.substring(contentDisposition.indexOf(";filename="));
        filename = filename.substring(";filename=".length());
        String fieldname = contentDisposition.substring(contentDisposition.indexOf(";name="));
        fieldname = fieldname.substring(";name=".length());
        fieldname = fieldname.substring(0, fieldname.indexOf(";"));

        while (filePayload.indexOf("\n") > 1) {
            filePayload = filePayload.substring(filePayload.indexOf("\n")+1);
        }

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setName(filename);
        uploadedFile.setFieldName(fieldname);
        uploadedFile.setContent(filePayload.trim());
        uploadPayload.add(uploadedFile);

        return uploadPayload;
    }
}
