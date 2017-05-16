package xml;

import java.util.Map;

/**
 * Created by user on 16/05/2017.
 */
public class SubNode implements MyNode {
    private String name;
    private Map<String, Object> content;

    public SubNode(String name, Map<String, Object> content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String getName() {
        return null;
    }
}
