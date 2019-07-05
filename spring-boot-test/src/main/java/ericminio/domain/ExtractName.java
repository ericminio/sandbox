package ericminio.domain;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExtractName {

    public String please(String content, String template) {
        Pattern pattern = Pattern.compile(template.replace(" ", "\\s").replace("%s", "(.*)"));
        Matcher matcher = pattern.matcher(content);
        matcher.find();

        return matcher.group(1);
    }
}
