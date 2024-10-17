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

        parentLayer.onAddChild = (newChildLayer) -> {
            LayerButton layerButton = new LayerButton(newChildLayer.name);

            layerButton.layer = newChildLayer;

            layerButton.parentLayer = parentLayer;

            innerPanel.add(layerButton);

            newChildLayer.onAddChild = (newChild) -> {
                onAddChildrenFunc(parentLayer);
            };
        };

        this.add(scrollPane);
    }

    private JButton getjButton(ParentLayer parentLayer, ToolContainer toolContainer) {
        JButton layerAdding = new JButton("Add Layer");

        layerAdding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String newChild;

                if (parentLayer.currentLayer != null) {

                    newChild =  parentLayer.currentLayer.name + "'s Layer " +
                            parentLayer.currentLayer.getChildren().size();

                    parentLayer.currentLayer.addChild(new BitmapLayer(toolContainer, newChild, parentLayer));


                }else{

                    newChild = "Layer " + parentLayer.getChildren().size();

                    parentLayer.addChild(new BitmapLayer(toolContainer,  newChild, parentLayer));
                }


                revalidate();
                repaint();
            }
        });
        return layerAdding;
    }

    private void onAddChildrenFunc(ParentLayer parentLayer){
        int index = parentLayer.currentLayer.getChildren().size() - 1;

        ChildLayer newChild = parentLayer.currentLayer.getChildren().get(index);

        LayerButton layerButton = new LayerButton(newChild.name);

        layerButton.layer = newChild;

        layerButton.parentLayer = parentLayer;

        innerPanel.add(layerButton);

        newChild.onAddChild = (newChildLayer) -> {
            onAddChildrenFunc(parentLayer);
        };
    }

}
