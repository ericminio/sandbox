package ericminio.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileContent {

    public static String contentOf(String filename) throws IOException {
        return new Stringify().inputStream(new FileInputStream(new File(filename)));
    }
}
