package ericminio.zip;

import java.util.ArrayList;
import java.util.List;

public class FileSet {

    List<FileInfo> set;

    public FileSet() {
        set = new ArrayList<>();
    }

    public int size() {
        return set.size();
    }

    public void add(FileInfo fileInfo) {
        set.add(fileInfo);
    }

    public FileInfo get(int index) {
        return set.get(index);
    }
}
