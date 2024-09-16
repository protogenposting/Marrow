package Tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Toolbox extends JPanel {

    enum ButtonID {
        PAINTBRUSH, BUCKET,
        LINE, SHAPE
    }

    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel(); // add all buttons to this

    // these 4 buttons will connect to their corresponding methods / classes (?)
    JButton paintBrush = initializeButton("paint brush", "iconImages/brushTool.png");
    JButton bucket = initializeButton("bucket", "iconImages/bucketTool.png");
    JButton line = initializeButton("line", "iconImages/lineTool.png");
    JButton shape = initializeButton("shape", "iconImages/shapeTool.png"); //circle or rectangle idk

    public Toolbox(String windowName) {
        frame.setTitle(windowName);
        frame.setSize(1366,128);

        frame.setResizable(true);
        frame.setVisible(true);
        buttonPanel.setVisible(true);

        buttonPanel.add(paintBrush); buttonPanel.add(bucket);
        buttonPanel.add(line); buttonPanel.add(shape);

        frame.add(buttonPanel);
        frame.setLocationRelativeTo(null);

        //region experimental code

        paintBrush.addActionListener(e -> { System.out.println("paintbrush selected");});
        bucket.addActionListener(e -> { System.out.println("bucket selected"); });
        line.addActionListener(e -> { System.out.println("line selected"); });
        shape.addActionListener(e -> { System.out.println("shape selected"); });

        //setCurrentTool(ButtonID.PAINTBRUSH);
        //setCurrentTool(ButtonID.BUCKET);
        //setCurrentTool(ButtonID.LINE);
        //setCurrentTool(ButtonID.SHAPE);
        //endregion

    }

    public JButton initializeButton(String buttonText, String iconFile) {
        Icon icon = new ImageIcon(iconFile);
        JButton tempButton = new JButton(icon);
        Dimension windowSize = new Dimension(200, 120);

        tempButton.setVisible(true);
        tempButton.setSize(windowSize);
        //tempButton.setText(buttonText); //replace with sprite later on

        return tempButton;
    }


}
