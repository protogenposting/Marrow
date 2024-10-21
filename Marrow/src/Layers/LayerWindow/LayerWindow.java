package Layers.LayerWindow;

import Animation.Timeline;
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
    Timeline timeline;

    public LayerWindow(ParentLayer parentLayer, ToolContainer toolContainer, Timeline timeline) {
        this.timeline = timeline;
        this.setVisible(true);
        this.setSize(256,768);
        this.parentLayer = parentLayer;
        this.setLayout(new FlowLayout());

        JPanel innerPanel = new JPanel();

        JButton layerAdding = getjButton(parentLayer, toolContainer);
        add(layerAdding);

        JButton parentLayerButton = new JButton("Parent Layer");
        parentLayerButton.addActionListener(e -> {parentLayer.currentLayer = null;});
        innerPanel.add(parentLayerButton);

        innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setViewportView(innerPanel);

        scrollPane.setPreferredSize(new Dimension(200,300));

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        parentLayer.onAddChild = (newChild) -> {
            LayerButton layerButton = new LayerButton(newChild.name);

            layerButton.layer = newChild;

            layerButton.parentLayer = parentLayer;

            innerPanel.add(layerButton);

            timeline.addKeyframes();

            addChildrenToChildren(newChild, parentLayer, innerPanel, timeline);
        };



        this.add(scrollPane);
    }

    private JButton getjButton(ParentLayer parentLayer, ToolContainer toolContainer) {
        JButton layerAdding = new JButton("Add Layer");

        layerAdding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (parentLayer.currentLayer == null) {
                    parentLayer.addChild(new BitmapLayer(toolContainer, "Layer " + parentLayer.getChildren().size()));
                }else {
                    parentLayer.currentLayer.addChild(new BitmapLayer(toolContainer, parentLayer.currentLayer.name
                                                + "'s Layer" + parentLayer.currentLayer.getChildren().size()));
                }


                revalidate();
                repaint();
            }
        });
        return layerAdding;
    }

   static void addChildrenToChildren(ChildLayer newChild, ParentLayer parentLayer, JPanel innerPanel,
                                     Timeline timeline){
        
       newChild.onAddChild = (newerChild) -> {
           LayerButton layerButton = new LayerButton(newerChild.name);

           layerButton.layer = newerChild;

           layerButton.parentLayer = parentLayer;

           innerPanel.add(layerButton);

           timeline.addKeyframes();
       };
   }


}
