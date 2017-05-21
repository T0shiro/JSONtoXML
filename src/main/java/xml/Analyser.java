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
    
    public double mean(List<Action> actions) {
        double cost = 0;
        for (Action action : actions) {
            cost += action.getCost();
        }
        return cost / actions.size();
    }
    
    public double deviation(List<Action> actions) {
        double mean = mean(actions);
        double deviation = 0;
        for (Action action : actions) {
            deviation += Math.pow((action.getCost() - mean), 2);
        }
        deviation /= actions.size();
        deviation = Math.sqrt(deviation);
        return deviation;
    }
}
