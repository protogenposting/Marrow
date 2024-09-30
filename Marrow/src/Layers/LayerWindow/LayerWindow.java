package Layers.LayerWindow;

import Layers.BitmapLayer;
import Layers.Layer;
import Layers.ParentLayer;
import Tools.ToolContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class LayerWindow extends JFrame {
    Layer parentLayer;

    public LayerWindow(String name, ParentLayer parentLayer, ToolContainer toolContainer)
    {
        this.setTitle(name);
        this.setVisible(true);
        this.setSize(256,768);
        this.setResizable(true);
        this.parentLayer = parentLayer;
        this.setLayout(new FlowLayout());

        JPanel panel = new JPanel();

        //region buttons!

        JButton layerAdding = new JButton("Add Layer");

        layerAdding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentLayer.addChild(new BitmapLayer(toolContainer,"Big Gay Layer " + new Random().nextInt()));
                revalidate();
                repaint();
            }
        });

        add(layerAdding);

        //endregion

        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setViewportView(panel);

        scrollPane.setPreferredSize(new Dimension(200,300));

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        parentLayer.onAddChild = (a) -> {
            LayerButton layerButton = new LayerButton(a.name);

            layerButton.layer = a;

            layerButton.parentLayer = parentLayer;

            panel.add(layerButton);
        };

        this.getContentPane().add(scrollPane);
    }
}
