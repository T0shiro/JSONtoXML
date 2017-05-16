package xml;

import java.util.Map;

/**
 * Created by user on 16/05/2017.
 */
public class Action implements MyNode {
    public Map<String, Object> extras;
    public Map<String, Object> parameters;
    private String name;
    private int cost;

    public Action(Map<String, Object> extras, Map<String, Object> parameters, String name, int cost) {
        this.extras = extras;
        this.parameters = parameters;
        this.name = name;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String getName() {
        return name;
    }
}
