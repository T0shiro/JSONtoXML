package xml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 21/05/2017.
 */
public class Analyser {
    public Analyser() {
    }
    
    /**
     * Cette fonction permet de séparer les actions par leur type et renvoie une Liste de liste d'actions
     * @param actions
     * @return
     */
    public List<List<Action>> separateByType(List<Action> actions) {
        List<List<Action>> actionsList = actions.stream()
                .collect(Collectors.groupingBy(x -> x.getName()))
                .values().stream()
                .map(e -> {
                    List<Action> c = new ArrayList<>();
                    c.addAll(e);
                    return c;
                })
                .collect(Collectors.toList());
        return actionsList;
    }
    
    /**
     * fonction qui calcule la moyenne du cout d'une liste d'action
     * @param actions
     * @return
     */
    public double meanCost(List<Action> actions) {
        double cost = 0;
        for (Action action : actions) {
            cost += action.getCost();
        }
        return cost / actions.size();
    }
    
    /**
     * fonction qui calcule l'écart type du coup d'une liste d'actions
     * @param actions
     * @return
     */
    public double deviationCost(List<Action> actions) {
        double mean = meanCost(actions);
        double deviation = 0;
        for (Action action : actions) {
            deviation += Math.pow((action.getCost() - mean), 2);
        }
        deviation /= actions.size();
        deviation = Math.sqrt(deviation);
        return deviation;
    }
}
