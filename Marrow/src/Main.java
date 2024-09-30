import Bitmaps.Bitmap;
import Bitmaps.Pixel;
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

    public static String currentSaveDirectory = "MarrowSaves/Test Project"; // change later on to be able to find the directory user saved it at

    public static void main(String[] args) {
        frameSetup();
    }

    /**
     * saves the parentLayer and its children into a custom text file named "save.marrow"
     * @param parentLayer the layer and its children being saved
     */
    public static void saveLayers(ParentLayer parentLayer){
        try {

            FileWriter writer = getSaveFileWriter();

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
            System.out.print("\nMARROW\n\nParentLayer");

            saveChildrenInChildLayer(
                    childLayers,
                    "-",
                    writer,
                    false,
                    "ChildLayer"
            );

            writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**
     * gets a FileWriter that will write to a save file for the computer to write to
     * @return the FileWriter that will write to a save file
     * @throws IOException for the case a file does not exist
     */
    private static FileWriter getSaveFileWriter() throws IOException {
        //NOTE: IF IT DOESN'T WORK OR IS UNABLE TO FIND A FILE, CHECK FOR TYPO

        File path = new File(currentSaveDirectory);
        boolean pathExists = path.mkdirs();

        File saveFile = new File(currentSaveDirectory + "/save.marrow");
        boolean saveExists = saveFile.createNewFile();

        // Currently, this will simply overwrite the current save file. Find way to resolve issue, maybe?

        return new FileWriter(currentSaveDirectory + "/save.marrow");
    }

    /**
     * checks if there are children in childLayer
     * @param childLayer the childLayer being checked for children
     * @return true if there are, false if empty
     */
    private static boolean isThereChildrenInChildLayer(ChildLayer childLayer){
        ArrayList<ChildLayer> childLayers = childLayer.getChildren();
        return !childLayers.isEmpty();
    }

    /**
     * finds children in childLayer and prints them accordingly in a specific format into a custom save file
     * @param childLayers the childLayers being saved
     * @param dashCount the "hierarchy" of the childLayer
     * @param writer the file writer that saves the childLayers into the save file
     * @param hasRepeated checks for if it has repeated at least once
     * @param childLayerName the name being saved to the file for each childLayer that exists
     */
    public static void saveChildrenInChildLayer(ArrayList<ChildLayer> childLayers, String dashCount, FileWriter writer,
                                                boolean hasRepeated, String childLayerName){
        boolean thereIsChild;
		
        for (int i = 0; i < childLayers.size(); i++) {
            thereIsChild = isThereChildrenInChildLayer(childLayers.get(i));

            try {
                writer.write("\n" + dashCount + childLayerName);
                System.out.print("\n" + dashCount + childLayerName);

                if(!hasRepeated){ //if it has repeated, it'll already have a number at the front
                    writer.write(i + 1);
                    System.out.print(i + 1);
                }
            }
            catch (IOException ignore) {}

            if(thereIsChild){
                ArrayList<ChildLayer> secondChildLayers = childLayers.get(i).getChildren(); //get the children of the child in childLayers

                for (int j = 0; j < secondChildLayers.size(); j++) {
                    if(!hasRepeated) { //needed so childLayerName prints "ChildLayer1~1" instead of "ChildLayer~1", for example
                        saveChildrenInChildLayer(secondChildLayers, dashCount + "-", writer,
                                true, childLayerName + (i + 1) + "~" + (j + 1));
                    }
                    else{
                        saveChildrenInChildLayer(secondChildLayers, dashCount + "-", writer,
                                true, childLayerName + "~" + (j + 1));
                    }
                }
            } // if this is not here, it will print duplicate layers
            if(hasRepeated){
                break;
            }
        }
    }

     /**
     * Sets up the main frame that the user draws on
     */
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

        /*
        end result should be:
        ParentLayer
        -CL1
        -CL2
        --CL2~1
        ---CL2~1~1
        ---CL2~1~2
        ---CL2~1~3
        ---CL2~1~4
        ----CL2~1~4~1
        -CL3
        --CL3~1
         */
        //endregion //

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
                    //saveLayers(parentLayer);
                    BitmapLayer layer = (BitmapLayer) parentLayer.getChildren().get(1);
                    ArrayList<ArrayList<Pixel>> bitmap = layer.bitmap.bitmap;
                    for(int x = 0; x < bitmap.size(); x++)
                    {
                        for(int y = 0; y < bitmap.get(x).size(); y++)
                        {
                            if(bitmap.get(x).get(y).alpha!=0) {
                                System.out.println(bitmap.get(x).get(y).alpha);
                            }
                        }
                    }
                    bitmap = bitmap;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        Toolbox tools = new Toolbox(toolContainer);
        LayerWindow layerOrganization = new LayerWindow("Marrow Layers",parentLayer,toolContainer);
        Timeline timeline = new Timeline("Marrow Timeline");
    }

}
