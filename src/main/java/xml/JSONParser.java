package xml;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by user on 02/05/2017.
 */
public class JSONParser {
    private JSONArray island;
    
    /**
     * Constructeur de JSONParser, lit le fichier demandé puis crée le JSONObject racine du fichier
     *
     * @param url
     */
    public JSONParser(String url) {
        File file = new File(url);
        StringBuilder data = new StringBuilder();
//        new BufferedReader();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        island = new JSONArray(data.toString());
    }
    /**
     * Fonction qui analyse le fichier et renvoie une liste de MyNode
     * d'abord analyse init puis analyse les actions une par une
     */
    public List<MyNode> parse() {
        List<MyNode> listNode = new ArrayList<>();
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
        
        return listNode;
    }
    
    /**
     * Fonction qui analyse les contrats et créé un mySubNode qui corresponds aux contrats
     *
     * @param contracts
     * @return
     */
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
    
    /**
     * Fonction qui Analyse un JSONObject qui contient les paramètres d'une action et renvoie une HashMap des parametres
     * qui sera ensuite intégrée dans le champ parameters d'une Action
     *
     * @param parameters
     * @return
     */
    public HashMap<String, Object> parseActionParameters(JSONObject parameters) {
        HashMap<String, Object> contentParameters = new HashMap<>();
        Iterator iterator = parameters.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            contentParameters.put(key, parameters.get(key));
        }
        return contentParameters;
    }
    
    /**
     * Fonction qui Analyse un JSONObject qui contient le resultat d'une action et renvoie une HashMap des resultats
     * qui sera ensuite intégrée dans le champ extras d'une Action
     *
     * @param result
     * @return
     */
    public HashMap<String, Object> parseExtras(JSONObject result) {
        JSONObject extras = result.getJSONObject("extras");
        HashMap<String, Object> extrasContent = new HashMap<>();
        Iterator<String> iterator = extras.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (extras.get(key) instanceof JSONArray) {
                extrasContent.put(key, parseArray((JSONArray) extras.get(key), key));
            } else {
                extrasContent.put(key, extras.get(key));
            }
        }
        return extrasContent;
    }
    
    /**
     * Analyse un tableau Json et renvoie une Liste qui contient tous le contennu de l'array JSON
     * fonction recursive si l'array contient un array
     * @param jsonArray
     * @param name
     * @return
     */
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
    /**
     * Analyse un objet Json et renvoie un MySbNode qui contient tous le contennu de l'objet JSON
     * fonction recursive si l'objet contient un autre objet
     * @param jsonObject
     * @param name
     * @return
     */
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
