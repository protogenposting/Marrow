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
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Consumer;

public class LayerWindow extends JPanel {
    Layer parentLayer;
    Timeline timeline;
    
    static JPanel innerPanel = new JPanel();
    static ArrayList <LayerButton> buttonLinkedList = new ArrayList<>();

    static boolean foundCurrentLayer;

    public LayerWindow(ParentLayer parentLayer, ToolContainer toolContainer, Timeline timeline) {
        this.timeline = timeline;
        this.setVisible(true);
        this.setSize(256,768);
        this.parentLayer = parentLayer;
        this.setLayout(new FlowLayout());



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

            timeline.addChannels();

            addChildrenToChildren(newChild, parentLayer, innerPanel, timeline);

            addToListAndResort(innerPanel, layerButton, parentLayer.currentLayer);
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

                    StringBuilder indentCount = new StringBuilder();

                    int depth = getIndentCount(parentLayer.currentLayer, parentLayer.getChildren(), 1);

                    for (int depthSeeker = 0; depthSeeker < depth; depthSeeker++){
                        indentCount.append("⤷ ");
                    }

                    parentLayer.currentLayer.addChild(new BitmapLayer(toolContainer, indentCount + "Layer "
                            + parentLayer.currentLayer.getChildren().size()));
                }


                revalidate();
                repaint();
            }
        });
        return layerAdding;
    }

    static int getIndentCount( ChildLayer currentLayer,
                                ArrayList<ChildLayer> childrenArray, int depth) {
    //return cases
        ChildLayer child;
        int returnDepth=-1;

        System.out.println("Size of Array: " + childrenArray.size());

        for (int childLocation = 0; childLocation < childrenArray.size(); childLocation++) {
            child = childrenArray.get(childLocation);
            System.out.println("Now Searching: " + childLocation);

            if (currentLayer == child ) {
              return depth;
            }
            if(child.getChildren().isEmpty()){
                returnDepth = -1;
            }
            //call function
            returnDepth = getIndentCount(currentLayer, child.getChildren(), depth+1);
            if(returnDepth!=-1){
                break;
            }
        }
        return returnDepth;
    }

    static void addChildrenToChildren(ChildLayer newChild, ParentLayer parentLayer, JPanel innerPanel,
                                     Timeline timeline){
        
       newChild.onAddChild = (newerChild) -> {
           LayerButton layerButton = new LayerButton(newerChild.name);

           layerButton.layer = newerChild;

           layerButton.parentLayer = parentLayer;

           innerPanel.add(layerButton);

           timeline.addChannels();

           addChildrenToChildren(newerChild, parentLayer, innerPanel, timeline);

           addToListAndResort(innerPanel, layerButton, parentLayer.currentLayer);
       };
   }

    public static void addToListAndResort(JPanel innerPannel, LayerButton newButton, ChildLayer currentLayer){

        boolean buttonAdded = false;

        if(buttonLinkedList.isEmpty()){
            buttonLinkedList.add(newButton);
            buttonAdded = true;
        }

        for (int i = 0; i < buttonLinkedList.size(); i++) {
            if(buttonLinkedList.get(i).layer==currentLayer){
                buttonLinkedList.add(countChildren(currentLayer.getChildren(), i),newButton);
            } else if (currentLayer == null && !buttonAdded) {
                buttonLinkedList.add(newButton);
                buttonAdded = true;
            }
        }

        for (int i = 0; i < buttonLinkedList.size(); i++) {
            innerPannel.add(buttonLinkedList.get(i));
            innerPannel.repaint();
            innerPannel.revalidate();
        }
    }

    /*  PL

        A0
        ⤷ B0
        ⤷ ⤷ C0
        ⤷ ⤷ ⤷ D0
        ⤷ ⤷ ⤷ D1
        ⤷ ⤷ ⤷ ⤷ E0
        ⤷ ⤷ ⤷ D2
        ⤷ ⤷ C1
        ⤷ B1
        A1
     */

    public static int countChildren(ArrayList<ChildLayer> childrenArray, int numberOfChildren){
        ChildLayer child;
        if (childrenArray.isEmpty()){
            return numberOfChildren;
        }
        numberOfChildren+= childrenArray.size();

        for(int chuldNum = 0; chuldNum < childrenArray.size(); chuldNum++) {
            child = childrenArray.get(chuldNum);

            numberOfChildren = countChildren(child.getChildren(), numberOfChildren);
        }

        return numberOfChildren;
    }


}
