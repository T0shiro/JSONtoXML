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
    public static final String BIOME = "biome";
    public static final String CELL = "cell";
    public static final String SUFFIX = "s";
    public static final String PERCENT = "percent";
    public static final String REPORT = "report";
    public static final String COND_PREFIX = "cond";
    public static final String COND_FULL = "condition";
    public static final String RANGE_FULL = "asked_range";
    public static final String RANGE = "range";
    public static final String RESOURCE = "resource";
    public static final String RESOURCE_FULL = "resource_name";
    public static final String ACTION = "action";
    public static final String NAME = "name";
    public static final String COST = "cost";
    public static final String PARAMETERS = "parameters";
    public static final String RESULT = "result";
    public static final String ACTIONS = "actions";
    private Document document;
    
    
    /**
     *Fonction qui recupere une liste de MyNode pour pouvoir les réécrire en xml dans le fichier donné en paramètre
     * la fonction créé un document puis lui rajoute les noeuds nécéssaires pour avoir un xml valide
     * @param nodes
     * @param file
     */
    public void writeNodes(List<MyNode> nodes, String file) {
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
        Element actions = document.createElement(ACTIONS);
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
        StreamResult result = new StreamResult(new File(file));
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
    
    /**
     * traduit un objet Action En un Element Xml valide
     * @param action
     * @return
     */
    private Element translate(Action action) {
        Element element = document.createElement(ACTION);
        Element name = document.createElement(NAME);
        name.setTextContent(action.getName());
        Element cost = document.createElement(COST);
        cost.setTextContent(String.valueOf(action.getCost()));
        element.appendChild(name);
        element.appendChild(cost);
        element.appendChild(translate(action.parameters, PARAMETERS));
        element.appendChild(translate(action.extras, RESULT));
        return element;
    }
    
    /**
     * traduit un MySubNode en un Element Xml valide
     * @param mySubNode
     * @return
     */
    private Element translate(MySubNode mySubNode) {
        return translate(mySubNode.content, mySubNode.getName());
    }
    
    /**
     * traduit une Map  d'objet en element, la fonction parcours la et réécrit clés en noeuds xml
     * @param map
     * @param name
     * @return
     */
    private Element translate(Map<String, Object> map, String name) {
        Element element = document.createElement(name);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof MySubNode) {
                element.appendChild(translate((MySubNode) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                element.appendChild(translate((List) entry.getValue(), entry.getKey()));
            } else {
                String key = entry.getKey();
                if (key.equals(RESOURCE)) {
                    key = RESOURCE_FULL;
                } else if (key.equals(RANGE_FULL)) {
                    key = RANGE;
                } else if (key.equals(COND_PREFIX)) {
                    key = COND_FULL;
                }
                Element child = document.createElement(key);
                child.setTextContent(entry.getValue().toString());
                element.appendChild(child);
            }
        }
        return element;
    }
    
    /**
     * traduit une liste d'objets en un Element xml qui contient une liste d'elements
     * L'élément parent est souvent au pluriel si c'est le cas l'élément fils est au singulié
     * Contient le  cas particulier du report qui est une liste avec des sous elements nommés differements
     * @param list
     * @param name
     * @return
     */
    private Element translate(List list, String name) {
        Element element = document.createElement(name);
        String childName = name;
        if (name.endsWith(SUFFIX)) {
            childName = name.substring(0, name.lastIndexOf(SUFFIX));
        } else if (name.equals(CELL)) {
            childName = BIOME;
        }
        for (Object o : list) {
            if (o instanceof MySubNode) {
                MySubNode node = (MySubNode) o;
                node.setName(childName);
                element.appendChild(translate(node));
            } else if (o instanceof List) {
                String subName = name;
                List subList = (List) o;
                if (name.equals(CELL)) {
                    Element biome = document.createElement(BIOME);
                    document.createElement(BIOME);
                    biome.setTextContent(subList.get(0).toString());
                    biome.setAttribute(PERCENT, subList.get(1).toString());
                    
                    element.appendChild(biome);
                } else {
                    if (name.equals(REPORT)) {
                        subName = CELL;
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


