package Tools;
import Layers.BitmapLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolButton extends JButton {

    private int currentX, currentY, oldX, oldY;

    JButton tool;
    ToolID toolID;

    public ToolButton(String iconFile, ToolID toolID){
        tool = initializeButton(iconFile);
        this.toolID = toolID;
    }

    /**
     * swaps the tool the user is using according to the tool button pressed
     * @param toolID the tool being switched to
     */
    public void swapTool(ToolID toolID){
        switch (toolID){
            case PAINTBRUSH -> this.toolID = ToolID.PAINTBRUSH;
            case BUCKET -> this.toolID = ToolID.BUCKET;
            case LINE -> this.toolID = ToolID.LINE;
            case SHAPE -> this.toolID = ToolID.SHAPE;
        }
    }

    public void useTool(ToolID toolID, BitmapLayer bitmap){
        switch(toolID){
            case PAINTBRUSH -> paintBrush(bitmap);
            case BUCKET -> bucket(bitmap);
            case LINE -> line(bitmap);
            case SHAPE -> shape(bitmap);
        }
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
