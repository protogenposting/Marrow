package Layers.LayerWindow;

import Animation.Timeline;
import Layers.BitmapLayer;
import Layers.ChildLayer;
import Layers.Layer;
import Layers.ParentLayer;
import Tools.ToolContainer;
import org.w3c.dom.ls.LSOutput;

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


        /*
            When a new child gets created under the parentLayer, it creates a button affiliated with that child and
            calls the function addChildrenToChildren which applies the same code to the child's onAddChildren
            afterwards calls the function addToListAndResort which sorts the buttons by layer & layerChildren
         */
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


    /** CALLED WHEN SETTING UP WINDOW
     * function that creates a button that adds children to either the parentLayer or the currently selected
     * layer. Calls getIndentCount()
     * @param parentLayer the layer that holds all layers
     * @param toolContainer the object that determines what drawing tool is used
     * @return the newly created button
     */
    private JButton getjButton(ParentLayer parentLayer, ToolContainer toolContainer) {
        JButton layerAdding = new JButton("Add Layer");

        layerAdding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (parentLayer.currentLayer == null) {
                    parentLayer.addChild(new BitmapLayer(toolContainer, "Layer " + parentLayer.getChildren().size()),false);
                }else {

                    StringBuilder indentCount = new StringBuilder();

                    int depth = getIndentCount(parentLayer.currentLayer, parentLayer.getChildren(), 1);

                    for (int depthSeeker = 0; depthSeeker < depth; depthSeeker++){
                        indentCount.append("⤷ ");
                    }

                    parentLayer.currentLayer.addChild(new BitmapLayer(toolContainer, indentCount + "Layer "
                            + parentLayer.currentLayer.getChildren().size()),false);
                }


                revalidate();
                repaint();
            }
        });
        return layerAdding;
    }

    /** RECURSIVE FUNCTION && CALLED WHEN CREATING THE NAME FOR A NEW LAYER THAT IS A CHILD OF ANOTHER LAYER
     * Searches through the parent layer's children until it finds the depth that the current layer is on and returns
     * currentLayer's depth +1
     *
     * @param currentLayer the currently selected layer
     * @param childrenArray the children array that is scanned for finding the depth of the newChild
     * @param depth the amount of layers between the parentLayer & the currentLayer
     * @return depth // return depth
     */
    static int getIndentCount( ChildLayer currentLayer, ArrayList<ChildLayer> childrenArray, int depth) {
    //return cases
        ChildLayer child;
        int returnDepth=-1;

        //System.out.println("Size of Array: " + childrenArray.size());

        for (int childLocation = 0; childLocation < childrenArray.size(); childLocation++) {
            child = childrenArray.get(childLocation);
            //System.out.println("Now Searching: " + childLocation);

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

    /** RECURSIVE FUNCTION && CALLED WHEN A LAYER IS CREATED
     * This function adds the method required for a new layer to add more new layers to itself,
     * reflects this code onto newly created children
     *
     * @param newChild the newly created child
     * @param parentLayer the layer that holds all children & their children
     * @param innerPanel the panel that holds the buttons
     * @param timeline the timeline that holds the keyFrames for the new layer
     */
    private void addChildrenToChildren(ChildLayer newChild, ParentLayer parentLayer, JPanel innerPanel,
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

    /** CALLED WHEN A NEW LAYER IS CREATED
     * Searches for the parent of the newly created button,
     * adds the new button immediately after the current layer's location or after the last child of the current layer
     * afterward reorders the buttons currently displayed on screen to reflect their parent layers
     *
     * calls countChildren() passes the currentLayer's childrenArray & the current count of layers
     *
     * @param innerPanel panel to add the button to
     * @param newButton the newly created button
     * @param currentLayer the layer that is the parent of the newly created button's layer
     */
    public static void addToListAndResort(JPanel innerPanel, LayerButton newButton, ChildLayer currentLayer){

        boolean buttonAdded = false;

        if(buttonLinkedList.isEmpty()){
            buttonLinkedList.add(newButton);
            buttonAdded = true;
        }

        for (int listIndex = 0; listIndex < buttonLinkedList.size(); listIndex++) {
            if(buttonLinkedList.get(listIndex).layer==currentLayer){
                buttonLinkedList.add(countChildren(currentLayer.getChildren(), listIndex),newButton);
            } else if (currentLayer == null && !buttonAdded) {
                buttonLinkedList.add(newButton);
                buttonAdded = true;
            }
        }

        for (int listIndex = 0; listIndex < buttonLinkedList.size(); listIndex++) {
            innerPanel.add(buttonLinkedList.get(listIndex));
            innerPanel.repaint();
            innerPanel.revalidate();
        }
    }

    /** CALLED BY addToListAndResort()
     * counts all children inside the parents childrenArray && the children in each child's childrenArray
     *
     * @param childrenArray the array of layers that belongs to a pre-counted parent layer
     * @param numberOfChildren the total count for number of children
     * @return numberOfChildren
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
