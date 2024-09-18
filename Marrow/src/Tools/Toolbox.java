package Tools;

import javax.swing.*;

public class Toolbox extends JPanel {

    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel();
    ToolContainer toolContainer;// adds all buttons to this

    // these 4 buttons will connect to their corresponding methods / classes (?)
    //JButton paintBrush = initializeButton("paint brush", "iconImages/brushTool.png");
    //JButton bucket = initializeButton("bucket", "iconImages/bucketTool.png");
    //JButton line = initializeButton("line", "iconImages/lineTool.png");
    //JButton shape = initializeButton("shape", "iconImages/shapeTool.png"); //circle or rectangle idk

    ToolButton paintBrush = new ToolButton("src/iconImages/brushTool.png", new Paintbrush());
    ToolButton bucket = new ToolButton("src/iconImages/bucketTool.png", new Bucket());
    ToolButton line = new ToolButton("src/iconImages/lineTool.png", new LineTool());
    ToolButton shape = new ToolButton("src/iconImages/shapeTool.png", new ShapeTool());

    /**
     * initializes the toolbox window
     */
    public Toolbox(ToolContainer toolContainer) {
        frame.setTitle("Marrow Toolbox");
        frame.setSize(1366,128);

        frame.setResizable(true);
        frame.setVisible(true);
        buttonPanel.setVisible(true);

        buttonPanel.add(paintBrush.toolButton);
        buttonPanel.add(bucket.toolButton);
        buttonPanel.add(line.toolButton);
        buttonPanel.add(shape.toolButton);

        frame.add(buttonPanel);

        this.toolContainer = toolContainer;

        // "if button is pressed, do this event"
        paintBrush.toolButton.addActionListener(e -> {
            toolContainer.currentTool = paintBrush.tool;
        });
        bucket.toolButton.addActionListener(e -> {
            toolContainer.currentTool = bucket.tool;
        });
        //line.addActionListener(e -> { line.swapTool(ToolID.LINE); });
        //shape.addActionListener(e -> { shape.swapTool(ToolID.SHAPE); });

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
