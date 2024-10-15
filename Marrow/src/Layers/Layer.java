package Layers;

import Animation.Keyframe;
import Animation.Transform2D;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * This is the abstract layer class. Never use it unless extending or using a layer list
 */
public class Layer extends JPanel {
    public ArrayList<ChildLayer> children = new ArrayList<>();

    public Transform2D transform = new Transform2D();

    public Consumer<ChildLayer> onAddChild = (a) -> {};

    public ArrayList<ChildLayer> getChildren()
    {
        return children;
    }

    public ArrayList<Keyframe> keyframes = new ArrayList<>();

    public ArrayList<Boolean> channels = new ArrayList<>();

    public void addChild(ChildLayer layer)
    {
        children.add(layer);
        onAddChild.accept(layer);
    }


}
