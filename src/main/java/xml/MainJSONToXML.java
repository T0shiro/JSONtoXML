package xml;

import java.util.List;

/**
 * Created by Thomas on 21/05/2017.
 */
public class MainJSONToXML {
    public static void main(String[] args) {
        JSONParser jsonParser = new JSONParser("Explorer_IADA.json");
        List<MyNode> nodes = jsonParser.parse();
        ToXML toXML = new ToXML();
        toXML.writeNodes(nodes, "file.xml");
    }
}
