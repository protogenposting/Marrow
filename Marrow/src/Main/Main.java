package Main;

//region imports
import Animation.*;
import Bitmaps.Bitmap;
import Animation.Timeline;
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
//endregion

public class Main {

    //region the main frame everything will be put on

    public static JFrame frame = new JFrame("Marrow");

    //these are all split panes, which can be resized
    static JSplitPane mainSP = new JSplitPane();
    static JSplitPane topScreenSP = new JSplitPane();
    static JSplitPane bottomScreenSPVert = new JSplitPane();
    static JSplitPane bottomScreenSPHor = new JSplitPane();

    public static String currentSaveDirectory;
    public static String currentLoadDirectory;
    public static boolean hasSavedOrLoaded = false;
    public static boolean stopSavingOrLoading = false; // stops the saving or loading if user changes their mind when choosing files

    public static Object[] yesNoOptions = {"Yes", "No"};

    public static void main(String[] args) {
        frameSetup();
    }

    //region SAVE FUNCTIONS
    /**
     * saves the parentLayer and its children into a custom text file named "save.marrow"
     * @param parentLayer the layer and its children being saved
     */
    public static void saveLayers(ParentLayer parentLayer, AnimationDataStorage animDataStorage){
        try {
            if(!hasSavedOrLoaded) {
                setSaveDirectory();
            }

            if(stopSavingOrLoading){
                stopSavingOrLoading = false;
                return;
            }

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

            writer.write("MARROW - Don't change name of this file!\n\nParentLayer");
            System.out.print("\nMARROW\n\nParentLayer");

            writer.write("\n\nMaxFrameCount: ");
            writer.write(String.valueOf(animDataStorage.maxFrameCount));
            writer.write("\nFPS: ");
            writer.write(String.valueOf(animDataStorage.framesPerSecond));

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

                saveKeyFrames(child, writer, dashCount);

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

    private static void saveKeyFrames(ChildLayer child, FileWriter writer, String dashCount) throws IOException {
        ArrayList<ArrayList<Keyframe>> channels = child.keyframes;

        Keyframe keyframe;
        TransformChannels[] channelNames = TransformChannels.values();
        TransformChannels channelName;
        EaseType easing;

        for (int channel = 0; channel < channels.size(); channel++) {

            channelName = channelNames[channel];

            writer.write("\n" + (dashCount + "-") + "CHANNEL: " + channel + channelName.name());

            for (int keyframes = 0; keyframes < channels.get(channel).size(); keyframes++) {

                keyframe = channels.get(channel).get(keyframes);
                easing = keyframe.easing;

                if(!keyframe.isActive){
                    continue;
                }

                writer.write("\n" + (dashCount + "--") + keyframes + ": " + keyframe.value + " $" + easing.name());
            }
        }
    }
    //endregion

    //region LOAD FUNCTIONS

    /**
     * Loads all the layers and their corresponding keyframes in a user-chosen save folder.
     * @param toolContainer For BitmapLayer constructor.
     * @param animDataStorage Loads previous animation data into this object.
     * @return An ArrayList of loaded {@link BitmapLayer}s.
     * @throws IOException In the event a file can't be read.
     */
    public static ArrayList<BitmapLayer> loadLayers(ToolContainer toolContainer,
                                                    AnimationDataStorage animDataStorage)throws IOException{

        setLoadDirectory();

        if(stopSavingOrLoading){
            stopSavingOrLoading = false;
            return null;
        }

        File saveFile = new File(currentLoadDirectory + "/save.marrow");
        Scanner fileReader = new Scanner(saveFile);

        LinkedList<String> layerNames = new LinkedList<>();
        ArrayList<BitmapLayer> bitmapLayers = new ArrayList<>();

        //contain everything in the save file into a list of strings
        while(fileReader.hasNextLine()){
            String layerName = fileReader.nextLine();

            if( layerName.isEmpty() ) { continue; }

            if( layerName.charAt(0) == '-') {
                layerName = removeDashes(layerName);
            }

            layerNames.add(layerName);
        }

        int maxFrameCount = getNumberFromString(layerNames.get(2));
        int framesPerSecond = getNumberFromString(layerNames.get(3));

        animDataStorage.maxFrameCount = maxFrameCount;
        animDataStorage.framesPerSecond = framesPerSecond;

        int currentBitmapIndex = -1;

        for (int i = 0; i < layerNames.size(); i++) {
            if(i < 4){
                continue;
            }

            String layerName = layerNames.get(i);

            if(isSavedChannel(layerName)){

                i = saveKeyframesToBitmap(bitmapLayers.get(currentBitmapIndex), layerNames, i);
                continue;
            }

            String filePath = currentLoadDirectory + "/" + layerName + ".png";
            File layer = new File(filePath);

            if (layer.createNewFile()) {
                layer.delete();
                continue; //if the layer doesn't exist, can't load it
            }

            BufferedImage image = ImageIO.read(layer);
            Bitmap imageToBitmap = new Bitmap(image);

            BitmapLayer bitmapLayer = new BitmapLayer(toolContainer, layerName, imageToBitmap);
            bitmapLayer.setSize(maxFrameCount);
            bitmapLayers.add(bitmapLayer);
            currentBitmapIndex += 1;
        }

        return bitmapLayers;
    }

    /**
     * Gets a number from a string if possible.
     * @param stringWithNumber The string the number is from.
     * @return The number from the string. If the number doesn't exist, it returns -1.
     */
    private static int getNumberFromString(String stringWithNumber){

        for (int i = 0; i < stringWithNumber.length(); i++) {
            if (!(stringWithNumber.charAt(i) == ' ')){
                continue;
            }
            try {
                return Integer.parseInt(stringWithNumber.substring(i + 1));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * Loads a set of keyframes from a save file into a {@link BitmapLayer}.
     * @param bitmapLayer the layer having its keyframes set
     * @param layerNames the save file being loaded
     * @param layerNameIndex the scanner's position in the save file
     * @return the current position of the scanner in the save file
     */
    private static int saveKeyframesToBitmap(BitmapLayer bitmapLayer, LinkedList<String> layerNames, int layerNameIndex){

        ArrayList<ArrayList<Keyframe>> channels = bitmapLayer.keyframes;

        int channelID = -1;

        int keyframeID;
        double keyframeValue;
        String layerName;
        EaseType easing;

        //load all the keyframes in a channel, then move to another channel
        //repeat until no more channels need to be set or an error occurs
        while(channelID < channels.size()) {

            //for if the scanner has reached the end of the file
            if(layerNames.size() <= layerNameIndex){
                return layerNameIndex - 1;
            }

            layerName = layerNames.get(layerNameIndex);

            //check if this is a saved channel
            if(isSavedChannel(layerName)){
                channelID++;
                layerNameIndex++;
                continue;
            }

            //find which keyframe in the channel to set
            keyframeID = findKeyframeID(layerName);

            //if less than 0, no longer looking at keyframes and channels but another bitmapLayer
            if(keyframeID < 0){
                layerNameIndex++;
                return layerNameIndex;
            }

            //find the value to set
            keyframeValue = findKeyframeValue(layerName);
            easing = findKeyframeEasing(layerName);

            Keyframe keyframe = channels.get(channelID).get(keyframeID);

            keyframe.isActive = true;
            keyframe.value = keyframeValue;
            keyframe.easing = easing;

            layerNameIndex++;
        }

        return layerNameIndex;
    }

    //region find keyframe values

    /**
     * Finds the easing type of the keyframe
     * @param keyframeName the keyframe being loaded
     * @return the keyframe's easing type
     */
    private static EaseType findKeyframeEasing(String keyframeName){

        boolean ignoreLoop = true;
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < keyframeName.length(); i++) {

            if(keyframeName.charAt(i) == '$'){
                ignoreLoop = false;
                continue;
            }
            else if(ignoreLoop){
                continue;
            }

            value.append(keyframeName.charAt(i));
        }

        try {
            return EaseType.valueOf(String.valueOf(value));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Keyframe's ease type could not be found!");
        }
    }

    /**
     * Finds the frame the keyframe was made in
     * @param keyframeName the keyframe being loaded
     * @return the frame the keyframe was on
     */
    private static int findKeyframeID(String keyframeName){

        for (int i = 0; i < keyframeName.length(); i++) {

            //keyframe's ID is the first thing in keyframeName
            //the colon is the end of the ID
            if(!(keyframeName.charAt(i) == ':')){
                continue;
            }

            //return
            try{
                return Integer.parseInt(keyframeName.substring(0, i));
            }
            catch (NumberFormatException e) {
                return -1;
            }

        }

        return -1;
    }

    /**
     * Finds the value of the keyframe being loaded
     * @param keyframeName the keyframe being loaded
     * @return the keyframe's value
     */
    private static double findKeyframeValue(String keyframeName){

        boolean ignoreLoop = true;
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < keyframeName.length(); i++) {

            //skip until after the first space
            if(keyframeName.charAt(i) == ' '){
                ignoreLoop = false;
                continue;
            }
            else if(ignoreLoop){
                continue;
            }

            //the $ sign is when it starts loading the ease type instead, so stop looping here
            if(keyframeName.charAt(i) == '$'){
                break;
            }

            value.append(keyframeName.charAt(i));
        }

        try{
            return Double.parseDouble(String.valueOf(value));
        }
        catch (NumberFormatException ex){
            throw new RuntimeException("Keyframe value could not be found!");
        }
    }
    //endregion

    /**
     * Checks if a string is a saved channel
     * @param layerName the string being checked
     * @return true if it is a saved channel, false if it isn't
     */
    private static boolean isSavedChannel(String layerName){
        try{
            String getChannel = layerName.substring(0, 8);

            return getChannel.equalsIgnoreCase("CHANNEL:");
        }
        catch (StringIndexOutOfBoundsException ex){
            return false;
        }
    }

    /**
     * Removes the dashes at the back of a layer name.
     * @param layerName The name having its dashes removed.
     * @return The layer name with the removed dashes.
     */
    private static String removeDashes(String layerName){
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
     * Sets the current save directory to where the user wants it to be.
     */
    private static void setSaveDirectory(){
        JFileChooser chooseFile = new JFileChooser(currentSaveDirectory);
        int fileChosen = chooseFile.showSaveDialog(null);

        if(fileChosen == JFileChooser.APPROVE_OPTION){
            currentSaveDirectory = chooseFile.getSelectedFile().getAbsolutePath();
            hasSavedOrLoaded = true;
        }
        else{
            stopSavingOrLoading = true;
        }
    }

    /**
     * Sets the current load directory to where the user wants it to be.
     */
    private static void setLoadDirectory(){
        JFileChooser chooseFile = new JFileChooser(currentLoadDirectory);
        chooseFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int fileChosen = chooseFile.showOpenDialog(null);

        if(fileChosen == JFileChooser.APPROVE_OPTION){
            currentLoadDirectory = chooseFile.getSelectedFile().getAbsolutePath();
            hasSavedOrLoaded = true;
        }
        else{
            stopSavingOrLoading = true;
        }
    }

     /**
     * Sets up the main frame that the user draws on
     */
    static void frameSetup(){
        frame.getContentPane().add(mainSP);

        ToolContainer toolContainer = new ToolContainer();
        AnimationDataStorage animDataStorage = new AnimationDataStorage();
        ParentLayer parentLayer = new ParentLayer(toolContainer, animDataStorage);

        animDataStorage.parentLayer = parentLayer;

        parentLayer.setSize(800,400);
        parentLayer.setVisible(true);

        System.out.println(toolContainer.currentTool.toString());

        Toolbox tools = new Toolbox(toolContainer);

        animDataStorage.setSize(240);

        Timeline timeline = new Timeline(animDataStorage, parentLayer);

        frame.setFocusable(true);
        frame.requestFocusInWindow();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_P) {
                    timeline.playOrPause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        animDataStorage.timeline = timeline;

        LayerWindow layerOrganization = new LayerWindow(parentLayer,toolContainer, timeline);

        //region color chooser gui used for determining the color of the drawTool || lineTool || paintBucket
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
        //endregion

        frame.setSize(1366,768);

        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //places window at the center of the screen
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if(askUser("Would you like to save before you exit?", "SAVE OPTION")){
                    saveLayers(parentLayer, animDataStorage);
                }

                frame.dispose();
                System.exit(0);
            }
        });

        //region parent layer mouse listeners
        parentLayer.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("Parent layer clicked");
                if(parentLayer.currentLayer != null) {
                    if(parentLayer.currentLayer instanceof BitmapLayer bitmapLayer) {
                        bitmapLayer.mousePressed(e);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(parentLayer.currentLayer != null) {
                    if(parentLayer.currentLayer instanceof BitmapLayer bitmapLayer) {
                        bitmapLayer.mouseReleased(e);
                    }
                }
            }
        } );

        parentLayer.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (parentLayer.currentLayer != null) {
                    if (parentLayer.currentLayer instanceof BitmapLayer bitmapLayer) {
                        bitmapLayer.mouseDragged(e);
                    }
                }
            }

        });

        //endregion


        // region useless code??
        /*
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


                    for(child; child<array; child++){

                    layer = (Bitmap) array.getChild

                    newSize = (Dimension) (frameHeight - (toolBox.getY + timeline.getY)),
                                            (frameWidth- childLayer.getX)
                    

                    layer.setSize(newSize)
                    layer.Bitmap.setSize(newSize)

                    }


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
        */
        //endregion

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

        frame.setJMenuBar(createMenuBar(parentLayer, toolContainer, animDataStorage));
        //frame.setLocationByPlatform(true);
        System.out.println(parentLayer.getChildren().size());
    }

    /**
     * Asks the user to answer a yes or no question.
     * @param prompt The question.
     * @param windowName The name of the window the question is in.
     * @return True if user answers yes, false if answered no.
     */
    private static boolean askUser(String prompt, String windowName){
        int choice = JOptionPane.showOptionDialog(
                null, // center of screen
                prompt,
                windowName,
                JOptionPane.YES_NO_OPTION, // Option type (Yes, No, Cancel)
                JOptionPane.QUESTION_MESSAGE, // Message type (question icon)
                null, // Custom icon (null means no custom icon)
                yesNoOptions, // Custom options array
                yesNoOptions[1] // Initial selection (default is "Cancel")
        );

        return choice == JOptionPane.YES_OPTION;
    }

    //region create menu methods

    /**
     * the dropdown menu that is used to hold the file dropdown menu and edit dropdown menu
     *
     * @param parentLayer the layer that holds all layers that the user draws on
     * @param toolContainer the tool container that declares which tools are being used
     * @param animDataStorage the data storage for the animation
     * @return menuBar
     */

    static JMenuBar createMenuBar(ParentLayer parentLayer, ToolContainer toolContainer, AnimationDataStorage animDataStorage) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu(parentLayer, toolContainer, animDataStorage));
        menuBar.add(createEditMenu());
        return menuBar;
    }

    /**
     * dropdown menu that allows the user to create a new animation open, a previous animation or
     * save the current animation
     *
     * @param parentLayer the layer that holds all layers that the user draws on
     * @param toolContainer the tool container that declares which tools are being used
     * @param animDataStorage the data storage for the animation
     * @return fileMenu
     */
    static JMenu createFileMenu(ParentLayer parentLayer, ToolContainer toolContainer, AnimationDataStorage animDataStorage) {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);

        openItem.addActionListener(e -> {
            if(!askUser("Warning: if you load, your unsaved progress WILL be lost! Continue?", "LOADING")){
                return;
            }
            if(!parentLayer.getChildren().isEmpty()){
                parentLayer.getChildren().clear();
            }
            try {
                ArrayList<BitmapLayer> bitmapLayers = loadLayers(toolContainer, animDataStorage);

                if(bitmapLayers == null){
                    return;
                }

                for (BitmapLayer bitmapLayer : bitmapLayers) {
                    parentLayer.addChild(bitmapLayer);
                }
                parentLayer.revalidate();
                parentLayer.repaint();

                //System.out.println("load successful");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveItem.addActionListener(e -> {
            saveLayers(parentLayer, animDataStorage);
        });

        saveAsItem.addActionListener(e -> {
            if(hasSavedOrLoaded){
                setSaveDirectory();
            }
            saveLayers(parentLayer, animDataStorage);
        });

        return fileMenu;
    }

    /**
     * dropdown menu that allows the user to cut, copy, or paste a piece of animation
     *
     * @return editMenu
     */
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
    //endregion
}
