package Tools;
import Layers.BitmapLayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class ToolButton extends JButton {

    private int currentX, currentY, oldX, oldY;

    JButton toolButton;
    public Tool tool;

    public ToolButton(String iconFile, Tool tool){
        toolButton = initializeButton(iconFile);
        this.tool = tool;
    }

    /**
     * initializes each tool button with its size and icon
     * @param iconFile the url to the icon png
     * @return the initialized button
     */
    public JButton initializeButton(String iconFile) {
        Image image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(iconFile)));
        } catch (IOException e) {

        }
        Icon icon = new ImageIcon(image);
        JButton tempButton = new JButton(icon);
        Dimension windowSize = new Dimension(200, 120);

        tempButton.setVisible(true);
        tempButton.setSize(windowSize);
        tempButton.setEnabled(true);

        return tempButton;
    }


}
