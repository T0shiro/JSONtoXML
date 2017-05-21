package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 20/05/2017.
 */
public class XMLParser {
    public XMLParser() {
    }
    
    public List<Action> parse(String fileName) {
        File file = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        List<Action> actions = new ArrayList<>();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            Node actionsNode = ((Element) doc.getElementsByTagName("island").item(0)).getElementsByTagName("actions").item(0);
            NodeList nodes = actionsNode.getChildNodes();
            nodes.item(0);
            for (int i = 2; i < nodes.getLength(); i++) {
                Node item = nodes.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    actions.add(parseAction((Element) item));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actions;
    }
    
    private Action parseAction(Element element) {
        
        Node parametersNode = element.getElementsByTagName("parameters").item(0);
        Map<String, Object> parameters = parseSubNode(parametersNode);
        Map<String, Object> extras = parseSubNode(element.getElementsByTagName("result").item(0));
        int cost = Integer.parseInt(element.getElementsByTagName("cost").item(0).getTextContent());
        Node name = element.getElementsByTagName("name").item(0);
        return new Action(name.getTextContent(), cost, extras, parameters);
        
    }
    
    
    private Map<String, Object> parseSubNode(Node node) {
        NodeList nodeList = node.getChildNodes();
        Map<String, Object> content = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item.hasChildNodes()) {
                content.put(item.getNodeName(), parseSubNode(item));
            }
            content.put(item.getNodeName(), item.getTextContent());
        }
        return content;
    }
}
