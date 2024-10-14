package Animation;

public class Transform2D {
    public double x = 0;
    public double y = 0;
    public double scaleX = 1;
    public double scaleY = 1;
    public double rotation = 0;
    public double rotationCenterX = 0;
    public double rotationCenterY = 0;
    public double shear = 0;

    /**
     * this will let us select a value for a keyframe to affect
     */
    public static enum KeyframeChannel {
        x,
        y,
        scaleX,
        scaleY,
        rotation,
        shear,
    }
}
