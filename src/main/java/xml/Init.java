package xml;

import java.util.Map;

/**
 * Created by user on 16/05/2017.
 */
public class Init implements MyNode {
    private String name;
    public Init(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
