package Layers;

import Animation.Keyframe;
import Animation.Transform2D;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * This is the abstract layer class. Never use it unless extending or using a layer list
 */
public class Layer extends JPanel {
    public ArrayList<ChildLayer> children = new ArrayList<>();

    public Transform2D transform = new Transform2D();

    AffineTransform currentAnimatedTransform = new AffineTransform();

    public Consumer<ChildLayer> onAddChild = (a) -> {};

    public ArrayList<ChildLayer> getChildren()
    {
        return children;
    }

    public ArrayList<ArrayList<Keyframe>> keyframes = new ArrayList<>();

    /**
     * this function adds a child
     * @param layer the layer to add as a child
     */
    public void addChild(ChildLayer layer)
    {
        //if we have no child then the current layer is the one we just added
        children.add(layer);

        //run the function that runs on child add (used for the layer organizer)
        onAddChild.accept(layer);

        layer.setOpaque(false);

        layer.parent = this;

        int defaultWidth = 400;

        int defaultHeight = 400;

        layer.transform.centerX = 200;

        layer.transform.centerY = 200;

        layer.setSize(defaultWidth,defaultHeight);

        layer.width = defaultWidth;

        layer.height = defaultHeight;

        if(layer instanceof BitmapLayer)
        {
            BitmapLayer currentLayer = (BitmapLayer) layer;
            currentLayer.bitmap.setSize(defaultWidth,defaultHeight);
        }


    }

}
