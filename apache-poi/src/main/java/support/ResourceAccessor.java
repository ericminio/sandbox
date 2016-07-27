package support;

public class ResourceAccessor {

    public static String excelFileWithName(String name) {
        return ResourceAccessor.class.getClassLoader().getResource(name + ".xlsx").getFile();
    }
}
