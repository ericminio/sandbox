package ericminio.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClearFile {

    public static void clearFile(String fileName) throws IOException {
        Files.write(Paths.get(fileName), "Hello".getBytes());
    }
}
