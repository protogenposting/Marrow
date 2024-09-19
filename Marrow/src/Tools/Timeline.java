package Tools;

import javax.swing.*;
import java.awt.*;

public class Timeline extends JFrame {

    JFrame frame = new JFrame();
    static JPanel timeLengthPanelU = new JPanel();
    static JPanel contentPane = new JPanel(null);

    static int timeLength_Num = 3;
    static String timeLength_String = String.valueOf(timeLength_Num);
    static int frameCount = 0;

    JLabel lengthPrompt = new JLabel("Timeline Length: ");
    static JTextField timePrompt = new JTextField(timeLength_String);
    JLabel promptCloser = new JLabel("Frames");
    JButton activateChange = new JButton("Submit");
    JScrollPane HScrolling  = new JScrollPane(timeLengthPanelU);
    

    public Timeline(String windowName){


        //MAIN FRAME, HOLDS
        frame.setTitle(windowName);
        frame.setSize(1366, 300);

        frame.setResizable(true);
        frame.setVisible(true);

        timeLengthPanelU.setSize(1366,768);
        HScrolling.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        HScrolling.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        HScrolling.setBounds(25, 50, 300, 150);
        contentPane.setPreferredSize(new Dimension(300, 150));
        contentPane.add(HScrolling);
        frame.setContentPane(contentPane);
        frame.pack();

        //INNER FRAME, DISPLAYS LENGTH OF ANIMATION
        timeLengthPanelU.setVisible(true);

        lengthPrompt.setSize(200, 120);
        lengthPrompt.setVisible(true);

        timePrompt.setSize(200,120);
        timePrompt.setVisible(true);

        promptCloser.setSize(200,120);
        promptCloser.setVisible(true);

        activateChange.setSize(200,120);
        activateChange.setVisible(true);

        timeLengthPanelU.add(lengthPrompt);
        timeLengthPanelU.add(timePrompt);
        timeLengthPanelU.add(promptCloser);
        timeLengthPanelU.add(activateChange);

        //FINALIZING DISPLAY
        frame.add(timeLengthPanelU);
        changeLength(timeLength_Num);


        activateChange.addActionListener(e -> {
            try{
                int newTime = Integer.parseInt(timePrompt.getText());
                addTime(newTime);
                System.out.println("lengthChanged");
            } catch (NumberFormatException Ex){
                System.out.println("ERROR: INVALID INPUT");
            }
        });

    }


    public static void addTime(int newTime){

        // changes the timeCap/length of the timeline

        changeLength(newTime);
        timePrompt.setText(String.valueOf(newTime));
        changeLength(newTime);
    }


    /**
     * adds & removes frames
     * @param frames
     */
    public static void changeLength(int frames){
        while(true){
            if (frameCount>frames){
                contentPane.getComponent(frameCount).setVisible(false);
                contentPane.remove(frameCount);
                frameCount--;
                contentPane.revalidate();
            }
            if (frameCount==frames){
                break;
            }
            if (frameCount<frames){
                JButton tempButton = new JButton("frame" + (frameCount + 1));
                Dimension windowSize = new Dimension(200, 120);
                tempButton.setVisible(true);
                tempButton.setSize(windowSize);

                tempButton.addActionListener(e -> {
                    System.out.println("Frame: " + tempButton.getText() + " loaded.");
                });

                contentPane.add(tempButton);
                frameCount++;
                contentPane.revalidate();
            }
        }
    }





}
