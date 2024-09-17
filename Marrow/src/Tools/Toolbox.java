package Tools;

import javax.swing.*;

public class Toolbox extends JPanel {

    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel(); // adds all buttons to this

    // these 4 buttons will connect to their corresponding methods / classes (?)
    //JButton paintBrush = initializeButton("paint brush", "iconImages/brushTool.png");
    //JButton bucket = initializeButton("bucket", "iconImages/bucketTool.png");
    //JButton line = initializeButton("line", "iconImages/lineTool.png");
    //JButton shape = initializeButton("shape", "iconImages/shapeTool.png"); //circle or rectangle idk

    ToolButton paintBrush = new ToolButton("iconImages/brushTool.png", ToolID.PAINTBRUSH);
    ToolButton bucket = new ToolButton("iconImages/brushTool.png", ToolID.BUCKET);
    ToolButton line = new ToolButton("iconImages/lineTool.png", ToolID.LINE);
    ToolButton shape = new ToolButton("iconImages/shapeTool.png", ToolID.SHAPE);

    /**
     * initializes the toolbox window
     */
    public Toolbox() {
        frame.setTitle("Marrow Toolbox");
        frame.setSize(1366,128);

        frame.setResizable(true);
        frame.setVisible(true);
        buttonPanel.setVisible(true);

        buttonPanel.add(paintBrush.tool);
        buttonPanel.add(bucket.tool);
        buttonPanel.add(line.tool);
        buttonPanel.add(shape.tool);

        frame.add(buttonPanel);

        // "if button is pressed, do this event"
        paintBrush.addActionListener(e -> { paintBrush.swapTool(ToolID.PAINTBRUSH); });
        bucket.addActionListener(e -> { bucket.swapTool(ToolID.BUCKET); });
        line.addActionListener(e -> { line.swapTool(ToolID.LINE); });
        shape.addActionListener(e -> { shape.swapTool(ToolID.SHAPE); });

        //region experimental code
        /*
        setCurrentTool(ButtonID.PAINTBRUSH);
        setCurrentTool(ButtonID.BUCKET);
        setCurrentTool(ButtonID.LINE);
        setCurrentTool(ButtonID.SHAPE);
         */
        //endregion

    }




}
