package Tools;

import javax.swing.*;
import java.awt.*;

public class Toolbox extends JPanel {

    ToolContainer toolContainer;

    JLabel promptCloser = new JLabel("Set brush size(Pixels)");
    JTextField setBrushSize = new JTextField("5", 5);

    final int drawSizeCap = 1000;

    /**
     * initializes the toolbox window
     */
    public Toolbox(ToolContainer toolContainer) {

        this.setSize(800,150);
        this.setPreferredSize(new Dimension(800,150));

        this.toolContainer = toolContainer;

        //region add events to the tool buttons and add buttons to button panel

        // "if button is pressed, do this event"
        // the code below makes it so when you press the corresponding tool button, your current tool will swap to it

        ToolButton paintBrush = new ToolButton("/IconImages/Tools/brushTool.png", new Paintbrush());
        ToolButton bucket = new ToolButton("/IconImages/Tools/bucketTool.png", new Bucket());
        ToolButton line = new ToolButton("/IconImages/Tools/lineTool.png", new LineTool());
        ToolButton shape = new ToolButton("/IconImages/Tools/shapeTool.png", new ShapeTool());
        ToolButton eraser = new ToolButton("/IconImages/Tools/eraserTool.png", new Eraser());

        paintBrush.toolButton.addActionListener(e -> setTool(paintBrush));
        bucket.toolButton.addActionListener(e -> setTool(bucket));
        line.toolButton.addActionListener(e -> setTool(line));
        eraser.toolButton.addActionListener(e -> setTool(eraser));
        shape.toolButton.addActionListener(e -> setTool(shape));

        this.add(paintBrush.toolButton);
        this.add(bucket.toolButton);
        this.add(line.toolButton);
        this.add(shape.toolButton);
        this.add(eraser.toolButton);

        //endregion

        //frame.setJMenuBar(createMenuBar());
        //frame.setLocationByPlatform(true);

        //panel.setResizable(true);
        this.setVisible(true);

        this.add(promptCloser);
        promptCloser.setSize(200,120);
        promptCloser.setVisible(true);

        this.add(setBrushSize);
        setBrushSize.setSize(200,120);
        setBrushSize.setVisible(true);

        setBrushSize.addActionListener(e -> {resetBrushSizeText(eraser);});
    }

    /**
     * sets the toolButton's tool to a specific brush or tool
     * @param toolButton the toolButton being set
     */
    private void setTool(ToolButton toolButton){
        toolContainer.currentTool = toolButton.tool;
        toolContainer.currentTool.currentColor = toolContainer.currentColor;

        setBrushSize.setText(String.valueOf(toolButton.tool.drawSize));
    }

    private void resetBrushSizeText(ToolButton eraser){
        try{
            int newDrawSize = Integer.parseInt(setBrushSize.getText());
            if(newDrawSize > drawSizeCap)
            {
                newDrawSize = drawSizeCap;
            }

            //here so eraser draw size is separate from everything else
            if (toolContainer.currentTool == eraser.tool) {
                eraser.tool.drawSize = newDrawSize;
            }
            else{
                toolContainer.currentTool.drawSize = newDrawSize;
            }
        } catch (NumberFormatException Ex){
            setBrushSize.setText(String.valueOf(toolContainer.currentTool.drawSize));
        }
    }
}
