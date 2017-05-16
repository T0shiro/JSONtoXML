package xml;

import java.util.Map;

/**
 * Created by user on 16/05/2017.
 */
public class Action implements MyNode {
    private String name;
    private Map<String, Object> extras;
    private Map<String, Object> parameters;

    public Action(String name, Map<String, Object> extras, Map<String, Object> parameters) {
        this.name = name;
        this.extras = extras;
        this.parameters = parameters;
    }

    @Override
    public String getName() {
        return name;
    }
}
