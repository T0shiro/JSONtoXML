package xml;

import java.util.List;

/**
 * Created by Thomas on 20/05/2017.
 */
public class Main {
    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser();
        List<Action> actions = xmlParser.parse("file.xml");
        Analyser analyser = new Analyser();
        System.out.println("nombres d'actions: " + actions.size());
        System.out.println("moyenne: " + analyser.mean(actions));
        System.out.println("ecart-type: " + analyser.deviation(actions));
        List<List<Action>> actionsList = analyser.separateByType(actions);
        for (List<Action> actionList : actionsList) {
            System.out.println("\n=========================================================\n");
            String name = actionList.get(0).getName();
            System.out.println("nombre de " + name + ": " + actionList.size());
            System.out.println("moyenne de " + name + ": " + analyser.mean(actionList));
            System.out.println("écart-type de " + name + ": " + analyser.deviation(actionList));
        }
    }
}
