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



    ToolButton paintBrush = new ToolButton("/iconImages/brushTool.png", new Paintbrush());
    ToolButton bucket = new ToolButton("/iconImages/bucketTool.png", new Bucket());
    ToolButton line = new ToolButton("/iconImages/lineTool.png", new LineTool());
    ToolButton shape = new ToolButton("/iconImages/shapeTool.png", new ShapeTool());
    ToolButton eraser = new ToolButton("/iconImages/shapeTool.png", new Eraser());

    /**
     * initializes the toolbox window
     */
    public Toolbox(ToolContainer toolContainer) {
        frame.setTitle("Marrow Toolbox");
        frame.setSize(1366,512);

        this.toolContainer = toolContainer;

        // "if button is pressed, do this event"

        paintBrush.toolButton.addActionListener(e -> {
            toolContainer.currentTool = paintBrush.tool;
            toolContainer.currentTool.currentColor = toolContainer.currentColor;
        });
        bucket.toolButton.addActionListener(e -> {
            toolContainer.currentTool = bucket.tool;
            toolContainer.currentTool.currentColor = toolContainer.currentColor;
        });
        line.toolButton.addActionListener(e -> {
            toolContainer.currentTool = line.tool;
            toolContainer.currentTool.currentColor = toolContainer.currentColor;
        });
        eraser.toolButton.addActionListener(e -> {
            toolContainer.currentTool = eraser.tool;
            toolContainer.currentTool.currentColor = toolContainer.currentColor;
        });
        shape.toolButton.addActionListener(e -> {
            toolContainer.currentTool = shape.tool;
			toolContainer.currentTool.currentColor = toolContainer.currentColor;
        });

        
        
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
                        255);
                toolContainer.currentTool.currentColor = toolContainer.currentColor;
            }
        });

        JFrame colorFrame = new JFrame();

        colorFrame.setSize(768,768);

        colorFrame.add(colorChooser);

        colorFrame.setVisible(true);

        frame.setJMenuBar(createMenuBar());

        frame.setLocationByPlatform(true);

        frame.setResizable(true);
        frame.setVisible(true);

        buttonPanel.add(paintBrush.toolButton);
        buttonPanel.add(bucket.toolButton);
        buttonPanel.add(line.toolButton);
        buttonPanel.add(shape.toolButton);
        buttonPanel.add(eraser.toolButton);

        frame.add(buttonPanel);

        //buttonPanel.setVisible(true);

        //region experimental code
        /*
        setCurrentTool(ButtonID.PAINTBRUSH);
        setCurrentTool(ButtonID.BUCKET);
        setCurrentTool(ButtonID.LINE);
        setCurrentTool(ButtonID.SHAPE);
         */
        //endregion

    }
    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        editMenu.add(cutItem);
        JMenuItem copyItem = new JMenuItem("Copy");
        editMenu.add(copyItem);
        JMenuItem pasteItem = new JMenuItem("Paste");
        editMenu.add(pasteItem);
        return editMenu;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        fileMenu.add(newItem);
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        return fileMenu;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        return menuBar;
    }
}
