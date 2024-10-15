package Animation;

import Layers.ParentLayer;

import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Animation Data Storage lets us transfer data between anything that uses animation.<br>
 * I want to krill myself this code is so ass
 */
public class AnimationDataStorage {
    public int currentFrame = 0;
    public int framesPerSecond = 24;
    public boolean isInAnimateMode = false;
    public boolean isPlaying = false;
    public ParentLayer parentLayer;
    Timer timer = new Timer();

    public AnimationDataStorage() {
        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(isPlaying)
                    {
                        currentFrame ++;
                    }
                }
            },
            1000/framesPerSecond);
    }

    /**
     * set size lets you set the length of your animation in frames.
     *
     * @param size the length in frames.
     */
    public void setSize(int size) {
        keyframes = new ArrayList<>();9
        for (int i = 0; i < size; i++) {
            keyframes.add(new Keyframe());
        }
    }
}