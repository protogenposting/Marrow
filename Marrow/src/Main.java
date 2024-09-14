import Layers.BitmapLayer;
import Layers.*;
import Tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Main {
    public Tool currentTool = new Tool();
    public static void main(String[] args) {
        //the main frame we will be drawing on
        JFrame frame = new JFrame("Marrow");
        Container content = frame.getContentPane();
        Toolbox tools = new Toolbox("Marrow Toolbox");

        LinkedList<Layer> layers = new LinkedList<>();

        //set layout
        content.setLayout(new BorderLayout());

        //create draw area
        BitmapLayer bitmapLayer = new BitmapLayer();

        //add the bitmap layer to the main window
        content.add(bitmapLayer, BorderLayout.CENTER);

        //controls, these will be used for buttons later
        JPanel controls = new JPanel();

        frame.setSize(1366,768);

        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //places window at the center of the screen

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //update loop
        while(true)
        {
            //move these to a key pressed event
            //ImageConversions.SaveImage(bitmapLayer.getImage());
            //System.out.println("saved");
        }
    }
}