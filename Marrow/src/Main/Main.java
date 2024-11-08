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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
//endregion

public class Main {

    //region the main frame everything will be put on

    public static JFrame frame = new JFrame("Marrow");

    //these are all split panes, which can be resized
    static JSplitPane mainSplitPane = new JSplitPane();
    static JSplitPane bottomScreenSplitPaneVertical = new JSplitPane();
    static JSplitPane bottomScreenSplitPaneHorizontal = new JSplitPane();

    static LayerWindow layerWindow;

    //saving variables (they are, in fact, being used)
    public static String currentSaveDirectory;
    public static String currentLoadDirectory;
    public static boolean hasSavedOrLoaded = false;
    public static boolean stopSavingOrLoading = false; // stops the saving or loading if user changes their mind when choosing files
	
    public static ArrayList<BufferedImage> images = new ArrayList<>();

    public static boolean rendering = false;
    /**
     * All main does is call frame setup lol
     * @param args
     */
    public static void main(String[] args) {
        frameSetup();
    }

    //region SAVE FUNCTIONS
    /**
     * Saves the parentLayer and its children into a custom text file named "save.marrow"
     * Tiger can you come and document this please-
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

            ArrayList<ChildLayer> childLayers = parentLayer.getChildren();

            writer.write("MARROW - Don't change name of this file!\n\nParentLayer");

            writer.write("\n\nMaxFrameCount: ");
            writer.write(String.valueOf(animDataStorage.maxFrameCount));
            writer.write("\nFPS: ");
            writer.write(String.valueOf(animDataStorage.framesPerSecond));

            saveChildrenInChildLayer(
                    childLayers,
                    "-",
                    writer,
                    false,
                    0
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
        boolean pathDoesNotExist = path.mkdirs();

        File saveFile = new File(currentSaveDirectory + "/save.marrow");
        boolean saveDoesNotExist = saveFile.createNewFile();

        // If it already exists, overwrite the file.
        if(!saveDoesNotExist){
            return new FileWriter(currentSaveDirectory + "/save.marrow", false);
        }

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
     */
    public static void saveChildrenInChildLayer(ArrayList<ChildLayer> childLayers, String dashCount,
                                                FileWriter writer, boolean hasRepeated, int childIndex){
        boolean thereIsChild;
        String childLayerName;

        for (ChildLayer layer : childLayers) {

            if(hasRepeated){
                layer = childLayers.get(childIndex);
            }

            thereIsChild = isThereChildrenInChildLayer(layer);

            try {
                childLayerName = layer.name;
                writer.write("\n" + dashCount + childLayerName);
                saveKeyFrames(layer, writer, dashCount);

                //SAVE THE IMAGE!!!
                if (layer.getClass().equals(BitmapLayer.class)) {
                    BitmapLayer bitmapLayer = (BitmapLayer) layer;

                    ImageConversions.SaveImage(bitmapLayer.drawnImage, currentSaveDirectory + "/" + childLayerName + ".png");
                }
            }
            catch (IOException ignore) {}

            if (thereIsChild) {
                ArrayList<ChildLayer> secondChildLayers = layer.getChildren(); //get the children of the child in childLayers

                for (int j = 0; j < secondChildLayers.size(); j++) {
                    saveChildrenInChildLayer(secondChildLayers, dashCount + "-", writer, true, j);
                }
            } // if this is not here, it will print duplicate layers
            if (hasRepeated) {
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
     * @return An {@link ArrayList} of loaded {@link BitmapLayer}s.
     * @throws IOException In the event a file can't be read.
     */
    public static ArrayList<ChildLayer> loadLayers(ToolContainer toolContainer,
                                                    AnimationDataStorage animDataStorage)throws IOException{

        setLoadDirectory();

        if(stopSavingOrLoading){
            stopSavingOrLoading = false;
            return null;
        }

        File saveFile = new File(currentLoadDirectory + "/save.marrow");
        Scanner fileReader = new Scanner(saveFile);

        LinkedList<String> layerNames = new LinkedList<>();
        ArrayList<ChildLayer> childLayers = new ArrayList<>();

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
                i = saveKeyframesToBitmap(childLayers.get(currentBitmapIndex), layerNames, i);
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
            childLayers.add(bitmapLayer);
            currentBitmapIndex += 1;
        }

        return childLayers;
    }

    private static boolean areBothChildren(String currentParent, String layerName){
        int currentParentIndents = getIndentCount(currentParent);
        int layerNameIndents = getIndentCount(layerName);
        return currentParentIndents == layerNameIndents;
    }

    private static int getIndentCount(String name){
        int indentCount = 0;

        for (int i = 0; i < name.length(); i += 2) {

            try {
                if (name.charAt(i) == '⤷' && name.charAt(i + 1) == ' ') {
                    indentCount += 1;
                }
            }
            catch(ArrayIndexOutOfBoundsException e){
                return indentCount;
            }
        }

        return indentCount;
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
    private static int saveKeyframesToBitmap(ChildLayer bitmapLayer, LinkedList<String> layerNames, int layerNameIndex){

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
                System.out.println(layerNameIndex);
                System.out.println(layerName);
                continue;
            }

            //find which keyframe in the channel to set
            keyframeID = findKeyframeID(layerName);

            //if less than 0, no longer looking at keyframes and channels but another bitmapLayer
            if(keyframeID < 0){
                System.out.println("returning index: " + layerNameIndex);
                System.out.println(layerName);
                return layerNameIndex - 1;
            }

            //find the value to set
            keyframeValue = findKeyframeValue(layerName);
            easing = findKeyframeEasing(layerName);

            Keyframe keyframe = channels.get(channelID).get(keyframeID);

            keyframe.isActive = true;
            keyframe.value = keyframeValue;
            keyframe.easing = easing;

            layerNameIndex++;
            System.out.println(layerNameIndex);
            System.out.println(layerName);
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
        chooseFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

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
        //add the main split pane.
        frame.getContentPane().add(mainSplitPane);

        //important components
        ToolContainer toolContainer = new ToolContainer();
        AnimationDataStorage animationDataStorage = new AnimationDataStorage();
        ParentLayer parentLayer = new ParentLayer(toolContainer, animationDataStorage);

        //add the parent layer to animation data
        animationDataStorage.parentLayer = parentLayer;

        //set the parent layer's size
        parentLayer.setSize(800,400);
        parentLayer.setPreferredSize(new Dimension(800, 400));
        parentLayer.setVisible(true);

        Toolbox tools = new Toolbox(toolContainer);

        //set the default size of the timeline
        animationDataStorage.setSize(240);

        Timeline timeline = new Timeline(animationDataStorage, parentLayer);

        //these need to be called for functions related to keys.
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                //toggle playing with P
                if(e.getKeyCode() == KeyEvent.VK_P) {
                    timeline.playOrPause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        //give the timeline to animation data
        animationDataStorage.timeline = timeline;

        layerWindow = new LayerWindow(parentLayer,toolContainer, timeline);

        //region color chooser gui used for determining the color of a tool
        JColorChooser colorChooser = new JColorChooser();

        //update color when color is clicked
        colorChooser.getSelectionModel().addChangeListener(e -> {
            Color currentColor = colorChooser.getColor();
            toolContainer.currentColor = new RGBColor(
                    currentColor.getRed(),
                    currentColor.getGreen(),
                    currentColor.getBlue(),
                    255);
            toolContainer.currentTool.currentColor = toolContainer.currentColor;
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

        //do this when frame is closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if(askUser("Would you like to save before you exit?", "SAVE OPTION")){
                    saveLayers(parentLayer, animationDataStorage);
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

        //region Split Pane Set up
        mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(75);
        mainSplitPane.setTopComponent(tools); //TOOL BAR HERE
        mainSplitPane.setBottomComponent(bottomScreenSplitPaneVertical);

        bottomScreenSplitPaneVertical.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        bottomScreenSplitPaneVertical.setDividerLocation(260);
        bottomScreenSplitPaneVertical.setLeftComponent(layerWindow); //CHILD LAYER HERE
        bottomScreenSplitPaneVertical.setRightComponent(bottomScreenSplitPaneHorizontal);

        bottomScreenSplitPaneHorizontal.setOrientation(JSplitPane.VERTICAL_SPLIT);
        bottomScreenSplitPaneHorizontal.setDividerLocation(450);
        bottomScreenSplitPaneHorizontal.setTopComponent(parentLayer); // DRAW SCREEN HERE
        bottomScreenSplitPaneHorizontal.setBottomComponent(timeline); // TIMELINE HERE
        //endregion

        frame.setJMenuBar(createMenuBar(parentLayer, toolContainer, animationDataStorage));
    }

    /**
     * Asks the user to answer a yes or no question.
     * @param prompt The question.
     * @param windowName The name of the window the question is in.
     * @return {@code true} if user answers yes, {@code false} if answered no.
     */
    private static boolean askUser(String prompt, String windowName){
        Object[] yesNoOptions = {"Yes", "No"};

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

    private static ArrayList<ChildLayer> rearrangeChildLayers(ArrayList<ChildLayer> childLayers){
        ChildLayer currentParent = childLayers.getFirst();
        ArrayList<ChildLayer> newChildLayers = new ArrayList<>();

        //if childLayer has the same indent count OR less, it is now the current parent

        for(ChildLayer childLayer : childLayers){

            if(getIndentCount(childLayer.name) <= getIndentCount(currentParent.name)){
                currentParent = childLayer;
            }

            if(currentParent != childLayer){
                currentParent.addChild(childLayer, true);
            }

            newChildLayers.add(childLayer);
        }

        return newChildLayers;
    }

    //region create menu methods

    /**
     * the dropdown menu that is used to hold the file dropdown menu and edit dropdown menu
     *
     * @param parentLayer the {@link Layer} that holds all layers that the user draws on
     * @param toolContainer the tool container that declares which tools are being used
     * @param animDataStorage the data storage for the animation
     * @return menuBar
     */

    static JMenuBar createMenuBar(ParentLayer parentLayer, ToolContainer toolContainer, AnimationDataStorage animDataStorage) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu(parentLayer, toolContainer, animDataStorage));
        menuBar.add(createEditMenu());
        menuBar.add(createHelpMenu());
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
        JMenuItem renderItem = new JMenuItem("Render");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(renderItem);

        //very wip
        newItem.addActionListener(e -> {
            if(!askUser("Create new?", "NEW")){
                return;
            }
            mainSplitPane.removeAll();
            bottomScreenSplitPaneVertical.removeAll();
            bottomScreenSplitPaneHorizontal.removeAll();
            frame.removeAll();
            frameSetup();
            //frame.repaint();
        });

        //load function
        openItem.addActionListener(e -> {
            if(!askUser("Warning: if you load, your unsaved progress WILL be lost! Continue?", "LOADING")){
                return;
            }
            if(!parentLayer.getChildren().isEmpty()){
                parentLayer.getChildren().clear();
            }
            try {
                ArrayList<ChildLayer> childLayers = loadLayers(toolContainer, animDataStorage);
                childLayers = rearrangeChildLayers(childLayers);

                if(childLayers == null){
                    return;
                }

                for (ChildLayer childLayer : childLayers) {
                    if(getIndentCount(childLayer.name) == 0) {
                        parentLayer.addChild(childLayer, true);
                    }
                }
                parentLayer.repaint();
                parentLayer.revalidate();
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

        renderItem.addActionListener(e -> {
            rendering = true;
            images = new ArrayList<>();
            animDataStorage.currentFrame = 0;
            animDataStorage.isPlaying = false;
            parentLayer.repaint();
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
    static JMenu createHelpMenu() {
        JMenu editMenu = new JMenu("Help");

        JMenuItem cutItem = new JMenuItem("Open Manual");

        cutItem.addActionListener(e -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("http://protogenposting.github.io/Marrow/manual/user_manual.html"));
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        editMenu.add(cutItem);
        return editMenu;
    }
    //endregion
}
