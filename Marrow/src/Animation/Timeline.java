package Animation;

import javax.swing.*;

public class Timeline extends JPanel {

    Object[] options = {"Yes", "No"};
    AnimationDataStorage animDataStorage;
    JButton playButton = new JButton("Play");
    JButton animModeButton = new JButton("Start Animating");

    JSlider frameSlider = setUpSlider();
    JLabel currentFrameLabel = new JLabel("Current Frame:");
    JLabel maxFrameCountLabel = new JLabel("Max Frame Count:");
    JLabel fpsLabel = new JLabel("Frames per Second:");

    JTextField maxFrameCount = setUpMaxFrameCount(new JTextField("120", 6));
    JTextField framesPerSecond = setUpFramesPerSecond(new JTextField("24", 4));
    JTextField currentFrameTextField = setUpCurrentFrame(new JTextField("0", 6));

    public Timeline(AnimationDataStorage animDataStorage)
    {
        playButton.addActionListener(e -> playOrPause());
        animModeButton.addActionListener(e -> animateOnOrOff());

        this.animDataStorage = animDataStorage;

        //yes the components are added in this specific order for a reason

        this.add(fpsLabel);
        this.add(framesPerSecond);

        this.add(maxFrameCountLabel);
        this.add(maxFrameCount);

        this.add(playButton);
        this.add(animModeButton);

        this.add(frameSlider);
        this.add(currentFrameLabel);
        this.add(currentFrameTextField);
    }
    //region text field methods

    private JTextField setUpCurrentFrame(JTextField textField){
        textField.setVisible(true);

        textField.addActionListener(e -> {
            try{
                animDataStorage.currentFrame = Integer.parseInt(textField.getText());
                frameSlider.setValue(Integer.parseInt(textField.getText()));

            } catch (NumberFormatException ex) {
                textField.setText("ERROR");
            }
        });

        return textField;
    }

    private JTextField setUpFramesPerSecond(JTextField textField){
        textField.setVisible(true);

        textField.addActionListener(e -> {

            int fps;

            try{
                fps = Integer.parseInt(textField.getText());
                animDataStorage.framesPerSecond = fps;
            }
            catch (NumberFormatException ex) {
                textField.setText("N/A");
            }
        });

        return textField;
    }

    private JTextField setUpMaxFrameCount(JTextField textField){
        textField.setVisible(true);

        textField.addActionListener(e -> {
            int maxFrameCount;
            boolean canSetMaxFrame = true;

            try{
                maxFrameCount = Integer.parseInt(textField.getText());

                // makes sure that the user doesn't accidentally delete any frames when they want to lower maxFrameCount
                if(maxFrameCount < frameSlider.getMaximum()){
                    canSetMaxFrame = maxFramePopUp();
                }

                if(canSetMaxFrame) {
                    frameSlider.setMaximum(maxFrameCount);
                    //setTickSpacingFromTextField();
                }
            }
            catch (NumberFormatException ex) {
                textField.setText("ERROR: ENTER A NUMBER");
            }
        });

        return textField;
    }

    private boolean maxFramePopUp(){
        int choice = JOptionPane.showOptionDialog(
                null, // Parent component (null means center on screen)
                "Are you really sure? If you proceed, some frames will be permanently deleted!", // Message to display
                "Decrease Max Frame Count", // Dialog title
                JOptionPane.YES_NO_OPTION, // Option type (Yes, No)
                JOptionPane.QUESTION_MESSAGE, // Message type (question icon)
                null, // Custom icon (null means no custom icon)
                options, // Custom options array
                options[1] // Initial selection (default is "Cancel")
        );
        if (choice == JOptionPane.YES_OPTION){
            return true;
        }
        else if (choice == JOptionPane.NO_OPTION){
            JOptionPane.showMessageDialog(null, "Not proceeding.");
            return false;
        }
        return false;
    }

    //endregion

    //region slider methods
    private JSlider setUpSlider(){
        JSlider slider = new JSlider(0, 120, 0);

        slider.setVisible(true);

        /*
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        int majorTickSpacing = setTickSpacing(slider.getMaximum(), 1);
        int minorTickSpacing = setTickSpacing(slider.getMaximum(), 5);

        slider.setMajorTickSpacing( majorTickSpacing );
        slider.setMinorTickSpacing( minorTickSpacing );
         */

        slider.addChangeListener(e -> {

            animDataStorage.currentFrame = slider.getValue();

            String frameValue = String.valueOf(slider.getValue());
            currentFrameTextField.setText(frameValue);
        });

        return slider;
    }
    /*
    private void setTickSpacingFromTextField(){
        int majorTickSpacing = setTickSpacing(frameSlider.getMaximum(), 1);
        int minorTickSpacing = setTickSpacing(frameSlider.getMaximum(), 5);

        frameSlider.setMajorTickSpacing( majorTickSpacing );
        frameSlider.setMinorTickSpacing( minorTickSpacing );

        System.out.println(frameSlider.getMajorTickSpacing());
        System.out.println(frameSlider.getMinorTickSpacing());
        frameSlider.updateUI();
        frameSlider.repaint();
        frameSlider.revalidate();
    }

    private int setTickSpacing(int maxSlideValue, int divider){
        if(maxSlideValue >= 50){
            return maxSlideValue / (2 * divider);
        }
        else{
            return maxSlideValue / (maxSlideValue / 10 * divider);
        }
    }
    */
    //endregion

    private void playOrPause(){
        animDataStorage.isPlaying = !(animDataStorage.isPlaying);
        System.out.println(animDataStorage.isPlaying);

        if(animDataStorage.isPlaying){
            playButton.setText("Pause");
        }
        else{
            playButton.setText("Play");
        }
    }

    private void animateOnOrOff(){
        animDataStorage.isInAnimateMode = !animDataStorage.isInAnimateMode;
        System.out.println(animDataStorage.isInAnimateMode);

        if(animDataStorage.isInAnimateMode){
            animModeButton.setText("Stop Animating");
        }
        else{
            animModeButton.setText("Start Animating");
        }
    }


}
