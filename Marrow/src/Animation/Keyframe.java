package Animation;

import Layers.Layer;

/**
 * Keyframes are used to save the state of a certain layer's properties
 */
public class Keyframe {
    public Layer targetLayer;

    public Transform2D transformModifier = new Transform2D();

    public boolean isActive = false;

    public int channel = 0;

    public EaseType easing = EaseType.NONE;

    public Keyframe()
    {

    }

    public Keyframe(Transform2D transformModifier)
    {
        this.transformModifier = transformModifier;
    }

    /**
     * allows you to get the value between 2 other values in percentage.
     * @param a value 1
     * @param b value 2
     * @param percentage the amount between the 2 values
     * @return the value between the 2
     */
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
