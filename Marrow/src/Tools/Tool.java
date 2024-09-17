package Tools;
import javax.swing.*;
import java.awt.*;

public class Tool extends JButton {

    JButton tool;
    ToolID toolID;

    public Tool(String iconFile, ToolID toolID){
        tool = initializeButton(iconFile);
        this.toolID = toolID;
    }

    /**
     * swaps the tool the user is using according to the tool button pressed
     * @param toolID the tool being switched to
     */
    public void swapTool(ToolID toolID){
        switch (toolID){
            case PAINTBRUSH -> paintBrush();
            case BUCKET -> bucket();
            case LINE -> line();
            case SHAPE -> shape();
        }

    }

    public void paintBrush(){
        System.out.println("WIP paintbrush");
    }

    public void bucket(){
        System.out.println("WIP bucket");
    }

    public void line(){
        System.out.println("WIP line");
    }

    public void shape(){
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
