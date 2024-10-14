package Animation;

import Layers.Layer;

/**
 * Keyframes are used to save the state of a certain layer's properties
 */
public class Keyframe {
    public Layer targetLayer;
    public Transform2D transformModifier = new Transform2D();

    Transform2D.KeyframableValue value = Transform2D.KeyframableValue.x;

    public EaseType easing = EaseType.SINE;

    public double valueBetweenPoints(double a, double b,double percentage)
    {
        double smaller;

        if(a < b)
        {
            smaller = a;
        }
        else if(a > b)
        {
            smaller = b;
        }
        else {
            return a;
        }

        double distance = Math.abs(a-b);

        return smaller + distance * percentage;
    }
}
