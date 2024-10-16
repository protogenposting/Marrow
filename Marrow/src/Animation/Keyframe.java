package Animation;

import Layers.Layer;

/**
 * Keyframes are used to save the state of a certain layer's properties
 */
public class Keyframe {
    public double value = 0;

    public boolean isActive = false;

    public EaseType easing = EaseType.NONE;

    public Keyframe()
    {

    }

    public Keyframe(boolean isActive)
    {
        this.isActive = isActive;
    }

    /**
     * allows you to get the value between 2 other values in percentage.
     * @param a value 1
     * @param b value 2
     * @param percentage the amount between the 2 values
     * @return the value between the 2
     */
    public static double valueBetweenPoints(double a, double b,double percentage,EaseType easing)
    {
        switch(easing)
        {
            case EaseType.SINE:
                percentage = (Math.sin(Math.PI * percentage - Math.PI/2) + 1)/2;
                break;
            case EaseType.SINEIN:
                percentage = Math.sin(Math.PI * percentage/2);
                break;
            case EaseType.SINEOUT:
                percentage = Math.sin(Math.PI * (percentage + 3)/2) + 1;
                break;
        }
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

        double distance = b-a;

        return a + distance * percentage;
    }
}
