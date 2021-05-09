package ericminio.http;

public class FileSet extends FormDataSet {

    public static FileSet from(FormDataSet input) {
        FileSet set = new FileSet();
        for (int i=0; i<input.size(); i++) {
            FormData candidate = input.get(i);
            if (candidate instanceof FileInfo) {
                set.add((FileInfo) candidate);
            }
        }
        return set;
    }

    public FileInfo getFileInfo(int index) {
        return (FileInfo) get(index);
    }
}
