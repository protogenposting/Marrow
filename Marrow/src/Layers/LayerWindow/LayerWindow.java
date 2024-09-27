package Layers.LayerWindow;

import Layers.Layer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LayerWindow extends JFrame {
    Layer parentLayer;

    public LayerWindow(String name, Layer parentLayer)
    {
        this.setTitle(name);
        this.setVisible(true);
        this.setSize(256,768);
        this.setResizable(true);
        this.parentLayer = parentLayer;
        this.setLayout(new FlowLayout());

        parentLayer.onAddChild = (a) -> {
            System.out.println("Added Child");
        };

        JPanel panel = new JPanel();

        panel.setPreferredSize(new Dimension(100,300));

        panel.setLayout(new FlowLayout(FlowLayout.CENTER,0,1));

        //region buttons!

        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));
        panel.add(new JButton("Add Layer"));


        //endregion

        JScrollPane scrollPane = new JScrollPane(panel);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.getContentPane().add(scrollPane);
    }
}
