package Animation;

import Layers.ParentLayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Timeline extends JPanel {

    AnimationDataStorage animDataStorage;
    ParentLayer parentLayer;
    JButton playButton = new JButton("Play");
    JButton animModeButton = new JButton("Start Animating");

    JSlider frameSlider = setUpSlider();
    JLabel currentFrameLabel = new JLabel("Current Frame:");
    JLabel maxFrameCountLabel = new JLabel("Max Frame Count:");
    JLabel fpsLabel = new JLabel("Frames per Second:");

    JTextField maxFrameCount = setUpMaxFrameCount();
    JTextField framesPerSecond = setUpFramesPerSecond();
    JTextField currentFrameTextField = setUpCurrentFrame();

    JPanel keyFramePanel = new JPanel();
    JScrollPane keyFramePanels;

    public Timeline(AnimationDataStorage animDataStorage, ParentLayer parentLayer)
    {
        this.setLayout(new FlowLayout());
        this.parentLayer = parentLayer;
        this.animDataStorage = animDataStorage;

        //yes the components are added in this specific order for a reason

        this.add(fpsLabel);
        this.add(framesPerSecond);

        this.add(maxFrameCountLabel);
        this.add(maxFrameCount);

        //region button setup
        playButton.addActionListener(e -> playOrPause());
        animModeButton.addActionListener(e -> animateOnOrOff());

        playButton.setVisible(true);
        animModeButton.setVisible(true);

        this.add(playButton);
        this.add(animModeButton);
        //endregion

        this.add(frameSlider);
        this.add(currentFrameLabel);
        this.add(currentFrameTextField);
    }

    public void addKeyframes(){

        if(keyFramePanels == null){
            setUpKeyFramePanels();
            return;
        }
        keyFramePanels.add(new JButton("1"));

    }

    private void setUpKeyFramePanels(){

        keyFramePanels = new JScrollPane();
        keyFramePanels.setPreferredSize(new Dimension(200, 100));

        keyFramePanels.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        keyFramePanel.setLayout(new BoxLayout(keyFramePanel, BoxLayout.Y_AXIS));
        ArrayList<Keyframe> keyframes;

        try {
            keyframes = parentLayer.currentLayer.keyframes.getFirst();
        }
        catch (NullPointerException e){
            return;
        }


        for (int i = 0; i < keyframes.size(); i++) {
            if(!keyframes.get(i).isActive){
                continue;
            }
            JButton keyframe = new JButton(String.valueOf(i));
            keyFramePanel.add(keyframe);
        }
        keyFramePanel.setVisible(true);

        keyFramePanels.add(keyFramePanel);
        keyFramePanels.setVisible(true);
        keyFramePanels.setViewportView(keyFramePanel);

        this.add(keyFramePanels);
    }

    private JTextField setUpCurrentFrame(){
        JTextField textField = new JTextField("0", 6);
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

    private JTextField setUpFramesPerSecond(){
        JTextField textField = new JTextField("24", 4);
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

    private JTextField setUpMaxFrameCount(){
        JTextField textField = new JTextField("120", 6);
        textField.setVisible(true);

        textField.addActionListener(e -> {
            int maxFrameCount;

            try{
                maxFrameCount = Integer.parseInt(textField.getText());
                frameSlider.setMaximum(maxFrameCount);
                animDataStorage.maxFrameCount = maxFrameCount;

                setTickSpacingFromTextField(frameSlider);
            }
            catch (NumberFormatException ex) {
                textField.setText("ERROR: ENTER A NUMBER");
            }
        });

        return textField;
    }

    private JSlider setUpSlider(){
        JSlider slider = new JSlider(0, 120, 0);

        slider.setVisible(true);
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        int majorTickSpacing = setTickSpacing(slider.getMaximum(), 5);
        int minorTickSpacing = setTickSpacing(slider.getMaximum(), 20);

        slider.setMajorTickSpacing( majorTickSpacing );
        slider.setMinorTickSpacing( minorTickSpacing );

        slider.addChangeListener(e -> {

            animDataStorage.currentFrame = slider.getValue();

            String frameValue = String.valueOf(slider.getValue());
            currentFrameTextField.setText(frameValue);
        });

        return slider;
    }

    private void setTickSpacingFromTextField(JSlider slider){
        int majorTickSpacing = setTickSpacing(slider.getMaximum(), 5);
        int minorTickSpacing = setTickSpacing(slider.getMaximum(), 20);

        slider.setMajorTickSpacing( majorTickSpacing );
        slider.setMinorTickSpacing( minorTickSpacing );
    }

    private int setTickSpacing(int maxSlideValue, int divider){
        if (maxSlideValue > 200){
            return maxSlideValue / (divider * 2);
        }
        else if (maxSlideValue > 20) {
            return maxSlideValue / divider;
        }
        else if(maxSlideValue > 10){
            return 2;
        }
        else {
            return 1;
        }
    }

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
