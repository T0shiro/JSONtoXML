package xml;


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by user on 02/05/2017.
 */
public class JSONParser {
    private JSONArray island;
    private Document document;
    
    private List<MyNode> listNode;
    
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
        listNode = new ArrayList<>();
    }
    
    public static void main(String[] args) {
        JSONParser jsonParser = new JSONParser("jsonIADA.json");
        jsonParser.parse();
        ToXML toXML = new ToXML();
        toXML.translateNodes(jsonParser.listNode, "ol");
    }
    
    public void parse() {
        JSONObject init = island.getJSONObject(0);
        JSONObject data = init.getJSONObject("data");
        Map<String, Object> content = new HashMap<>();
        content.put("heading", data.get("heading"));
        content.put("men", data.get("men"));
        content.put("budget", data.get("budget"));
        content.put("contracts", parseContracts(data.getJSONArray("contracts")));
        listNode.add(new MySubNode("init", content));
        
        for (int i = 1; i < island.length(); i += 2) {
            JSONObject action = island.getJSONObject(i);
            Map<String, Object> actionContent = new HashMap<>();
            JSONObject actionData = action.getJSONObject("data");
            actionContent.put("action", actionData.get("action"));
            Map<String, Object> parametersContent = new HashMap<>();
            if (actionData.has("parameters")) {
                parametersContent = parseActionParameters(actionData.getJSONObject("parameters"));
            }
            JSONObject result = island.getJSONObject(i + 1);
            JSONObject dataAction = result.getJSONObject("data");
            listNode.add(new Action(actionData.getString("action"), dataAction.getInt("cost"), parseExtras(result.getJSONObject("data")), parametersContent));
        }
    }
    
    
    public MySubNode parseContracts(JSONArray contracts) {
        HashMap<String, Object> contentContracts = new HashMap<>();
        for (int i = 0; i < contracts.length(); i++) {
            HashMap<String, Object> contentContract = new HashMap<>();
            JSONObject contract = contracts.getJSONObject(i);
            contentContract.put("amount", contract.get("amount"));
            contentContract.put("resource", contract.get("resource"));
            MySubNode contractXML = new MySubNode("contract", contentContract);
            contentContracts.put("contract", contractXML);
        }
        return new MySubNode("contracts", contentContracts);
    }
    
    public HashMap<String, Object> parseActionParameters(JSONObject parameters) {
        HashMap<String, Object> contentParameters = new HashMap<>();
        Iterator iterator = parameters.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            contentParameters.put(key, parameters.get(key));
        }
        return contentParameters;
    }
    
    public HashMap<String, Object> parseExtras(JSONObject result) {
        JSONObject extras = result.getJSONObject("extras");
        HashMap<String, Object> extrasContent = new HashMap<>();
        Iterator<String> iterator = extras.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (extras.get(key) instanceof JSONArray) {
                extrasContent.put(key,parseArray((JSONArray) extras.get(key), key));
            } else {
                extrasContent.put(key, extras.get(key));
            }
        }
        return extrasContent;
    }
    
    private List<Object> parseArray(JSONArray jsonArray, String name) {
        List<Object> allArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object o = jsonArray.get(i);
            if (o instanceof JSONObject) {
                allArray.add(parseObject((JSONObject) o, name));
            } else if (o instanceof JSONArray) {
                allArray.add(parseArray((JSONArray) o, name));
            } else {
                allArray.add(o);
            }
        }
        return allArray;
    }
    
    private MySubNode parseObject(JSONObject jsonObject, String name) {
        Map<String, Object> content = new HashMap<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                content.put(key, parseObject(jsonObject, key));
            } else {
                content.put(key, jsonObject.get(key).toString());
            }
        }
        return new MySubNode(name, content);
        
    }
    
}
