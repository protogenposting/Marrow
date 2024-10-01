package Layers;

public class ChildLayer extends Layer{
    public String name = "bob undertale";
    public Layer parent;
    public boolean isCurrentLayer = false;

    /**
     * checks if this child layer has a name
     * @return true if it has a name, false if not
     */
    public boolean hasName(){
        return this.name != null;
    }

    /**
     * gets name of child layer
     * @return the name of the child layer
     */
    public String getName(){
        return this.name;
    }
}
