
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class DOMExample3 {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder parser = factory.newDocumentBuilder();
        DOMImplementation domImpl = parser.getDOMImplementation();

        DocumentType type = domImpl.createDocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        Document doc = domImpl.createDocument("http://www.w3.org/1999/xhtml", "html", type);

        Element head = doc.createElement("head");
        doc.getDocumentElement().appendChild(head);
        Element title = doc.createElement("title");
        head.appendChild(title);
        title.appendChild(doc.createTextNode("Titre de la page"));

        Element body = doc.createElement("body");
        doc.getDocumentElement().appendChild(body);

        Element para = doc.createElement("p");
        body.appendChild(para);
        para.appendChild(doc.createTextNode("this is a "));
        Element span = doc.createElement("span");
        para.appendChild(span);
        span.setAttribute("style", "color: red;");
        span.appendChild(doc.createTextNode("paragraph"));

        body.appendChild(para.cloneNode(true));

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