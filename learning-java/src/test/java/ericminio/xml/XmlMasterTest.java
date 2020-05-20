package ericminio.xml;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class XmlMasterTest {

    private XmlMaster extract = new XmlMaster();

    @Test
    public void canExtractContentByAttributeValue() {
        String input = "<a name=\"hello\" ignore=\"this\"><b>world</b></a>";

        assertThat(extract.contentByTagAndAttribute(input, "a", "name", "hello"),
                equalTo("<b>world</b>"));
    }
    @Test
    public void canExtractContentByTag() {
        String input = "<b>world</b>";

        assertThat(extract.contentByTag(input, "b"),
                equalTo("world"));
    }
    @Test
    public void canExtractContentByTagWithNamespace() {
        String input = "<ns:b>world</ns:b>";

        assertThat(extract.contentByTag(input, "b"),
                equalTo("world"));
    }
    @Test
    public void canExtractContentByAttributeValueWithNamespace() {
        String input = "<ns2:a name=\"hello\"><ns1:b>world</ns1:b></ns2:a>";

        assertThat(extract.contentByTagAndAttribute(input, "a", "name", "hello"),
                equalTo("<ns1:b>world</ns1:b>"));
    }
    @Test
    public void resistsEmptyValue() {
        String input = "<ns:b/>";

        assertThat(extract.contentByTag(input, "b"),
                equalTo(""));
    }
    @Test
    public void resistsMultipleAttributes() {
        String input = "<a first=\"hello\" second=\"world\"><b>found</b></a>";

        assertThat(extract.contentByTagAndAttribute(input, "a", "first", "hello"),
                equalTo("<b>found</b>"));
    }
    @Test
    public void canExtractSeveralChildren() {
        String input = "<a greeting=\"hello\" who=\"world\"><b>hello</b><c>world</c></a>";

        assertThat(extract.contentByTagAndAttribute(input, "a", "greeting", "hello"),
                equalTo("<b>hello</b><c>world</c>"));
    }
    @Test
    public void isUsuallyWhatWeNeed() {
        String input = "" +
                "<ns1:Nodes>" +
                "   <ns2:Node attribute=\"skip-me\" ignore=\"that\">" +
                "       <ns3:Value>hello all</ns3:Value>" +
                "   </ns2:Node>" +
                "   <ns2:Node attribute=\"select-me\" ignore=\"this\">" +
                "       <ns3:Value>" +
                "           hello world" +
                "       </ns3:Value>" +
                "   </ns2:Node>" +
                "</ns1:Nodes>";
        String content = extract.contentByTagAndAttribute(input, "Node", "attribute", "select-me");
        String value = extract.contentByTag(content, "Value");

        assertThat(value, equalTo("hello world"));
    }
}
