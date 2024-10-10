package Animation;

import Layers.Layer;

/**
 * Keyframes are used to save the state of a certain layer's properties
 */
public class Keyframe {
    public Layer targetLayer;
    public Transform2D transformModifier = new Transform2D();

    public EaseType easing = EaseType.SINE;

    public Transform2D getInBetweenTransform(double percentage)
    {
        percentage = Math.clamp(percentage,0,1);
        switch(easing)
        {
            case EaseType.SINE:
                percentage = Math.sin((percentage/2)*Math.PI);
            default:
                break;
        }

        Transform2D layerTransform = targetLayer.transform;

        return layerTransform;
    }
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
