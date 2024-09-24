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
    static boolean test = false;
    //the main frame we will be drawing on
    static JFrame frame = new JFrame("Marrow");

    public static void main(String[] args) {

        if(test){
            ToolContainer toolContainer = new ToolContainer();

            ParentLayer parentLayer = new ParentLayer(frame, toolContainer);
            ChildLayer childLayer2 = new ChildLayer();

            childLayer2.addChild(new ChildLayer());
            childLayer2.addChild(new ChildLayer());
            childLayer2.addChild(new ChildLayer());

            parentLayer.addChild(new ChildLayer());
            parentLayer.addChild(childLayer2);

            saveLayers(parentLayer);

        }else {
            frameSetup();
        }
    }

    public static void saveLayers(ParentLayer parentLayer){
        try {
            String currentSaveDirectory = "MarrowSaves"; // change later on to be able to find the directory user saved it at
            File path = new File(currentSaveDirectory);
            path.mkdirs();
            File saveFile = new File(currentSaveDirectory + "/save.marrow");
            saveFile.createNewFile();

            FileWriter writer = new FileWriter(currentSaveDirectory + "save.marrow");

            /*
            example of what it should look like
            parentLayer
            -childLayer1
            --childLayer1~1  the reason it ends with ~1 is so computer doesn't get confused reading the same names
            --childLayer1~2
            --childLayer1~3
            ---childLayer1~3~1
            -childLayer2
            --childLayer2~1
             */

            ArrayList<ChildLayer> childLayers = parentLayer.getChildren();

            writer.write("MARROW\n\nParentLayer");
            System.out.println("MARROW\n\nParentLayer");

            findChildrenInChildLayer(childLayers, 1, writer, false, "ChildLayer");


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    static boolean isThereChildrenInChildLayer(ChildLayer childLayer){
        ArrayList<ChildLayer> childLayers = childLayer.getChildren();
        return !childLayers.isEmpty();
    }

    public static void findChildrenInChildLayer(ArrayList<ChildLayer> childLayers, int dashCount, FileWriter writer,
                                                boolean hasRepeated, String childLayerName){
        boolean thereIsChild;

        for (int i = 0; i < childLayers.size(); i++) {
            thereIsChild = isThereChildrenInChildLayer(childLayers.get(i));

            if(!hasRepeated) {
                try {
                    writer.write("\n");
                    printDashes(dashCount, writer);
                    writer.write(childLayerName + (i + 1));

                    System.out.print("\n");
                    printDashes(dashCount, writer);
                    System.out.println(childLayerName + (i + 1));
                } catch (IOException ignore) {}
            }
            else{
                try {
                    writer.write("\n");
                    printDashes(dashCount, writer);
                    writer.write(childLayerName);

                    System.out.print("\n");
                    printDashes(dashCount, writer);
                    writer.write(childLayerName);
                } catch (IOException ignore) {}
            }

            if(thereIsChild){
                ArrayList<ChildLayer> secondChildLayers = childLayers.get(i).getChildren();

                for (int j = 0; j < secondChildLayers.size(); j++) {
                    findChildrenInChildLayer(secondChildLayers, dashCount + 1, writer,
                            true, childLayerName + (i + 1) + "~" + (j + 1));
                }

            }

            /* for int childNum; childExists; childExists=true; childNum++

                    when writing add ~ + childNum

                    if dashcount = same , add ~1
             */
        }
    }

    public static void printDashes(int dashCount, FileWriter writer) throws IOException {
        for (int i = 0; i < dashCount; i++) {
            writer.write("-");
            System.out.print("-");
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