package Tools;

import javax.swing.*;
import java.awt.*;

public class Toolbox extends JPanel {

    enum ButtonID {
        PAINTBRUSH, BUCKET,
        LINE, SHAPE
    }

    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel(); // adds all buttons to this

    // these 4 buttons will connect to their corresponding methods / classes (?)
    JButton paintBrush = initializeButton("paint brush", "iconImages/brushTool.png");
    JButton bucket = initializeButton("bucket", "iconImages/bucketTool.png");
    JButton line = initializeButton("line", "iconImages/lineTool.png");
    JButton shape = initializeButton("shape", "iconImages/shapeTool.png"); //circle or rectangle idk

    /**
     * initializes the toolbox window
     * @param windowName window name
     */
    public Toolbox(String windowName) {
        frame.setTitle(windowName);
        frame.setSize(1366,128);

        frame.setResizable(true);
        frame.setVisible(true);
        buttonPanel.setVisible(true);

        buttonPanel.add(paintBrush);
        buttonPanel.add(bucket);
        buttonPanel.add(line);
        buttonPanel.add(shape);

        frame.add(buttonPanel);
        frame.setLocationRelativeTo(null);

        // "if button is pressed, do this event"
        paintBrush.addActionListener(e -> { System.out.println("paintbrush selected");});
        bucket.addActionListener(e -> { System.out.println("bucket selected"); });
        line.addActionListener(e -> { System.out.println("line selected"); });
        shape.addActionListener(e -> { System.out.println("shape selected"); });

        //region experimental code
        /*
        setCurrentTool(ButtonID.PAINTBRUSH);
        setCurrentTool(ButtonID.BUCKET);
        setCurrentTool(ButtonID.LINE);
        setCurrentTool(ButtonID.SHAPE);
         */
        //endregion

    }

    /**
     * initializes each tool button with its size and icon
     * @param buttonText the text inside the button
     * @param iconFile the url to the icon png
     * @return the initialized button
     */
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
