package Tools;

import javax.swing.*;

public class Timeline extends JFrame {

    JFrame mainFrame = new JFrame();
    static JPanel panel = new JPanel();

    JSlider timelineSlider = new JSlider(JSlider.HORIZONTAL, 0, 36,36);
    JButton addFrames = new JButton("Add " + timelineSlider + " Frame(s)");
    JButton removeFrames = new JButton("Remove " + timelineSlider + " Frame(s)");

    JTextField changeFPSTextBox = new JTextField("                   ");
    JButton changeFramesPer = new JButton("Set Frames per Second(DEFAULT 24)");

    int framesPerIncrease = 24;

    public Timeline(String windowName){


        //MAIN FRAME, HOLDS PANEL
        mainFrame.setTitle(windowName);
        mainFrame.setSize(400, 300);

        mainFrame.setResizable(true);
        mainFrame.setVisible(true);

        addFrames.setSize(200,120);
        addFrames.setVisible(true);

        removeFrames.setSize(200,120);
        removeFrames.setVisible(true);

        addFrames.addActionListener(e -> {
            timelineSlider.setMaximum(timelineSlider.getMaximum()+framesPerIncrease);
            timelineSlider.validate();
        });
        removeFrames.addActionListener(e -> {
            timelineSlider.setMaximum(timelineSlider.getMaximum()-framesPerIncrease);
        });



        //panel.add(addFrames);
        //panel.add(removeFrames);

        timelineSlider.setMinorTickSpacing(1);
        timelineSlider.setMajorTickSpacing(framesPerIncrease);
        timelineSlider.setPaintLabels(true);
        timelineSlider.setPaintTicks(true);

        timelineSlider.setMaximum(timelineSlider.getMaximum()+1);

        panel.add(timelineSlider);


        //FINALIZING DISPLAY
        mainFrame.add(panel);


    }
}
