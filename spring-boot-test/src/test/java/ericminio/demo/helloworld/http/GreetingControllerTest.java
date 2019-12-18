package ericminio.demo.helloworld.http;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class GreetingControllerTest {

    @Test
    public void inheritsMapping() throws NoSuchMethodException {
        GreetingController controller = new GreetingController();
        Class<? extends GreetingController> controllerClass = controller.getClass();
        List<Method> methods = Arrays.asList(controllerClass.getMethods());
        Method greetingMethod = methods.get(0);
        System.out.println(greetingMethod.getName());

        List<Annotation> annotations = Arrays.asList(greetingMethod.getAnnotations());
        System.out.println(annotations);
    }
}
