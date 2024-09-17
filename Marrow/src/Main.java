import Layers.*;
import Tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    //public Tool currentTool = new Tool();

    //the main frame we will be drawing on
    static JFrame frame = new JFrame("Marrow");

    public static void main(String[] args) {

        frameSetup();

        //update loop
        while(true)
        {
            //move these to a key pressed event
            //ImageConversions.SaveImage(bitmapLayer.getImage());
            //System.out.println("saved");
        }
    }

    static void frameSetup(){
        Container content = frame.getContentPane();

        ArrayList<Layer> layers = new ArrayList<>();

        Toolbox tools = new Toolbox();
        LayerWindow layerOrganization = new LayerWindow("Marrow Layers",layers);
        Timeline timeline = new Timeline("Marrow Timeline");

        //set layout
        content.setLayout(new BorderLayout());

        //create draw area
        ParentLayer parentLayer = new ParentLayer(frame);

        //add the bitmap layer to the main window
        content.add(parentLayer, BorderLayout.CENTER);

        parentLayer.addChild(new BitmapLayer());

        parentLayer.setSize(1366,768);

        //controls, these will be used for buttons later
        JPanel controls = new JPanel();

        frame.setSize(1366,768);

        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //places window at the center of the screen

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}