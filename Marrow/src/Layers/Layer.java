package Layers;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * This is the abstract layer class. Never use it unless extending or using a layer list
 */
public class Layer extends JComponent {
    public ArrayList<ChildLayer> children = new ArrayList<>();

    public double x = 0;
    public double y = 0;

    public Consumer<Integer> onAddChild = (a) -> {};

    public ArrayList<ChildLayer> getChildren()
    {
        return children;
    }

    public void addChild(ChildLayer layer)
    {
        children.add(layer);
        onAddChild.accept(1);
    }
}
