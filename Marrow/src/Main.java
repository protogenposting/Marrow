import Bitmaps.Pixel;
import Bitmaps.RGBColor;
import Layers.*;
import Layers.LayerWindow.LayerWindow;
import Tools.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

     /**
     * Sets up the main frame that the user draws on
     */
    static void frameSetup(){

        //region SPLIT PANE DEBUG
        colorWheelDEBUG.add(colourButton);
        drawScreenDEBUG.add(drawButton);
      //  toolBarDEBUG.add();
        timeLineDEBUG.add(timeButton);
        childLayerDEBUG.add(childButton);

        colorWheelDEBUG.setVisible(true);
        drawScreenDEBUG.setVisible(true);
        toolBarDEBUG.setVisible(true);
        timeLineDEBUG.setVisible(true);
        childLayerDEBUG.setVisible(true);

        colourButton.addActionListener(e -> {
            System.out.println("colourButton pressed");
        });
        drawButton.addActionListener(e -> {
            System.out.println("drawButton pressed");
        });
        toolButton.addActionListener(e -> {
            System.out.println("toolButton pressed");
        });
        timeButton.addActionListener(e -> {
            System.out.println("timeButton pressed");
        });
        childButton.addActionListener(e -> {
            System.out.println("childButton pressed");
        });

        //endregion

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

        frame.setJMenuBar(createMenuBar());
        //frame.setLocationByPlatform(true);

    }

    static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        return menuBar;
    }

    static JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        fileMenu.add(newItem);
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
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
