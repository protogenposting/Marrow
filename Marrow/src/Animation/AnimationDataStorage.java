package Animation;

import java.util.ArrayList;

public class AnimationDataStorage {
    public int currentFrame = 0;
    public boolean isInAnimateMode = false;
    public boolean isPlaying = false;
    public int keyFrameID = 0;
    public ArrayList<Keyframe> keyframes = new ArrayList<>();
}
