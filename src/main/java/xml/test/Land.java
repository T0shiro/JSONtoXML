package xml.test;

/**
 * Created by Thomas on 20/05/2017.
 */
public class Land implements TEST {
    String creek;
    private int cost;
    
    @Override
    public int getCost() {
        return cost;
    }
    
    @Override
    public int getTime() {
        return 0;
    }
}
