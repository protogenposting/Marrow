package Layers;

import javax.swing.*;
import java.util.LinkedList;

/**
 * This is the abstract layer class. Never use it unless extending or using a layer list
 */
public class Layer extends JComponent {
    private LinkedList<Layer> children = new LinkedList<>();

    public LinkedList<Layer> getChildren()
    {
        return children;
    }

    public void addChild(Layer layer)
    {
        children.add(layer);
    }
}
