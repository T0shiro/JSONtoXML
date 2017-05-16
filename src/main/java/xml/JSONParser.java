package xml;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by user on 02/05/2017.
 */
public class JSONParser {
    private JSONArray island;
    private Document document;

    public JSONParser(String url) {
        File file = new File(url);
        String data = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        island = new JSONArray(data);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JSONParser jsonParser = new JSONParser("jsonIADA.json");
        try {
            jsonParser.parse();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void parse() throws ParserConfigurationException, TransformerException {
        JSONObject init = island.getJSONObject(0);
        JSONObject data = init.getJSONObject("data");
        Element islandXML = document.createElement("island");
        document.appendChild(islandXML);
        Element initXML = document.createElement("init");
        islandXML.appendChild(initXML);
        initXML.appendChild(createXMLElement("heading", data));
        initXML.appendChild(createXMLElement("men", data));
        initXML.appendChild(createXMLElement("budget", data));
        JSONArray contractsJSon = data.getJSONArray("contracts");
        initXML.appendChild(parseContracts(contractsJSon));
        initXML.appendChild(createXMLElement("time", init));
        Element actions = document.createElement("actions");
        islandXML.appendChild(actions);
        for (int i = 1; i < island.length(); i++) {
            Element action = document.createElement("action");
            JSONObject jsonObject = island.getJSONObject(i);
            data = jsonObject.getJSONObject("data");
            analyze(data,action);
            islandXML.appendChild(action);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(new File("salut.xml")));

        //Todo : FAIRE DES OBJETS

    }

    public void analyze(JSONObject obj, Element element) {
        for (String keyName : obj.keySet()) {
            Object subObj = obj.get(keyName);
            if(keyName.equals("action")){
                keyName="name";
            }
            Element subElement = document.createElement(keyName);
            element.appendChild(subElement);
            if (subObj instanceof JSONObject) {
                analyze((JSONObject) subObj, subElement);
            } else {
                element.setTextContent(subObj.toString());
            }
        }
    }

    public Element parseContracts(JSONArray contracts) {
        Element contractsXML = document.createElement("contracts");
        for (int i = 0; i < contracts.length(); i++) {
            JSONObject contract = contracts.getJSONObject(i);
            Element contractXML = document.createElement("contract");
            contractXML.appendChild(createXMLElement("amount", contract));
            contractXML.appendChild(createXMLElement("resource", contract));
            contractsXML.appendChild(contractXML);
        }
        return contractsXML;
    }

    public Element createXMLElement(String tagName, JSONObject jsonObject) {
        Object value = jsonObject.get(tagName);
        Element element = document.createElement(tagName);
        element.setTextContent(value.toString());
        return element;
    }
}
