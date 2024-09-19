package Tools;

import Bitmaps.RGBColor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Toolbox extends JPanel {

    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel();
    ToolContainer toolContainer;// adds all buttons to this

    // these 4 buttons will connect to their corresponding methods / classes (?)
    //JButton paintBrush = initializeButton("paint brush", "iconImages/brushTool.png");
    //JButton bucket = initializeButton("bucket", "iconImages/bucketTool.png");
    //JButton line = initializeButton("line", "iconImages/lineTool.png");
    //JButton shape = initializeButton("shape", "iconImages/shapeTool.png"); //circle or rectangle idk

    // icon file shouldn't have src/, otherwise images don't render for some reason
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
        line.toolButton.addActionListener(e -> {
            toolContainer.currentTool = line.tool;
        });
        shape.toolButton.addActionListener(e -> {
            toolContainer.currentTool = shape.tool;
        });

        toolContainer.currentTool.currentColor = toolContainer.currentColor;
        
        //line.addActionListener(e -> { line.swapTool(ToolID.LINE); });
        //shape.addActionListener(e -> { shape.swapTool(ToolID.SHAPE); });

        JColorChooser colorChooser = new JColorChooser();

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Color currentColor = colorChooser.getColor();
                toolContainer.currentColor = new RGBColor(
                        currentColor.getRed(),
                        currentColor.getGreen(),
                        currentColor.getBlue(),
                        100);
                toolContainer.currentTool.currentColor = toolContainer.currentColor;
            }
        });

        frame.add(colorChooser);

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
