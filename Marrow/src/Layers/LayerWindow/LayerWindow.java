package Layers.LayerWindow;

import Layers.BitmapLayer;
import Layers.ChildLayer;
import Layers.Layer;
import Layers.ParentLayer;
import Main.ImageConversions;
import Tools.ToolContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class LayerWindow extends JPanel {
    Layer parentLayer;
    static int childNum;
    boolean parentLayerSelected = true;

    public LayerWindow(ParentLayer parentLayer, ToolContainer toolContainer)
    {

        JPanel mainPanel = this;
        JPanel innerPanel = new JPanel();

        mainPanel.setVisible(true);
        mainPanel.setSize(256,768);
        this.parentLayer = parentLayer;
        mainPanel.setLayout(new FlowLayout());



        //region buttons!

        JButton layerAdding = new JButton("Add Layer");

        layerAdding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                childNum = parentLayer.currentLayer.children.size() + 1;

                if (parentLayer.currentLayer != (Layer)parentLayer){
                    parentLayerSelected = false;
                } else{
                    parentLayerSelected = true;
                }

                if (parentLayerSelected) {
                    parentLayer.addChild(new BitmapLayer(toolContainer, "Big Gay Layer " + new Random().nextInt()));
                }else {
                    parentLayer.addChild(new BitmapLayer(toolContainer, "Smol Gay Layer " + parentLayer.currentLayer + childNum));
                }

                revalidate();
                repaint();

            }
        });

        add(layerAdding);

        //endregion

        innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setViewportView(innerPanel);

        scrollPane.setPreferredSize(new Dimension(200,300));

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        parentLayer.onAddChild = (a) -> {
            LayerButton layerButton = new LayerButton(a.name);

            layerButton.layer = a;

            layerButton.parentLayer = parentLayer;

            innerPanel.add(layerButton);
        };

        mainPanel.add(scrollPane);
    }

}
