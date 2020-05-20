package ericminio.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlMaster {

    public String contentByTagAndAttribute(String input, String tag, String name, String value) {
        Selection selection = new Selection(input);
        selection = selection.split(attribute(name, value));
        selection = selection.split(closing(tag));

        return selection.before;
    }

    public String contentByTag(String input, String tag) {
        Selection selection = new Selection(input);
        selection = selection.split(opening(tag));
        selection = selection.split(closing(tag));

        return selection.before;
    }

    public String opening(String tag) {
        return "<[^/]*:?" + tag + "[^<]*>";
    }

    public String closing(String tag) {
        return "</[^>]*:?" + tag + ">";
    }

    public String attribute(String name, String value) {
        return name + "=\"" + value + "\"" + "[^<]*>";
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
    }
}
