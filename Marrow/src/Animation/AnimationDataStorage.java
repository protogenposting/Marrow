package Animation;

import Layers.ParentLayer;

import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Animation Data Storage lets us transfer data between anything that uses animation.
 */
public class AnimationDataStorage {
    public int currentFrame = 0;
    public int framesPerSecond = 24;
    public boolean isInAnimateMode = false;
    public boolean isPlaying = false;
    public ParentLayer parentLayer;
    public Timeline timeline;
    public int maxFrameCount = 120;
    Timer timer = new Timer();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public AnimationDataStorage() {
        runAnimation();
    }


    public void runAnimation(){
        Runnable yourRunnable = () -> {
            if(isPlaying)
            {
                currentFrame ++;

                timeline.currentFrameTextField.setText(String.valueOf(currentFrame));
                timeline.frameSlider.setValue(currentFrame);

                parentLayer.repaint();

                if(currentFrame >= maxFrameCount)
                {
                    currentFrame = 0;
                }
            }
        };
        int initialDelay = 0;
        int delay = 1000/framesPerSecond;
        scheduler.scheduleWithFixedDelay(yourRunnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    public void resetFramesPerSecond(){
        scheduler.shutdown();
        runAnimation();
    }

    /**
     * set size lets you set the length of your animation in frames.
     *
     * @param size the length in frames.
     */
    public void setSize(int size) {
        /*keyframes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            keyframes.add(new Keyframe());
        }*/
    }
}