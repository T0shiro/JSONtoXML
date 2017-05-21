package xml;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 16/05/2017.
 */
public class ToXML {
    private Document document;
    
    public void translateNodes(List<MyNode> nodes, String file) {
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
        island.appendChild(translate((MySubNode) nodes.get(0)));
        Element actions = document.createElement("actions");
        island.appendChild(actions);
        for (int i = 1; i < nodes.size(); i++) {
            actions.appendChild(translate((Action) nodes.get(i)));
        }
        System.out.println(document);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("file.xml"));
        DOMImplementation domImpl = document.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype", "island", "DTD.dtd");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    
    private Element translate(Action action) {
        Element element = document.createElement("action");
        Element name = document.createElement("name");
        name.setTextContent(action.getName());
        Element cost = document.createElement("cost");
        cost.setTextContent(String.valueOf(action.getCost()));
        element.appendChild(name);
        element.appendChild(cost);
        element.appendChild(translate(action.parameters, "parameters"));
        element.appendChild(translate(action.extras, "result"));
        return element;
    }
    
    private Element translate(MySubNode mySubNode) {
        return translate(mySubNode.content, mySubNode.getName());
    }
    
    private Element translate(Map<String, Object> map, String name) {
        Element element = document.createElement(name);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof MySubNode) {
                element.appendChild(translate((MySubNode) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                element.appendChild(translate((List) entry.getValue(), entry.getKey()));
            } else {
                String key = entry.getKey();
                if (key.equals("resource")) {
                    key = "resource_name";
                } else if (key.equals("asked_range")) {
                    key = "range";
                } else if (key.equals("cond")) {
                    key = "condition";
                }
                Element child = document.createElement(key);
                child.setTextContent(entry.getValue().toString());
                element.appendChild(child);
            }
        }
        return element;
    }
    
    //    public Element translateReport(List list, String name) {
    //
    //    }
    //
    public Element translate(List list, String name) {
        Element element = document.createElement(name);
        String childName = name;
        if (name.endsWith("s")) {
            childName = name.substring(0, name.lastIndexOf('s'));
        } else if (name.equals("cell")) {
            childName = "biome";
        }
        for (Object o : list) {
            if (o instanceof MySubNode) {
                MySubNode node = (MySubNode) o;
                node.setName(childName);
                element.appendChild(translate(node));
            } else if (o instanceof List) {
                String subName = name;
                List subList = (List) o;
                if (name.equals("cell")) {
                    subName = "biome";
                    Element biome = document.createElement("biome");
                    document.createElement("biome");
                    biome.setTextContent(subList.get(0).toString());
                    biome.setAttribute("percent", subList.get(1).toString());
                    
                    element.appendChild(biome);
                } else {
                    if (name.equals("report")) {
                        subName = "cell";
                    }
                    element.appendChild(translate(subList, subName));
                }
            } else {
                
                Element child = document.createElement(childName);
                child.setTextContent(o.toString());
                element.appendChild(child);
            }
        }
        return element;
    }
}


