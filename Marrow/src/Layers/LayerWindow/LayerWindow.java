package Layers.LayerWindow;

import Layers.BitmapLayer;
import Layers.ChildLayer;
import Layers.Layer;
import Layers.ParentLayer;
import Tools.ToolContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class LayerWindow extends JPanel {
    Layer parentLayer;
    static JPanel innerPanel = new JPanel();

    public LayerWindow(ParentLayer parentLayer, ToolContainer toolContainer) {

        this.setVisible(true);
        this.setSize(256,768);
        this.parentLayer = parentLayer;
        this.setLayout(new FlowLayout());

        JPanel innerPanel = new JPanel();

        JButton layerAdding = getjButton(parentLayer, toolContainer);
        add(layerAdding);

        innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setViewportView(innerPanel);

        scrollPane.setPreferredSize(new Dimension(200,300));

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //region PARENT LAYER REFERENCE LAYER
        JButton parentLayerButton = new JButton("Parent Layer");

        parentLayerButton.addActionListener(e -> {
            parentLayer.currentLayer = null;
        });

        innerPanel.add(parentLayerButton);

        //end region



        this.add(scrollPane);
    }

    private JButton getjButton(ParentLayer parentLayer, ToolContainer toolContainer) {
        JButton layerAdding = new JButton("Add Layer");

        layerAdding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parentLayer.currentLayer != null) {
                    parentLayer.currentLayer.addChild(new BitmapLayer(toolContainer,  parentLayer.currentLayer.name + "'s Layer " +
                                        parentLayer.currentLayer.getChildren().size() + "                              ."
                                        + new Random().nextInt()));


                }else{
                    parentLayer.addChild(new BitmapLayer(toolContainer,  "Layer " +
                            parentLayer.getChildren().size() + "                              ."
                            + new Random().nextInt()));
                }

                ChildLayer newChild = parentLayer.getChildren().get(parentLayer.getChildren().size() - 1);

                LayerButton layerButton = new LayerButton(newChild.name);

                layerButton.layer = newChild;

                layerButton.parentLayer = parentLayer;

                innerPanel.add(layerButton);

                revalidate();
                repaint();
            }
        });
        return layerAdding;
    }


}
