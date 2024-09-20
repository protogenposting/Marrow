import Layers.*;
import Layers.LayerWindow.LayerWindow;
import Tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Main {

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

    public static void saveLayers(){



    }






    static void frameSetup(){
        Container content = frame.getContentPane();

        ToolContainer toolContainer = new ToolContainer();

        System.out.println(toolContainer.currentTool.toString());

        ArrayList<Layer> layers = new ArrayList<>();

        //set layout
        content.setLayout(new BorderLayout());

        //create draw area
        ParentLayer parentLayer = new ParentLayer(frame, toolContainer);

        //add the bitmap layer to the main window
        content.add(parentLayer, BorderLayout.CENTER);

        parentLayer.addChild(new BitmapLayer(toolContainer));

        parentLayer.setSize(1366,768);

        //controls, these will be used for buttons later
        JPanel controls = new JPanel();

        frame.setSize(1366,768);

        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //places window at the center of the screen

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });


        Toolbox tools = new Toolbox(toolContainer);
        LayerWindow layerOrganization = new LayerWindow("Marrow Layers",layers);
        //Timeline timeline = new Timeline("Marrow Timeline");
    }

}