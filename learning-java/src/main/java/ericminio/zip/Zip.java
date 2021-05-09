package ericminio.zip;

import ericminio.http.FileInfo;
import ericminio.http.FileSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    public byte[] please(FileSet fileSet) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8);
        for (int i = 0; i< fileSet.size(); i++) {
            FileInfo fileInfo = fileSet.getFileInfo(i);
            ZipEntry zipEntry = new ZipEntry(fileInfo.getFileName());
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(fileInfo.getContent().getBytes(), 0, fileInfo.getContent().length());
            zipOutputStream.flush();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
