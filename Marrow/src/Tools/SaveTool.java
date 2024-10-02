package Tools;

import Bitmaps.Bitmap;
import Layers.BitmapLayer;
import Layers.ChildLayer;
import Layers.ParentLayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class SaveTool {
    public String currentSaveDirectory;

    public SaveTool(String currentSaveDirectory)
    {
        this.currentSaveDirectory = currentSaveDirectory;
    }

    /**
     * saves the parentLayer and its children into a custom text file named "save.marrow"
     * @param parentLayer the layer and its children being saved
     */
    public void saveLayers(ParentLayer parentLayer){
        Scanner scan = new Scanner(System.in);

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
    private FileWriter getSaveFileWriter() throws IOException {
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
    private boolean isThereChildrenInChildLayer(ChildLayer childLayer){
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
    private void saveChildrenInChildLayer(ArrayList<ChildLayer> childLayers, String dashCount, FileWriter writer,
                                                boolean hasRepeated, String childLayerName){
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
}
