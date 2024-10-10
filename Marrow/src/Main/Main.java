package Main;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;
import Layers.*;
import Layers.LayerWindow.LayerWindow;
import Tools.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    //the main frame we will be drawing on
    public static JFrame frame = new JFrame("Marrow");

    static JSplitPane mainSP = new JSplitPane();
    static JSplitPane topScreenSP = new JSplitPane();
    static JSplitPane bottomScreenSPVert = new JSplitPane();
    static JSplitPane bottomScreenSPHor = new JSplitPane();

    //region SPLIT PANE DEBUGGING
    static JPanel colorWheelDEBUG = new JPanel();
    static JPanel drawScreenDEBUG = new JPanel();
    static JPanel toolBarDEBUG = new JPanel();
    static JPanel timeLineDEBUG = new JPanel();
    static JPanel childLayerDEBUG = new JPanel();
    static JButton colourButton = new JButton("Color wheel");
    static JButton toolButton = new JButton("Tool Bar");
    static JButton drawButton = new JButton("Draw Screen");
    static JButton timeButton = new JButton("Time Line");
    static JButton childButton = new JButton("Child Layer");
    //endregion

    public static String currentSaveDirectory = "MarrowSaves"; // change later on to be able to find the directory user saved it at

    public static void main(String[] args) {
        frameSetup();
    }

    //region SAVE FUNCTIONS
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
        //NOTE: IF IT DOESN'T WORK OR IS UNABLE TO FIND A FILE, CHECK FOR TYPOS

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
    public static void saveChildrenInChildLayer(ArrayList<ChildLayer> childLayers, String dashCount,
                                                FileWriter writer, boolean hasRepeated,
                                                String childLayerName){
        boolean thereIsChild;
		
        for (int i = 0; i < childLayers.size(); i++) {
            thereIsChild = isThereChildrenInChildLayer(childLayers.get(i));

            try {
                ChildLayer child = childLayers.get(i);
                childLayerName = child.name;
                writer.write("\n" + dashCount + childLayerName);
                System.out.print("\n" + dashCount + childLayerName);

                //SAVE THE IMAGE!!!
                if(child.getClass().equals(BitmapLayer.class))
                {
                    BitmapLayer bitmapLayer = (BitmapLayer) child;

                    ImageConversions.SaveImage(bitmapLayer.drawnImage,currentSaveDirectory+"/"+childLayerName+".png");
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
    //endregion

    //region LOAD FUNCTIONS
    public ArrayList<BitmapLayer> loadLayers(ToolContainer toolContainer) throws IOException {
        File saveFile = new File(currentSaveDirectory + "/save.marrow");
        Scanner fileReader = new Scanner(saveFile);

        LinkedList<String> layerNames = new LinkedList<>();
        ArrayList<BitmapLayer> bitmapLayers = new ArrayList<>();

        while(fileReader.hasNextLine()){
            String layerName = fileReader.nextLine();

            if( layerName.isEmpty() ) { continue; }

            if( layerName.charAt(0) == '-') {
                layerName = removeDashes(layerName);
            }

            layerNames.add(layerName);
        }

        for (int i = 0; i < layerNames.size(); i++) {
            if(i < 2){
                continue;
            }

            String layerName = layerNames.get(i);

            String filePath = currentSaveDirectory + "/" + layerName + ".png";
            File layer = new File(filePath);

            if (layer.createNewFile()) {
                layer.delete();
                continue; //if the layer doesn't exist, can't load it
            }

            BufferedImage image = ImageIO.read(layer);
            Bitmap imageToBitmap = new Bitmap(image);

            BitmapLayer bitmapLayer = new BitmapLayer(toolContainer, layerName, imageToBitmap);
            bitmapLayers.add(bitmapLayer);
        }

        return bitmapLayers;
    }

    /**
     * removes the dashes at the back of a layer name
     * @param layerName the name having its dashes removed
     * @return the layer name with the removed dashes
     */
    private String removeDashes(String layerName){
        StringBuilder returningName = new StringBuilder();
        boolean firstDashesPassed = false;

        for (int i = 0; i < layerName.length(); i++) {

            if(!(layerName.charAt(i) == '-') || firstDashesPassed){
                returningName.append(layerName.charAt(i));
                firstDashesPassed = true; //don't need to care about dashes in the middle of a name
            }

        }

        return returningName.toString();
    }
    //endregion
     /**
     * Sets up the main frame that the user draws on
     */
    static void frameSetup(){

        frame.getContentPane().add(mainSP);

        ToolContainer toolContainer = new ToolContainer();
        ParentLayer parentLayer = new ParentLayer(toolContainer);

        parentLayer.setSize(800,400);
        parentLayer.setVisible(true);

        System.out.println(toolContainer.currentTool.toString());

        Toolbox tools = new Toolbox(toolContainer);

        LayerWindow layerOrganization = new LayerWindow(parentLayer,toolContainer);

        Timeline timeline = new Timeline("Marrow Timeline");

        JColorChooser colorChooser = new JColorChooser();

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Color currentColor = colorChooser.getColor();
                toolContainer.currentColor = new RGBColor(
                        currentColor.getRed(),
                        currentColor.getGreen(),
                        currentColor.getBlue(),
                        255);
                toolContainer.currentTool.currentColor = toolContainer.currentColor;
            }
        });

        JFrame colorFrame = new JFrame();

        colorFrame.setSize(500,384);
        colorFrame.add(colorChooser);
        colorFrame.setVisible(true);

        frame.setSize(1366,768);

        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //places window at the center of the screen

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // this allows it to save the stuff when user exits
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

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
                    for (ArrayList<Pixel> x : bitmap) {

                        for (Pixel y : x) {
                            if (y.alpha != 0) {
                                System.out.println(y.alpha);
                            }
                        }
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                int frameWidth = frame.getWidth();
                int frameHeight = frame.getHeight();
                // note: don't use getX and getY. those return the origin (as in, the center position of the frame
                // that barely changes whenever you resize it). these two methods are much better suited

                int childLayerWidth = 260;
                int frameDefaultHeight = 768;
                int drawScreenDefaultHeight = 450;

                // added these variables since the equivalent of these were being assigned everywhere
                int newWidth = frameWidth - childLayerWidth;
                int newHeight = frameHeight - frameDefaultHeight + drawScreenDefaultHeight;

                parentLayer.setSize(newWidth, newHeight);

                ArrayList<ChildLayer> childrenArray = parentLayer.getChildren();

                /*

                    for(child; child<array; child++){

                    layer = (Bitmap) array.getChild

                    newSize = (Dimension) (frameHeight - (toolBox.getY + timeline.getY)),
                                            (frameWidth- childLayer.getX)
                    

                    layer.setSize(newSize)
                    layer.Bitmap.setSize(newSize)

                    }

                 */

                for (ChildLayer childLayer : childrenArray) {

                    System.out.println("setting size");
                    BitmapLayer layer = (BitmapLayer) childLayer;

                    layer.setSize(newWidth, newHeight);

                    layer.bitmap.setSize(newWidth, newHeight);
                }

                parentLayer.revalidate();
                parentLayer.repaint();

                System.out.println((newHeight) + " Height by " + (newWidth) + " Width");
            }
        });

        //region Split Pane Settup
        mainSP.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainSP.setDividerLocation(75);
        mainSP.setTopComponent(tools); //TOOL BAR HERE
        mainSP.setBottomComponent(bottomScreenSPVert);

        bottomScreenSPVert.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        bottomScreenSPVert.setDividerLocation(260);
        bottomScreenSPVert.setLeftComponent(layerOrganization); //CHILD LAYER HERE
        bottomScreenSPVert.setRightComponent(bottomScreenSPHor);

        bottomScreenSPHor.setOrientation(JSplitPane.VERTICAL_SPLIT);
        bottomScreenSPHor.setDividerLocation(450);
        bottomScreenSPHor.setTopComponent(parentLayer); // DRAW SCREEN HERE
        bottomScreenSPHor.setBottomComponent(timeline); // TIMELINE HERE

        //endregion

        frame.setJMenuBar(createMenuBar(parentLayer));
        //frame.setLocationByPlatform(true);

    }

    static JMenuBar createMenuBar(ParentLayer parentLayer) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu(parentLayer));
        menuBar.add(createEditMenu());
        return menuBar;
    }

    static JMenu createFileMenu(ParentLayer parentLayer) {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        fileMenu.add(newItem);
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            saveLayers(parentLayer);
        });
        fileMenu.add(saveItem);
        return fileMenu;
    }

    static JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        editMenu.add(cutItem);
        JMenuItem copyItem = new JMenuItem("Copy");
        editMenu.add(copyItem);
        JMenuItem pasteItem = new JMenuItem("Paste");
        editMenu.add(pasteItem);
        return editMenu;
    }
}
