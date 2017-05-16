package xml;

import java.util.Map;

/**
 * Created by user on 16/05/2017.
 */
public class Action implements MyNode {
    public String name;
    private int cost;
    public Map<String, Object> extras;
    public Map<String, Object> parameters;

    public Action(String name, int cost, Map<String, Object> extras, Map<String, Object> parameters) {
        this.name = name;
        this.cost = cost;
        this.extras = extras;
        this.parameters = parameters;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }
}
