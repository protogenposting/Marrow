package Tools;

import Bitmaps.RGBColor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Toolbox extends JPanel {

    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel(); //adds all buttons to this
    ToolContainer toolContainer;

    //region button initializations

    // icon file shouldn't have src/, otherwise images don't render for some reason
    ToolButton paintBrush = new ToolButton("/iconImages/brushTool.png", new Paintbrush());
    ToolButton bucket = new ToolButton("/iconImages/bucketTool.png", new Bucket());
    ToolButton line = new ToolButton("/iconImages/lineTool.png", new LineTool());
    ToolButton shape = new ToolButton("/iconImages/shapeTool.png", new ShapeTool());
    ToolButton eraser = new ToolButton("/iconImages/shapeTool.png", new Eraser());
    //endregion

    /**
     * initializes the toolbox window
     */
    public Toolbox(ToolContainer toolContainer) {
        frame.setTitle("Marrow Toolbox");
        frame.setSize(600,150);

        this.toolContainer = toolContainer;

        //region add events to the tool buttons and add buttons to button panel

        // "if button is pressed, do this event"
        // the code below makes it so when you press the corresponding tool button, your current tool will swap to it

        paintBrush.toolButton.addActionListener(e -> {setTool(paintBrush);});
        bucket.toolButton.addActionListener(e -> {setTool(bucket);});
        line.toolButton.addActionListener(e -> {setTool(line);});
        eraser.toolButton.addActionListener(e -> {setTool(eraser);});
        shape.toolButton.addActionListener(e -> {setTool(shape);});

        buttonPanel.add(paintBrush.toolButton);
        buttonPanel.add(bucket.toolButton);
        buttonPanel.add(line.toolButton);
        buttonPanel.add(shape.toolButton);
        buttonPanel.add(eraser.toolButton);

        //endregion

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

        frame.add(buttonPanel);

    }


    /**
     * sets the user's current tool to the tool parameter entered
     * @param tool the tool that the player's tool is being set to
     */
    private void setTool(ToolButton tool){
        toolContainer.currentTool = tool.tool;
        toolContainer.currentTool.currentColor = toolContainer.currentColor;
    }

    //region menus
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
    //endregion
}
