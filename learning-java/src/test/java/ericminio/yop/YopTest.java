package ericminio.yop;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public abstract class YopTest {

    protected abstract String getYopFileName();
    private List<MethodMatcher> methodMatchers;
    List<String> input;

    @Before
    public void parseYopFile() throws IOException {
        String packageName = this.getClass().getPackage().getName();
        List<String> segments = new ArrayList<>();
        segments.addAll(Arrays.asList(packageName.split("\\.")));
        segments.add(0, "java");
        segments.add(0, "test");
        segments.add(0, "src");
        segments.add(0, ".");
        segments.add(getYopFileName());
        String pathName = segments.stream().collect(Collectors.joining(File.separator));
        input = Files.readAllLines(Paths.get(pathName));

        methodMatchers = new ArrayList<>();
        for (Method method:this.getClass().getMethods()) {
            if (method.getAnnotation(Yop.class) != null) {
                methodMatchers.add(new MethodMatcher(method));
            }
        }
    }

    private void debug(String message) {
//        System.out.print("\n" + message);
    }

    @Test
    public void yopMethodsFound() {
        assertThat(methodMatchers.size(), greaterThan(0));
        for (MethodMatcher methodMatcher: methodMatchers) {
            Yop annotation = methodMatcher.method.getAnnotation(Yop.class);
            assertThat(annotation, notNullValue());
            assertThat(annotation.match(), notNullValue());
            assertThat(annotation.match().trim(), not(equalTo("")));
            debug("method=" + methodMatcher.method.getName() + ", match=" + methodMatcher.match);
        }
    }

    @Test
    public void yopMatchingIsReady() {
        String line = "   Then the feedback is 3, 0";
        String match = "the feedback is (.*)";
        Pattern compile = Pattern.compile(match);
        Matcher matcher = compile.matcher(line);

        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.groupCount(), equalTo(1));
        assertThat(matcher.group(1), equalTo("3, 0"));
    }

    @Test
    public void yopTests() {
        int invokationCount = 0;

        for (int i=0; i<input.size(); i++) {
            String line = input.get(i);
            for (int j=0; j<methodMatchers.size(); j++) {
                MethodMatcher methodMatcher = methodMatchers.get(j);
                Match match = methodMatcher.matches(line);
                if (match.isYes()) {
                    try {
                        String[] parameters = match.getParameters();
                        debug("invoking " + methodMatcher.method.getName() + " with " + parameters.length + " parameters");
                        methodMatcher.method.invoke(this, parameters);
                        invokationCount ++;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        fail();
                    } catch (InvocationTargetException e) {
                        if (e.getCause() instanceof AssertionError) {
                            e.getCause().printStackTrace();
                        }
                        else {
                            e.printStackTrace();
                        }
                        fail(line + "(" + getYopFileName() + ":" + (i+1) + ")");
                    }
                    break;
                }
            }
        }
        assertThat(invokationCount, greaterThan(0));
    }

    class MethodMatcher {
        private Method method;
        private Pattern pattern;
        private String match;

        public MethodMatcher(Method method) {
            this.method = method;
            this.match = method.getAnnotation(Yop.class).match();
            this.pattern = Pattern.compile(match);
        }

        public Match matches(String line) {
            return new Match(pattern.matcher(line));
        }

    }
    class Match {

        private final boolean yes;
        private Matcher matcher;

        public Match(Matcher matcher) {
            this.matcher = matcher;
            this.yes = matcher.find();
        }

        public boolean isYes() {
            return yes;
        }

        public String[] getParameters() {
            String[] parameters = new String[this.matcher.groupCount()];
            int index = 0;
            while (index < this.matcher.groupCount()) {
                parameters[index] = this.matcher.group(index+1);
                index += 1;
            }
            return parameters;
        }
    }
}


