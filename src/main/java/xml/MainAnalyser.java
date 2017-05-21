package xml;

import java.util.List;

/**
 * Created by Thomas on 20/05/2017.
 */
public class MainAnalyser {
    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser();
        List<Action> actions = xmlParser.parse("file.xml");
        Analyser analyser = new Analyser();
        System.out.println("Nombres d'actions: " + actions.size());
        System.out.println("Moyenne: " + analyser.meanCost(actions));
        System.out.println("Ecart-type: " + analyser.deviationCost(actions));
        List<List<Action>> actionsList = analyser.separateByType(actions);
        for (List<Action> actionList : actionsList) {
            System.out.println("\n=========================================================\n");
            String name = actionList.get(0).getName();
            System.out.println("Nombre de " + name + ": " + actionList.size());
            System.out.println("Moyenne de " + name + ": " + analyser.meanCost(actionList));
            System.out.println("Ecart-type de " + name + ": " + analyser.deviationCost(actionList));
        }
    }
}
