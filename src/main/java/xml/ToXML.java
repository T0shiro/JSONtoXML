package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 16/05/2017.
 */
public class ToXML {
    private Document document;

    public void translate(List<MyNode> nodes, String file) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Element island = document.createElement("island");
        document.appendChild(island);
        island.appendChild(translate((SubNode) nodes.get(0)));
        Element actions = document.createElement("actions");
        island.appendChild(actions);
        for (int i = 1; i < nodes.size(); i++) {
            actions.appendChild(translate((Action) nodes.get(i)));
        }
        System.out.println(document);
    }

    private Element translate(Action action) {
        Element element = document.createElement(action.getName());
        element.appendChild(translate(action.parameters, "parameters"));
        element.appendChild(translate(action.extras, "extras"));
        return element;
    }

    private Element translate(SubNode subNode) {
        return translate(subNode.content, subNode.getName());
    }

    private Element translate(Map<String, Object> map, String name) {
        Element element = document.createElement(name);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof SubNode) {
                element.appendChild(translate((SubNode) entry.getValue()));
            } else {
                Element child = document.createElement(entry.getKey());
                child.setTextContent(entry.getValue().toString());
                element.appendChild(child);
            }
        }
        return element;
    }
}


