package xml;

import java.util.List;

/**
 * Created by Thomas on 20/05/2017.
 */
public class MainAnalyser {
    
    public static final String DEVIATION = "Ecart-type du coût: ";
    
    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser();
        List<Action> actions = xmlParser.parse("file.xml");
        Analyser analyser = new Analyser();
        System.out.println("Nombres d'actions: " + actions.size());
        System.out.println("Coût moyen: " + analyser.meanCost(actions));
        System.out.println(DEVIATION + analyser.deviationCost(actions));
        List<List<Action>> actionsList = analyser.separateByType(actions);
        for (List<Action> actionList : actionsList) {
            System.out.println("\n=========================================================\n");
            String name = actionList.get(0).getName();
            System.out.println("Nombre de " + name + ": " + actionList.size());
            System.out.println("Coût moyen de " + name + ": " + analyser.meanCost(actionList));
            System.out.println(DEVIATION + " de " + name + ": " + analyser.deviationCost(actionList));
        }
    }
}
