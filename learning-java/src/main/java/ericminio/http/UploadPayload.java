package ericminio.http;

import java.util.ArrayList;
import java.util.List;

public class UploadPayload {

    List<UploadedFile> files;

    public UploadPayload() {
        this.files = new ArrayList<>();
    }

    public int size() {
        return this.files.size();
    }

    public UploadedFile get(int index) {
        return this.files.get(index);
    }

    public void add(UploadedFile uploadedFile) {
        this.files.add(uploadedFile);
    }

    public UploadedFile getFileByFieldName(String field) {
        for (int i=0; i<size(); i++) {
            UploadedFile candidate = get(i);
            if (candidate.getFieldName().equalsIgnoreCase(field)) {
                return candidate;
            }
        }
        return null;
    }
}
