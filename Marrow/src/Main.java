import Bitmaps.Pixel;
import Keyframes.Timeline;
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

public class Main {
    //the main frame we will be drawing on
    static JFrame frame = new JFrame("Marrow");

    public static String currentSaveDirectory = "MarrowSaves/Test Project"; // change later on to be able to find the directory user saved it at

    public static void main(String[] args) {
        frameSetup();
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

        LayerWindow layerOrganization = new LayerWindow("Marrow Layers",parentLayer,toolContainer);
        Timeline timeline = new Timeline("Marrow Timeline",parentLayer);
        SaveTool saver = new SaveTool(currentSaveDirectory);
        Toolbox tools = new Toolbox(toolContainer,saver,parentLayer);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                saver.saveLayers(parentLayer);
                System.exit(0);
            }
        });
    }

}
