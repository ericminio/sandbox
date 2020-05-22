package ericminio.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUtils {

    public String contentByTagAndAttribute(String input, String tag, String name, String value) {
        Selection selection = new Selection(input);
        selection = selection.split(attribute(tag, name, value));
        selection = selection.split(closing(tag));

        return selection.before;
    }

    public String contentByTag(String input, String tag) {
        Selection selection = new Selection(input);
        selection = selection.split(opening(tag));
        selection = selection.split(closing(tag));

        return selection.before;
    }

    public String attributeValueByTag(String input, String tag, String name) {
        Selection selection = new Selection(input);
        selection = selection.extract(opening(tag));
        selection = selection.extract(attributeValue(name));

        return selection.input;
    }

    private String opening(String tag) {
        return "<[^/^<^\\s]*:?" + tag + "[^<]*>";
    }

    private String closing(String tag) {
        return "</[^>^\\s]*:?" + tag + ">";
    }

    private String attribute(String tag, String name, String value) {
        return "<[^/]*:?" + tag + "[^>]*" + name+"=\""+value+"\"" + "[^<]*>";
    }

    private String attributeValue(String name) {
        return name + "=\"([^\"]*)\"";
    }

    class Selection {
        public String before;
        public String input;

        public Selection(String input) {
            this("", input);
        }

        public Selection(String before, String tail) {
            this.before = before.trim();
            this.input = tail.trim();
        }

        public Selection split(String token) {
            Pattern p = Pattern.compile(token);
            Matcher matcher = p.matcher(this.input);
            if (matcher.find()) {
                String before = input.substring(0, matcher.start());
                String tail = input.substring(matcher.end());

                return new Selection(before, tail);
            }
            return new Selection("", "");
        }

        public Selection extract(String token) {
            Pattern p = Pattern.compile(token);
            Matcher matcher = p.matcher(this.input);
            if (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    return new Selection(matcher.group(1));
                }
                else {
                    return new Selection(matcher.group());
                }
            }
            return new Selection("");
        }
    }
}
