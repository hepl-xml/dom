
import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

class MyErrorHandler implements ErrorHandler {

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        System.err.println("*** warning called !");
        throw exception;
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        System.err.println("*** error called !");
        throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("*** fatalError called !");
        throw exception;
    }
}

public class DOMExample1 {

    public static void main(String[] args) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        System.out.printf("DocumentBuilderFactory: %s\n", factory.getClass().getName());

        factory.setValidating(true);

        DocumentBuilder parser = factory.newDocumentBuilder();
        System.out.printf("DocumentBuilder: %s\n", parser.getClass().getName());

        // useless but show how to get to the W3C API
        DOMImplementation domImpl = parser.getDOMImplementation();
        System.out.printf("DOMImplementation: %s\n", domImpl.getClass().getName());

        parser.setErrorHandler(new MyErrorHandler());

        try {
            System.out.print("Parsing XML document... ");
            Document doc = parser.parse(args[0]);
            System.out.println("OK. Document is valid.");
        } catch (SAXException e) {
            System.out.println("KO. SAXException in Main.");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("KO. IOException in Main.");
            System.err.println(e.getMessage());
        }
    }
}