package Tools;

import javax.swing.*;

public class Timeline extends JPanel {

    JButton tempButton = new JButton("THIS IS TIMLINE");



    public Timeline(String windowName){

        this.add(tempButton);
        tempButton.setVisible(true);
        tempButton.addActionListener(e -> {
            System.out.println("timeButton pressed");
        });
    }
}
