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
        System.out.println(jsonParser.listNode);
    }

    public void parse() {
        JSONObject init = island.getJSONObject(0);
        JSONObject data = init.getJSONObject("data");
        Map<String, Object> content = new HashMap<>();
        content.put("heading", data.get("heading"));
        content.put("men", data.get("men"));
        content.put("budget", data.get("budget"));
        content.put("contracts", parseContracts(data.getJSONArray("contracts")));
        content.put("time", init.get("time"));
        listNode.add(new SubNode("init", content));

        for (int i = 1; i < island.length(); i += 2) {
            JSONObject action = island.getJSONObject(i);
            Map<String, Object> actionContent  = new HashMap<>();
            data = action.getJSONObject("data");
            actionContent.put("action", data.get("action"));
            if (action.has("parameters")) {
                actionContent.put("parameters", parseActionParameters(data.getJSONObject("parameters")));
            }
            JSONObject result = island.getJSONObject(i + 1);
            actionContent.put("result", parseResult(result.getJSONObject("data")));
            listNode.add(new SubNode("action", actionContent));
        }
    }



    public SubNode parseContracts(JSONArray contracts) {
        HashMap<String, Object> contentContracts = new HashMap<>();
        for (int i = 0; i < contracts.length(); i++) {
            HashMap<String, Object> contentContract = new HashMap<>();
            JSONObject contract = contracts.getJSONObject(i);
            contentContract.put("amount", contract.get("amount"));
            contentContract.put("resource", contract.get("resource"));
            SubNode contractXML = new SubNode("contract", contentContract);
            contentContracts.put("contract", contractXML);
        }
        return new SubNode("contracts", contentContracts);
    }

    public SubNode parseActionParameters(JSONObject parameters) {
        HashMap<String, Object> contentParameters = new HashMap<>();
        Iterator iterator = parameters.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            contentParameters.put(key, parameters.get(key));
        }
        return new SubNode("parameters", contentParameters);
    }

    public SubNode parseResult(JSONObject result) {
        HashMap<String, Object> contentResult = new HashMap<>();
        contentResult.put("cost", result.get("cost"));
        JSONObject extras = result.getJSONObject("extras");
        HashMap<String, Object> extrasContent = new HashMap<>();
        Iterator iterator = extras.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            extrasContent.put(key, extras.get(key));
        }
        contentResult.put("extras", new SubNode("extras", extrasContent));
        return new SubNode("result", contentResult);
    }
}
