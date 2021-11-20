
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

class NodeTyper {

    private final static Map<Short, String> types;

    static {
        types = new HashMap<>();
        // part of the tree
        types.put(Node.DOCUMENT_NODE, "Document");
        types.put(Node.DOCUMENT_TYPE_NODE, "Document Type");
        types.put(Node.ELEMENT_NODE, "Element");
        types.put(Node.TEXT_NODE, "Text");
        types.put(Node.COMMENT_NODE, "Comment");
        types.put(Node.PROCESSING_INSTRUCTION_NODE, "Processing Instruction");
        types.put(Node.CDATA_SECTION_NODE, "CDATA Section");
        types.put(Node.ENTITY_REFERENCE_NODE, "Entity Reference");
        // not part of the tree
        types.put(Node.ENTITY_NODE, "Entity");
        types.put(Node.ATTRIBUTE_NODE, "Attribute");
        types.put(Node.DOCUMENT_FRAGMENT_NODE, "Document Fragment");
        types.put(Node.NOTATION_NODE, "Notation");
    }

    public static String getTypeName(short type) {
        return types.get(type);
    }
}

// Based on Elliotte Rusty Harold PropertyPrinter class in
// "Processing XML with Java"
class NodePrinter {

    private int nodeCount = 0;

    public void printNode(Node node) throws IOException {
        if (node.getNodeType() == Node.DOCUMENT_NODE
                || node.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) {
            nodeCount = 1;
        }
        String name = node.getNodeName();
        String type = NodeTyper.getTypeName(node.getNodeType());
        String localName = node.getLocalName();
        String uri = node.getNamespaceURI();
        String prefix = node.getPrefix();
        String value = node.getNodeValue();
        if (node.getNodeType() == Node.TEXT_NODE && value.matches("^[\\t\\n ]+$")) {
        ArrayList<String> codePoints = new ArrayList<>();
        for (int i = 0; i < value.length(); i++) {
            int codePoint = value.codePointAt(i);
            codePoints.add(String.format("0x%X", codePoint));
        }
        value = join(codePoints);
        }
        System.out.print(String.format("[%s][%d, N:%s, V:\"%s\", P:%s, LN:%s, URI:%s]\n",
                type, nodeCount, name, value, prefix, localName, uri));
        nodeCount++;
    }
        public static String join(ArrayList a) {
        return join(a, null);
    }

    public static String join(ArrayList a, String separator) {
        StringBuilder buffer = new StringBuilder();
        int i = 0;
        for (; i < a.size() - 1; i++) {
            buffer.append(a.get(i));
            if (separator != null) {
                buffer.append(separator);
            }
        }
        if (i < a.size()) {
            buffer.append(a.get(i));
        }
        return buffer.toString();
    }
}

public class DOMExample2 {

    private static NodePrinter nodePrinter = new NodePrinter();

    public static void main(String[] args) throws Exception {
        System.out.println("LEGEND: [Type][Node count, Name, Value, Prefix, Local Name, NS URI]");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setNamespaceAware(true);
        DocumentBuilder parser = factory.newDocumentBuilder();

        try {
            Document doc = parser.parse(args[0]);
            System.out.println("Parsing OK.");
            visitNode(doc);
        } catch (DOMException e) {
            System.err.printf("DOMException: %s\n", e.getMessage());
        }
    }

    static void visitNode(Node node) throws IOException {
        nodePrinter.printNode(node);
        if (node.hasChildNodes()) {
            Node firstChild = node.getFirstChild();
            visitNode(firstChild);
        }
        Node nextNode = node.getNextSibling();
        if (nextNode != null) {
            visitNode(nextNode);
        }
    }
}