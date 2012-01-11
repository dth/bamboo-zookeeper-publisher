import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:dale.harrison@morningstar.com">Dale Harrison</a>
 */
public class XpathTest {

    @Test
    public void validXpath() throws Exception {

        XPath xpath = XPathFactory.newInstance().newXPath();
        String artifactId = "";
        String version = "";

        NodeList nodes = ((Node) xpath.evaluate("/", new InputSource(new FileInputStream(new File("pom.xml"))), XPathConstants.NODE)).getFirstChild().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n.getNodeName().equalsIgnoreCase("artifactId")) {
                artifactId = n.getFirstChild().getTextContent();
            }
            if (n.getNodeName().equalsIgnoreCase("version")) {
                version = n.getFirstChild().getTextContent();
            }
        }
        assertEquals("zookeeper-publisher", artifactId);
        assertEquals("1.0-SNAPSHOT", version);
    }
}
