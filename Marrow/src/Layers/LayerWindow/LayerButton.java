package Layers.LayerWindow;

import Layers.ChildLayer;
import Layers.Layer;
import Layers.ParentLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LayerButton extends JButton
{
    public ChildLayer layer;

    public ParentLayer parentLayer;

    //public

    public LayerButton(String name)
    {
        setText(name);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentLayer.repaint();
                parentLayer.setChildTo(layer);
                repaint();
            }
        });
    }

    @Override
    public void paintComponents(Graphics g) {
        setSelected(parentLayer.currentLayer == layer);
        super.paintComponents(g);
    }
}
