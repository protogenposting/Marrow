package Animation;

import java.util.ArrayList;

/**
 * Animation Data Storage lets us transfer data between anything that uses animation.
 */
public class AnimationDataStorage {
    public int currentFrame = 0;
    public boolean isInAnimateMode = false;
    public boolean isPlaying = false;
    public int fps = 24;
    public ArrayList<Keyframe> keyframes;

    /**
     * set size lets you set the length of your animation in frames.
     * @param size the length in frames.
     */
    public void setSize(int size)
    {
        keyframes = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            keyframes.add(new Keyframe());
        }
    }
}
