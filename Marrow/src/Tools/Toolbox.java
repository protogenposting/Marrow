package Tools;

import Bitmaps.RGBColor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Toolbox extends JPanel {


    ToolContainer toolContainer;


    JLabel promptCloser = new JLabel("Set brush size(Pixels)");
    JTextField setBrushSize = new JTextField("          1");
    JButton activateChange = new JButton("Submit");

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

        ToolButton paintBrush = new ToolButton("/iconImages/brushTool.png", new Paintbrush());
        ToolButton bucket = new ToolButton("/iconImages/bucketTool.png", new Bucket());
        ToolButton line = new ToolButton("/iconImages/lineTool.png", new LineTool());
        ToolButton shape = new ToolButton("/iconImages/shapeTool.png", new ShapeTool());
        ToolButton eraser = new ToolButton("/iconImages/eraserTool.png", new Eraser());

        paintBrush.toolButton.addActionListener(e -> {setTool(paintBrush);});
        bucket.toolButton.addActionListener(e -> {setTool(bucket);});
        line.toolButton.addActionListener(e -> {setTool(line);});
        eraser.toolButton.addActionListener(e -> {setTool(eraser);});
        shape.toolButton.addActionListener(e -> {setTool(shape);});

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

        //region ANTHONY'S UNFINISHED CODE

        this.add(promptCloser);
        promptCloser.setSize(200,120);
        promptCloser.setVisible(true);

        this.add(setBrushSize);
        setBrushSize.setSize(200,120);
        setBrushSize.setVisible(true);

        this.add(activateChange);
        activateChange.setSize(200,120);
        activateChange.setVisible(true);
        activateChange.addActionListener(e -> {
            try{
                int newDrawSize = Integer.parseInt(setBrushSize.getText());
                if (toolContainer.currentTool == paintBrush.tool) {
                    paintBrush.tool.drawSize = newDrawSize;
                    System.out.println("Paint Brush draw size is now " + paintBrush.tool.drawSize);
                } else if (toolContainer.currentTool == eraser.tool) {
                    eraser.tool.drawSize = newDrawSize;
                    System.out.println("Eraser draw size is now " + eraser.tool.drawSize);
                }
            } catch (NumberFormatException Ex){
                System.out.println("ERROR: INVALID INPUT");
            }
        });
        //endregion

    }

    private void setTool(ToolButton toolButton){
        toolContainer.currentTool = toolButton.tool;
        toolContainer.currentTool.currentColor = toolContainer.currentColor;
    }

    //region create menus
    /**
     * Creates the edit menu
     * @return the created edit menu
     */
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

    /**
     * creates the File menu
     * @return the created file menu
     */
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

    /**
     * creates the menu bar
     * @return the created menu bar
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        return menuBar;
    }
    //endregion
}
