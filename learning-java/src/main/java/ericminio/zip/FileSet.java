package ericminio.zip;

import java.util.ArrayList;
import java.util.List;

public class FileSet {

    List<FileInfo> files;

    public FileSet() {
        this.files = new ArrayList<>();
    }

    public int size() {
        return this.files.size();
    }

    public FileInfo getFileInfo(int index) {
        return this.files.get(index);
    }

    public void add(FileInfo fileInfo) {
        this.files.add(fileInfo);
    }

    public FileInfo getFileInfoByFieldName(String field) {
        for (int i=0; i<size(); i++) {
            FileInfo candidate = getFileInfo(i);
            if (candidate.getFieldName().equalsIgnoreCase(field)) {
                return candidate;
            }
        }
        return null;
    }
}
