package ericminio.http;

import java.util.ArrayList;
import java.util.List;

public class FormDataSet {
    List<FormData> set;

    public FormDataSet() {
        set = new ArrayList<>();
    }

    public int size() {
        return set.size();
    }

    public void add(FormData formData) {
        set.add(formData);
    }

    public FormData get(int index) {
        return set.get(index);
    }

    public FormData getByName(String name) {
        for (int i=0; i<size(); i++) {
            FormData candidate = get(i);
            if (name.equalsIgnoreCase(candidate.getName())) {
                return candidate;
            }
        }
        return null;
    }
}
