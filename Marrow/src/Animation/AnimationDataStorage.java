package Animation;

import java.util.ArrayList;

/**
 * Animation Data Storage lets us transfer data between anything that uses animation.<br>
 * I want to krill myself this code is so ass
 */
public class AnimationDataStorage {
    public int currentFrame = 0;
    public int framesPerSecond = 24;
    public boolean isInAnimateMode = false;
    public boolean isPlaying = false;
    public int fps = 24;
    public ArrayList<Keyframe> keyframes;
    public ArrayList<Boolean> channels = new ArrayList<>();

    public AnimationDataStorage() {
        for (int i = 0; i < Transform2D.TransformChannel.values().length; i++) {
            channels.add(false);
        }
    }

    /**
     * set size lets you set the length of your animation in frames.
     *
     * @param size the length in frames.
     */
    public void setSize(int size) {
        keyframes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            keyframes.add(new Keyframe());
        }
    }
}