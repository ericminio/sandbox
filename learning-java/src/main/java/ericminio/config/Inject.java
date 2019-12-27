package ericminio.config;

import java.lang.reflect.Field;

public class Inject {

    public void visit(Object object) {
        Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Value.class)) {
                try {
                    String key = field.getAnnotation(Value.class).key();
                    String value = new Environment().valueOf(key);
                    field.set(object, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
