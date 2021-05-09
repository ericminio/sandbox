package ericminio.http;

import ericminio.zip.FileInfo;
import ericminio.zip.FileSet;

public class BuildFileSet {

    public FileSet from(FormDataSet input) {
        FileSet set = new FileSet();
        for (int i=0; i<input.size(); i++) {
            if (input.get(i) instanceof FileFormData) {
                FileFormData fileFormData = (FileFormData) input.get(i);
                set.add(new FileInfo(fileFormData.getFileName(), fileFormData.getContent()));
            }
        }
        return set;
    }
}
