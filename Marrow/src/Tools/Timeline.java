package Tools;

import javax.swing.*;

public class Timeline extends JFrame {

    JFrame frame = new JFrame();
    JPanel timeLengthPanelU = new JPanel();

    static double timeLength_Num = 3.0;
    static String timeLength_String = String.valueOf(timeLength_Num);

    JLabel lengthPrompt = new JLabel("Timeline Length: ");
    JTextField timePrompt = new JTextField(timeLength_String);
    JLabel promptCloser = new JLabel("s.");
    

    public Timeline(String windowName){

        frame.setTitle(windowName);
        frame.setSize(1366, 300);

        frame.setResizable(true);
        frame.setVisible(true);
        timeLengthPanelU.setVisible(true);

        lengthPrompt.setSize(200, 120);
        lengthPrompt.setVisible(true);

        timePrompt.setSize(200,120);
        timePrompt.setVisible(true);

        promptCloser.setSize(200,120);
        promptCloser.setVisible(true);

        timeLengthPanelU.add(lengthPrompt);
        timeLengthPanelU.add(timePrompt);
        timeLengthPanelU.add(promptCloser);

        frame.add(timeLengthPanelU);
        frame.setLocationRelativeTo(null);

    }


    public static void addTime(double newTime){

        // changes the timeCap/length of the timeline

        timeLength_Num = newTime;
    }
}
