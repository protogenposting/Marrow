package Animation;

import Layers.ParentLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;
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
    JScrollPane channelPanel;
    JPanel keyframes = new JPanel();
    JPanel keyframeValuePanel = new JPanel();

    Object[] yesNoOptions = {"Yes", "No"};

    private int currentChannel = 0;

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

    public void addChannels(){

        if(channelPanel == null){
            setUpKeyFramePanels();
            return;
        }
        channelPanel.add(new JButton( String.valueOf( animDataStorage.currentFrame )));

    }

    private void setUpKeyFramePanels(){

        channelPanel = new JScrollPane();
        channelPanel.setPreferredSize(new Dimension(100, 200));

        channelPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        keyFramePanel.setLayout(new BoxLayout(keyFramePanel, BoxLayout.Y_AXIS));

        //region add channel buttons
        //replace channelID with TransformChannels enum values
        keyFramePanel.add(createChannelButton("X", 0));
        keyFramePanel.add(createChannelButton("Y", 1));
        keyFramePanel.add(createChannelButton("Scale X", 2));
        keyFramePanel.add(createChannelButton("Scale Y", 3));
        keyFramePanel.add(createChannelButton("Rotation", 4));
        keyFramePanel.add(createChannelButton("Shear", 5));
        keyFramePanel.add(createChannelButton("Opacity", 6));
        //endregion

        keyFramePanel.setVisible(true);

        channelPanel.add(keyFramePanel);
        channelPanel.setVisible(true);
        channelPanel.setViewportView(keyFramePanel);

        this.add(channelPanel, 0);
    }

    /**
     * Creates a channel button that directs to a certain TransformChannel and its corresponding keyframes.
     * @param name The name of the channel.
     * @return The created channel button.
     */
    private JButton createChannelButton(String name, int channelID){
        JButton channelButton = new JButton(name);

        channelButton.setPreferredSize(new Dimension(100, 30));
        channelButton.addActionListener(e -> {
            keyframeSelection(parentLayer.currentLayer.keyframes.get(channelID), channelID);
            currentChannel = channelID;
        });

        return channelButton;
    }

    private void keyframeSelection(ArrayList<Keyframe> keyframe, int channelID){

        if(keyframes.isVisible()){
            keyframes.removeAll();
            keyframes.setVisible(false);
        }

        keyframes.setLayout(new BoxLayout(keyframes, BoxLayout.X_AXIS));

        TransformChannels[] channels = TransformChannels.values();
        TransformChannels channel = channels[channelID];


        keyframes.add(new JLabel("Channel: " + channel.name() + ", Keyframes: "));

        for (int i = 0; i < keyframe.size(); i++) {
            if(!keyframe.get(i).isActive){
                continue;
            }

            JButton keyframeButton = new JButton(String.valueOf(i));
            int finalI = i;
            keyframeButton.addActionListener(e -> editKeyframeValue(channelID, finalI, String.valueOf(finalI)));

            keyframes.add(keyframeButton);
        }

        JButton addKeyFrameButton = new JButton("Add Keyframe");
        JButton removeKeyFrameButton = new JButton("Remove Keyframe");

        addKeyFrameButton.addActionListener(e -> addSpecificKeyFrame(keyframe, channelID));
        removeKeyFrameButton.addActionListener(e -> removeSpecificKeyframe(keyframe, channelID, animDataStorage.currentFrame));

        keyframes.add(addKeyFrameButton);
        keyframes.add(removeKeyFrameButton);

        keyframes.setVisible(true);

        this.add(keyframes);
        revalidate();
        repaint();
    }

    /**
     * Adds a keyframe to the currently selected channel.
     * @param channel The current channel.
     * @param channelID The channel the keyframe is on.
     */
    private void addSpecificKeyFrame(ArrayList<Keyframe> channel, int channelID){
        Keyframe currentKeyframe = parentLayer.currentLayer.keyframes.get(channelID).get(animDataStorage.currentFrame);
        currentKeyframe.isActive = true;

        // "repaint"
        keyframeSelection(channel, channelID);
    }

    /**
     * Removes a keyframe from the currently selected channel.
     * @param channel The current channel.
     * @param channelID The channel the keyframe is on.
     */
    private void removeSpecificKeyframe(ArrayList<Keyframe> channel, int channelID, int currentFrame){
        Keyframe currentKeyframe = parentLayer.currentLayer.keyframes.get(channelID).get(currentFrame);

        currentKeyframe.isActive = false;
        currentKeyframe.value = 0;

        // "repaint"
        keyframeSelection(channel, channelID);
    }

    /**
     * Creates a panel with a text field that allows you to edit the value of a keyframe.
     * @param channelID The channel the keyframe is on.
     * @param keyframeID The location of the keyframe in the channel.
     * @param name The name of the keyframe. Usually a number.
     */
    private void editKeyframeValue(int channelID, int keyframeID, String name){

        // if this panel already exists, reset and create it again
        if(keyframeValuePanel.isVisible()){
            keyframeValuePanel.removeAll();
            keyframeValuePanel.setVisible(false);
        }

        double currentValue = parentLayer.currentLayer.keyframes.get(channelID).get(keyframeID).value;

        JLabel keyframeValue = new JLabel("Keyframe " + name + " Value: ");
        JTextField keyframeValueTextbox = new JTextField(String.valueOf(currentValue), 4);

        keyframeValueTextbox.addActionListener(e -> {
            String userInput = keyframeValueTextbox.getText();

            parentLayer.currentLayer.keyframes.get(channelID).get(keyframeID).value = Double.parseDouble(userInput);

            parentLayer.repaint();
        });

        keyframeValuePanel.setLayout(new BoxLayout(keyframeValuePanel, BoxLayout.X_AXIS));

        keyframeValuePanel.add(keyframeValue);
        keyframeValuePanel.add(keyframeValueTextbox);
        keyframeValuePanel.setVisible(true);

        EaseType[] choices = EaseType.values();

        JComboBox<EaseType> dropdown = new JComboBox<>(choices);

        dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentLayer.currentLayer.keyframes.get(channelID).get(keyframeID).easing = (EaseType) dropdown.getSelectedItem();
                parentLayer.repaint();
            }
        });

        dropdown.setSelectedItem(parentLayer.currentLayer.keyframes.get(channelID).get(keyframeID).easing);

        keyframeValuePanel.add(dropdown);

        this.add(keyframeValuePanel);
        revalidate();
        repaint();
    }

    //region textField methods

    /**
     * Sets up a text field to show the current frame the timeline is on at all times.
     * It will also change frameSlider's current frame accordingly if a value is entered.
     * @return The edited text field.
     */
    private JTextField setUpCurrentFrame(JTextField textField){

        textField.addActionListener(e -> {
            try{
                animDataStorage.currentFrame = Integer.parseInt(textField.getText());
                frameSlider.setValue(Integer.parseInt(textField.getText()));

            } catch (NumberFormatException ex) {
                textField.setText(String.valueOf(animDataStorage.currentFrame));
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
                animDataStorage.resetFramesPerSecond();

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
            boolean canSetMaxFrame = true;
            try{
                maxFrameCount = Integer.parseInt(textField.getText());

                // If new maxFrameCount is less than current maxFrameCount,
                // warn user as it will delete the keyframes included in the frames deleted.
                if(maxFrameCount < animDataStorage.maxFrameCount){
                    canSetMaxFrame = warnUser("Are you sure? If you proceed, you may delete keyframes!",
                            "Setting Max Frame");
                }

                if(!canSetMaxFrame) {
                    textField.setText(String.valueOf(animDataStorage.maxFrameCount));
                }
                else {
                    frameSlider.setMaximum(maxFrameCount); //sets the maximum limit of frame slider
                    animDataStorage.maxFrameCount = maxFrameCount + 1;
                    resetKeyframeArraySize();
                }

            }
            catch (NumberFormatException ex) {
                textField.setText(String.valueOf(animDataStorage.maxFrameCount));
            }
        });

        return textField;
    }
    //endregion

    private void resetKeyframeArraySize(){
        ArrayList<ArrayList<Keyframe>> newKeyFrames = parentLayer.currentLayer.keyframes;

        if(newKeyFrames.getFirst().size() > animDataStorage.maxFrameCount){
            removeKeyFrames(newKeyFrames);
        }
        else if(newKeyFrames.getFirst().size() < animDataStorage.maxFrameCount){
            addChannels(newKeyFrames);
        }
    }

    private void addChannels(ArrayList<ArrayList<Keyframe>> newKeyFrames){
        //add the new keyframes to each channel
        for(int channel = 0; channel < TransformChannels.values().length; channel++) {
            for (int i = newKeyFrames.get(channel).size(); i < animDataStorage.maxFrameCount; i++) {
                newKeyFrames.get(channel).add(new Keyframe());
                System.out.println(newKeyFrames.get(channel).size());
            }
        }
    }

    private void removeKeyFrames(ArrayList<ArrayList<Keyframe>> newKeyFrames){
        if (!(newKeyFrames.getFirst().size() > animDataStorage.maxFrameCount + 1)) {
            return;
        }

        for (int channel = 0; channel < TransformChannels.values().length; channel++) {

            for (int aKeyFrame = animDataStorage.maxFrameCount; aKeyFrame < newKeyFrames.get(channel).size(); aKeyFrame++) {

                removeSpecificKeyframe(newKeyFrames.get(channel), channel, aKeyFrame);

            }
        }

        keyframeSelection(parentLayer.currentLayer.keyframes.get(currentChannel), currentChannel);
    }

    private boolean warnUser(String prompt, String windowName){

        int choice = JOptionPane.showOptionDialog(
                null, // Parent component (null means center on screen)
                prompt, // Message to display
                windowName, // Dialog title
                JOptionPane.YES_NO_OPTION, // Option type (Yes, No, Cancel)
                JOptionPane.QUESTION_MESSAGE, // Message type (question icon)
                null, // Custom icon (null means no custom icon)
                yesNoOptions, // Custom options array
                yesNoOptions[1] // Initial selection (default is "Cancel")
        );

        return choice == JOptionPane.YES_OPTION;

    }

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

            parentLayer.repaint();

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
