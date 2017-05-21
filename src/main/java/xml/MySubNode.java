package xml;

import java.util.Map;

/**
 * Map representant un sous objet d'une action par exemple une ressource
 * qui contiendrais plusieurs sous informations
 */
public class MySubNode implements MyNode {
    private String name;
    /**
     * "Contenu" du SousObjet cette map contient l'id d'un objet contenu ainsi que sa valeur
     */
    public Map<String, Object> content;
    
    public void setName(String name) {
        this.name = name;
    }
    
    public MySubNode(String name, Map<String, Object> content) {
        this.name = name;
        this.content = content;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    @Override
    public String getName() {
        return name;
    }
}
