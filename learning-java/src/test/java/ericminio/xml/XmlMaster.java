package ericminio.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlMaster {

    public String contentByTagAndAttribute(String input, String tag, String attribute, String value) {
        Selection selection = new Selection(input);
        selection = selection.splitByAttributeValue(attribute, value);
        selection = selection.splitByClosingTag(tag);

        return selection.before;
    }

    public String contentByTag(String input, String tag) {
        Selection selection = new Selection(input);
        selection = selection.splitByOpeningTag(tag);
        selection = selection.splitByClosingTag(tag);

        return selection.before;
    }

    class NotFound extends Exception {}
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

        public Selection split(String token) throws NotFound {
            Pattern p = Pattern.compile(token);
            Matcher m = p.matcher(this.input);
            if (m.find()) {
                int index = m.start();
                String before = input.substring(0, index);
                String tail = input.substring(index);
                index = tail.indexOf(">");
                tail = tail.substring(index + 1);

                return new Selection(before, tail);
            }
            throw new NotFound();
        }

        public Selection splitByOpeningTag(String tag) {
            try {
                Selection selection = split("<([^\\s^/]*)?:?" + tag + ">");
                return selection;
            } catch (NotFound e) {
                return new Selection("", "");
            }
        }

        public Selection splitByClosingTag(String tag) {
            try {
                return split("<(/[^\\s^<]*)?:?" + tag + ">");
            } catch (NotFound e) {
                return new Selection("", "");
            }
        }

        public Selection splitByAttributeValue(String attribute, String value) {
            try {
                return split(attribute + "=\"" + value + "\"");
            } catch (NotFound e) {
                return new Selection("", "");
            }
        }
    }
}
