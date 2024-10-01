package Tools;

import javax.swing.*;
import java.awt.*;

public class Timeline extends JFrame {

    JFrame frame = new JFrame();
    static JPanel panel = new JPanel();

    JSlider timelineSlider = new JSlider(JSlider.HORIZONTAL, 0, 36,36);
    JButton addFrames = new JButton("Add " + 1 + " Frame(s)");
    JButton removeFrames = new JButton("Remove " + 1 + " Frame(s)");
    

    public Timeline(String windowName){


        //MAIN FRAME, HOLDS PANEL
        frame.setTitle(windowName);
        frame.setSize(1366, 300);

        frame.setResizable(true);
        frame.setVisible(true);

        addFrames.setSize(200,120);
        addFrames.setVisible(true);

        removeFrames.setSize(200,120);
        removeFrames.setVisible(true);

        panel.add(addFrames);
        panel.add(removeFrames);

        timelineSlider.setMinorTickSpacing(1);
        timelineSlider.setMajorTickSpacing(12);
        timelineSlider.setPaintLabels(true);
        timelineSlider.setPaintTicks(true);

        timelineSlider.setMaximum(timelineSlider.getMaximum()+1);

        panel.add(timelineSlider);


        //FINALIZING DISPLAY
        frame.add(panel);


    }
}
