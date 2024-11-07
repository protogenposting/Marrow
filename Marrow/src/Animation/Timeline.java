package Animation;

import Layers.ParentLayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Timeline extends JSplitPane {

    private final AnimationDataStorage animDataStorage;
    private final ParentLayer parentLayer;
    private final JButton playButton = new JButton("Play");
    private final JButton animModeButton = new JButton("Start Animating");

    public final JSlider frameSlider = setUpSlider();
    private final JLabel currentFrameLabel = new JLabel("Current Frame:");
    private final JLabel maxFrameCountLabel = new JLabel("Max Frame Count:");
    private final JLabel fpsLabel = new JLabel("Frames per Second:");

    private final JTextField maxFrameCount = setUpMaxFrameCount(new JTextField("120", 6));
    private final JTextField framesPerSecond = setUpFramesPerSecond(new JTextField("24", 4));
    public final JTextField currentFrameTextField = setUpCurrentFrame(new JTextField("0", 6));

    private final JPanel channelPanel = new JPanel();
    private JScrollPane channelScrollPanel;
    private JScrollPane keyframeScrollPanel = new JScrollPane();
    private final JPanel totalKeyframePanel = new JPanel();
    private final JPanel keyframePanel = new JPanel();
    private final JPanel keyframeValuePanel = new JPanel();

    private final JSplitPane propertiesArea = new JSplitPane();

    private int currentChannel = 0;

    public Timeline(AnimationDataStorage animDataStorage, ParentLayer parentLayer)
    {

        setSplitPanes();

        this.setPreferredSize(new Dimension(800, 300));

        this.parentLayer = parentLayer;
        this.animDataStorage = animDataStorage;

        //yes the components are added in this specific order for a reason

        addComponents();
    }

    /**
     * Sets up the split pane values for the GUI.
     */
    private void setSplitPanes(){
        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.setDividerLocation(200);
        this.setLeftComponent(channelScrollPanel);
        this.setRightComponent(propertiesArea);

        propertiesArea.setOrientation(JSplitPane.VERTICAL_SPLIT);
        propertiesArea.setDividerLocation(40);

        JLabel info = new JLabel("   Set the current channel!");
        info.setVisible(true);

        propertiesArea.setBottomComponent(info);
    }

    /**
     * Adds all the immediately necessary GUIs to the Timeline object.
     * The line a GUI is added determines its position on the timeline.
     */
    private void addComponents(){

        playButton.addActionListener(e -> playOrPause());
        animModeButton.addActionListener(e -> animateOnOrOff());

        playButton.setVisible(true);
        animModeButton.setVisible(true);

        JPanel properties = new JPanel();
        properties.add(fpsLabel);
        properties.add(framesPerSecond);

        properties.add(maxFrameCountLabel);
        properties.add(maxFrameCount);

        properties.add(playButton);
        properties.add(animModeButton);

        properties.add(frameSlider);
        properties.add(currentFrameLabel);
        properties.add(currentFrameTextField);

        propertiesArea.setTopComponent(properties);
    }

    public void addChannels(){

        if(channelScrollPanel == null){
            setUpChannelPanel();
            return;
        }
        channelScrollPanel.add(new JButton( String.valueOf( animDataStorage.currentFrame )));

    }

    private void setUpChannelPanel(){

        channelScrollPanel = new JScrollPane();
        channelScrollPanel.setPreferredSize(new Dimension(100, 200));

        channelScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        channelPanel.setLayout(new BoxLayout(channelPanel, BoxLayout.Y_AXIS));

        //region add channel buttons

        TransformChannels channelName;

        for(int channel = 0; channel < TransformChannels.values().length; channel++){

            channelName = TransformChannels.values()[channel];
            channelPanel.add(createChannelButton(channelName.name(), channel));
        }

        channelPanel.setVisible(true);

        channelScrollPanel.add(channelPanel);
        channelScrollPanel.setVisible(true);
        channelScrollPanel.setViewportView(channelPanel);

        this.setLeftComponent(channelScrollPanel);
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

        // "repaints" all the keyframe stuff if this function has been called more than once
        if(totalKeyframePanel.isVisible()){
            totalKeyframePanel.removeAll();
            keyframeScrollPanel.removeAll();
            keyframePanel.removeAll();
            totalKeyframePanel.setVisible(false);
        }

        totalKeyframePanel.setLayout(new FlowLayout());

        keyframeScrollPanel = new JScrollPane();
        keyframeScrollPanel.setPreferredSize(new Dimension(530, 48));
        keyframeScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        //region add and remove keyframe buttons
        JButton addKeyFrameButton = new JButton("Add Keyframe");
        JButton removeKeyFrameButton = new JButton("Remove Keyframe");

        addKeyFrameButton.addActionListener(e -> addSpecificKeyFrame(keyframe, channelID));
        removeKeyFrameButton.addActionListener(e -> removeSpecificKeyframe(keyframe, channelID, animDataStorage.currentFrame));

        totalKeyframePanel.add(addKeyFrameButton);
        totalKeyframePanel.add(removeKeyFrameButton);
        //endregion

        TransformChannels[] channels = TransformChannels.values();
        TransformChannels channel = channels[channelID];

        totalKeyframePanel.add(new JLabel("   Channel: " + channel.name() + ", Keyframes: "));

        //adds a button for every active keyframe to the scroll panel
        for (int i = 0; i < keyframe.size(); i++) {
            if(!keyframe.get(i).isActive){
                continue;
            }

            JButton keyframeButton = new JButton(String.valueOf(i));
            int finalI = i;
            keyframeButton.addActionListener(e -> editKeyframeValue(channelID, finalI, String.valueOf(finalI)));

            keyframePanel.add(keyframeButton);
        }

        keyframePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        totalKeyframePanel.setVisible(true);
        keyframeScrollPanel.setVisible(true);
        keyframeScrollPanel.setViewportView(keyframePanel);
        totalKeyframePanel.add(keyframeScrollPanel);

        propertiesArea.setBottomComponent(totalKeyframePanel);
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

        JLabel keyframeValue = new JLabel("   Keyframe " + name + " Value: ");
        JTextField keyframeValueTextField = new JTextField(String.valueOf(currentValue), 4);
        keyframeValueTextField.setMaximumSize(new Dimension(150, 20));

        keyframeValueTextField.addActionListener(e -> {
            String userInput = keyframeValueTextField.getText();

            parentLayer.currentLayer.keyframes.get(channelID).get(keyframeID).value = Double.parseDouble(userInput);

            parentLayer.repaint();
        });

        keyframeValuePanel.setLayout(new BoxLayout(keyframeValuePanel, BoxLayout.X_AXIS));

        keyframeValuePanel.add(keyframeValue);
        keyframeValuePanel.add(keyframeValueTextField);
        keyframeValuePanel.setVisible(true);

        EaseType[] choices = EaseType.values();

        JComboBox<EaseType> dropdown = new JComboBox<>(choices);

        dropdown.addActionListener(e -> {
            parentLayer.currentLayer.keyframes.get(channelID).get(keyframeID).easing = (EaseType) dropdown.getSelectedItem();
            parentLayer.repaint();
        });

        dropdown.setMaximumSize(new Dimension(100, 20));
        dropdown.setSelectedItem(parentLayer.currentLayer.keyframes.get(channelID).get(keyframeID).easing);

        keyframeValuePanel.add(new JLabel(" Easing: "));
        keyframeValuePanel.add(dropdown);
        totalKeyframePanel.add(keyframeValuePanel);

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

        Object[] yesNoOptions = {"Yes", "No"};

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

        slider.addChangeListener(e -> {

            animDataStorage.currentFrame = slider.getValue();

            parentLayer.repaint();

            String frameValue = String.valueOf(slider.getValue());
            currentFrameTextField.setText(frameValue);
        });

        return slider;
    }

    public void playOrPause(){
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
