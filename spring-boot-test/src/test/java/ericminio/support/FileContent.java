package ericminio.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileContent {

    public static String contentOf(String filename) throws IOException {
        return Files.readAllLines(Paths.get(filename)).stream().collect(Collectors.joining());
    }

    public static String lastLineOf(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));

        return lines.size() > 0 ? lines.get(lines.size()-1) : null;
    }
}
