package Tools;
import Layers.BitmapLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolButton extends JButton {

    private int currentX, currentY, oldX, oldY;

    JButton toolButton;
    public Tool tool;

    public ToolButton(String iconFile, Tool tool){
        toolButton = initializeButton(iconFile);
        this.tool = tool;
    }


    /*
    public void getOldMouseCoordinates(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //save the old coords
                oldX = e.getX();
                oldY = e.getY();
            }
        });
    }

     */

    public void paintBrush(BitmapLayer bitmap){
        System.out.println("WIP paintbrush");

    }

    public void bucket(BitmapLayer bitmap){
        System.out.println("WIP bucket");
    }

    public void line(BitmapLayer bitmap){
        System.out.println("WIP line");
    }

    public void shape(BitmapLayer bitmap){
        System.out.println("WIP shape");
    }

    /**
     * initializes each tool button with its size and icon
     * @param iconFile the url to the icon png
     * @return the initialized button
     */
    public JButton initializeButton(String iconFile){
        Icon icon = new ImageIcon(iconFile);
        JButton tempButton = new JButton(icon);
        Dimension windowSize = new Dimension(200, 120);

        tempButton.setVisible(true);
        tempButton.setSize(windowSize);

        return tempButton;
    }


}
