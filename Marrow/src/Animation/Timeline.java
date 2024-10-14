package Animation;

import javax.swing.*;

public class Timeline extends JPanel {

    AnimationDataStorage animDataStorage = new AnimationDataStorage();
    JButton playButton = new JButton("Play");
    JButton animModeButton = new JButton("Animate");

    public Timeline(AnimationDataStorage animDataStorage)
    {
        this.animDataStorage = animDataStorage;
        playButton.addActionListener(e -> playOrPause());
        animModeButton.addActionListener(e -> animateOnOrOff());

        playButton.setVisible(true);
        animModeButton.setVisible(true);

        this.add(playButton);
        this.add(animModeButton);
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
