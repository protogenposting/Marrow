package Tools;

import Bitmaps.RGBColor;
import Layers.ParentLayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Toolbox extends JPanel {

    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel(); //adds all buttons to this
    ToolContainer toolContainer;

    SaveTool saver;

    ParentLayer parentLayer;

    /**
     * initializes the toolbox window
     */
    public Toolbox(ToolContainer toolContainer, SaveTool saver, ParentLayer parentLayer) {
        frame.setTitle("Marrow Toolbox");
        frame.setSize(800,150);

        this.toolContainer = toolContainer;

        this.saver = saver;

        this.parentLayer = parentLayer;

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

        //region ANTHONY'S UNFINISHED CODE
        /*
        buttonPanel.add(promptCloser);
        promptCloser.setSize(200,120);
        promptCloser.setVisible(true);

        buttonPanel.add(timePrompt);
        timePrompt.setSize(200,120);
        timePrompt.setVisible(true);

        buttonPanel.add(activateChange);
        activateChange.setSize(200,120);
        activateChange.setVisible(true);
        activateChange.addActionListener(e -> {
            try{
                int drawSize = Integer.parseInt(timePrompt.getText());
                // setPixels drawn to drawsize WHEN YOU FIND THE VARIABLE NAME
                System.out.println("Draw size changed");
            } catch (NumberFormatException Ex){
                System.out.println("ERROR: INVALID INPUT");
            }
        });
         */
        //endregion

        frame.add(buttonPanel);

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

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tiger add a way to call some load layer function from here
            }
        });

        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saver.saveLayers(parentLayer);
            }
        });

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
