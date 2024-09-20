import Layers.*;
import Layers.LayerWindow.LayerWindow;
import Tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import java.io.*;

public class Main {

    //the main frame we will be drawing on
    static JFrame frame = new JFrame("Marrow");

    public static void main(String[] args) {
        frameSetup();
    }

    public static void saveLayers(ParentLayer parentLayer){
        try {
            String currentSaveDirectory = "MarrowSaves"; // change later on to be able to find the directory user saved it at
            File path = new File(currentSaveDirectory);
            path.mkdirs();
            File saveFile = new File(currentSaveDirectory + "/save.marrow");
            saveFile.createNewFile();

            FileWriter writer = new FileWriter(currentSaveDirectory + "save.marrow");

            









        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

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

        parentLayer.addChild(new BitmapLayer(toolContainer,"pussy"));

        parentLayer.setSize(1366,768);

        //controls, these will be used for buttons later
        JPanel controls = new JPanel();

        frame.setSize(1366,768);

        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //places window at the center of the screen

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // this allows it to save the stuff when user exits
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                saveLayers(parentLayer);

                frame.dispose();
                System.exit(0);

            }
        });

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_S){
                    saveLayers(parentLayer);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        Toolbox tools = new Toolbox(toolContainer);
        LayerWindow layerOrganization = new LayerWindow("Marrow Layers",layers);
        //Timeline timeline = new Timeline("Marrow Timeline");
    }

}