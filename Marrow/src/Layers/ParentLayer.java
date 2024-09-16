package Layers;

import javax.swing.*;

/**
 * This layer should always be on top of the hierarchy. It contains all other layers and draws them on a canvas. It should be responsible for drawing everything.
 */
public class ParentLayer extends Layer {
    JFrame frame;
    public ParentLayer(JFrame frame)
    {
        this.frame = frame;
    }
    @Override
    public void addChild(Layer layer)
    {
        children.add(layer);
        frame.getContentPane().add(layer);
    }
}
