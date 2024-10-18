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

    JTextField maxFrameCount = setUpMaxFrameCount(new JTextField("120", 6));
    JTextField framesPerSecond = setUpFramesPerSecond(new JTextField("24", 4));
    JTextField currentFrameTextField = setUpCurrentFrame(new JTextField("0", 6));

    JPanel keyFramePanel = new JPanel();
    JScrollPane keyFramePanels;

    public Timeline(AnimationDataStorage animDataStorage, ParentLayer parentLayer)
    {
        playButton.addActionListener(e -> playOrPause());
        animModeButton.addActionListener(e -> animateOnOrOff());

        playButton.setVisible(true);
        animModeButton.setVisible(true);

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.parentLayer = parentLayer;
        this.animDataStorage = animDataStorage;

        //yes the components are added in this specific order for a reason

        addComponents();
    }

    /**
     * Adds all the immediately necessary GUIs to the Timeline object.
     * The line a GUI is added determines its position on the timeline.
     */
    private void addComponents(){
        JPanel timeline = new JPanel();
        timeline.add(fpsLabel);
        timeline.add(framesPerSecond);

        timeline.add(maxFrameCountLabel);
        timeline.add(maxFrameCount);

        timeline.add(playButton);
        timeline.add(animModeButton);

        timeline.add(frameSlider);
        timeline.add(currentFrameLabel);
        timeline.add(currentFrameTextField);

        this.add(timeline);
    }

    public void addKeyframes(){

        if(keyFramePanels == null){
            setUpKeyFramePanels();
            return;
        }
        keyFramePanels.add(new JButton( String.valueOf( animDataStorage.currentFrame )));

    }

    private void setUpKeyFramePanels(){

        keyFramePanels = new JScrollPane();
        keyFramePanels.setPreferredSize(new Dimension(100, 150));

        keyFramePanels.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        keyFramePanel.setLayout(new BoxLayout(keyFramePanel, BoxLayout.Y_AXIS));
        ArrayList<Keyframe> keyframes;

        try {
            keyframes = parentLayer.currentLayer.keyframes.getFirst();
        }
        catch (NullPointerException e){
            return;
        }

        //adds all active keyframes to this panel
        for (int i = 0; i < keyframes.size(); i++) {
            if(!keyframes.get(i).isActive){
                continue;
            }
            JButton keyframe = new JButton(String.valueOf(i));
            keyframe.addActionListener(e -> keyframeChannelSelection(parentLayer.currentLayer.keyframes.getFirst()));
            keyFramePanel.add(keyframe);
        }

        keyFramePanel.setVisible(true);

        keyFramePanels.add(keyFramePanel);
        keyFramePanels.setVisible(true);
        keyFramePanels.setViewportView(keyFramePanel);

        this.add(keyFramePanels, 0);
    }

    private void keyframeChannelSelection(ArrayList<Keyframe> keyframe){
        JPanel channels = new JPanel();
        channels.setLayout(new BoxLayout(channels, BoxLayout.Y_AXIS));

        channels.add(createChannel("X", TransformChannels.x.getValue(), keyframe.getFirst()));
        channels.add(createChannel("Y", TransformChannels.y.getValue(), keyframe.get(1)));
        channels.add(createChannel("Scale X", TransformChannels.scaleX.getValue(), keyframe.get(2)));
        channels.add(createChannel("Scale Y", TransformChannels.scaleY.getValue(), keyframe.get(3)));

        channels.setVisible(true);

        this.add(channels);
    }

    private JPanel createChannel(String channelName, int channelID, Keyframe keyframe){

        JPanel channel = new JPanel();
        channel.setLayout(new BoxLayout(channel, BoxLayout.X_AXIS));

        JLabel channelValue = new JLabel(channelName + ":");
        JTextField channelValueTextbox = new JTextField("0", 4);

        channelValueTextbox.addActionListener(e -> {
            String userInput = channelValueTextbox.getText();

            keyframe.value = Double.parseDouble(userInput);

        });

        channel.add(channelValue);
        channel.add(channelValueTextbox);
        return channel;
    }

    //region textField methods

    /**
     * Sets a text field to show the current frame the timeline is on at all times.
     * It will also change frameSlider's current frame accordingly if a value is entered.
     * @return The edited text field.
     */
    private JTextField setUpCurrentFrame(JTextField textField){

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

    /**
     * Sets a text field to allow you to change how many frames the animation plays every second.
     * @return The edited text field.
     */
    private JTextField setUpFramesPerSecond(JTextField textField){

        textField.addActionListener(e -> {

            int framesPerSecond;

            try{
                framesPerSecond = Integer.parseInt(textField.getText());

                // This is for refresh rate limits.
                if(framesPerSecond > 360){
                    framesPerSecond = 360;
                }

                animDataStorage.framesPerSecond = framesPerSecond;
            }
            catch (NumberFormatException ex) {
                textField.setText(String.valueOf(animDataStorage.framesPerSecond));
            }
        });

        return textField;
    }

    /**
     * Sets a text field to allow you to change the maximum amount of frames there are in the animation.
     * The maximum frameSlider value is also changed accordingly when the created text field is used.
     * @return The edited text field.
     */
    private JTextField setUpMaxFrameCount(JTextField textField){

        textField.addActionListener(e -> {
            int maxFrameCount;

            try{
                maxFrameCount = Integer.parseInt(textField.getText());
                frameSlider.setMaximum(maxFrameCount);
                animDataStorage.maxFrameCount = maxFrameCount;

                setTickSpacingFromTextField(frameSlider);
            }
            catch (NumberFormatException ex) {
                textField.setText("N/A");
            }
        });

        return textField;
    }
    //endregion

    private JSlider setUpSlider(){
        JSlider slider = new JSlider(0, 120, 0);

        slider.setVisible(true);

        /*
        until we figure out why the tick spacing never wants to change for some reason,
        keep this code commented (or just delete it eventually)
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
         */

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

        if(animDataStorage.isPlaying){
            playButton.setText("Pause");
        }
        else{
            playButton.setText("Play");
        }
    }

    private void animateOnOrOff(){
        animDataStorage.isInAnimateMode = !animDataStorage.isInAnimateMode;

        if(animDataStorage.isInAnimateMode){
            animModeButton.setText("Stop Animating");
        }
        else{
            animModeButton.setText("Start Animating");
        }
    }


}
