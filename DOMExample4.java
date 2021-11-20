
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DOMExample4 {
    
    public static Element findElement(Element node) {
        // Locate the good title element
        //NodeList titles = node.getElementsByTagName("title");
        NodeList titles = node.getElementsByTagNameNS("http://khi.be/ns/library", "title");
        int i = 0;
        boolean not_found = true;
        while (i < titles.getLength() && not_found) {
            String titleText = titles.item(i).getFirstChild().getNodeValue();
            if (titleText.equals("Java & XML")) {
                not_found = false;
            } else {
                i++;
            }
        }
        if (not_found) {
            return null;
        } else {
            return (Element) titles.item(i);
        }
    }
    
    public static void main(String[] args) throws Exception {        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setExpandEntityReferences(false);
        factory.setNamespaceAware(true);
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document doc = parser.parse(args[0]);
        
        Element title = findElement(doc.getDocumentElement());
        if (title == null) {
            System.out.println("Right title not found !");
            System.exit(1);
        }
        
        title.getFirstChild().setNodeValue("Java and XML");
        
        TransformerFactory transFactory = TransformerFactory.newInstance();
        System.out.println("TransformerFactory: " + transFactory.getClass().getName());
        Transformer idTransform = transFactory.newTransformer();
        idTransform.setOutputProperty(OutputKeys.METHOD, "xml");
        idTransform.setOutputProperty(OutputKeys.INDENT, "yes");        
        Source input = new DOMSource(doc);
        Result output = new StreamResult(System.out);
        idTransform.transform(input, output);
    }
}
