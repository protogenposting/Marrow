package Layers.LayerWindow;

import Layers.ChildLayer;
import Layers.Layer;
import Layers.ParentLayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class LayerButton extends JButton
{
    public ChildLayer layer;

    public ParentLayer parentLayer;

    public LayerButton(String name)
    {
        setText(name);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                parentLayer.repaint();
                parentLayer.setChildTo(layer);
                System.out.println(layer.getWidth());
                System.out.println(layer.getHeight());
                repaint();
            }
        });
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
