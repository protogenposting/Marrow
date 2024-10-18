package Animation;

/**
 * Keyframes are used to save the state of a certain property.
 * They are arranged in a layer's different channels according to the property of the channel.
 */
public class Keyframe {
    public double value = 0; // note: "0" eventually gets replaced by a value from TransformChannels

    public boolean isActive = false;

    public EaseType easing = EaseType.NONE;

    public Keyframe()
    {

    }

    /**
     *
     * @param isActive determines if a keyframe actually affects the animation and is viewable
     */
    public Keyframe(boolean isActive)
    {
        this.isActive = isActive;
    }

    /**
     * allows you to get the value between 2 other values in percentage.
     * @param a value 1
     * @param b value 2
     * @param percentage the amount between the 2 values
     * @param easing the easing type of the value. Use <a href = https://www.desmos.com/calculator>desmos<a> if you want to view any of these formulas
     * @return the value between the 2
     */
    public static double valueBetweenPoints(double a, double b,double percentage,EaseType easing) {

        //change the percentage based on the easing type. If it's NONE do nothing lol
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

        double distance = b-a;

        return a + distance * percentage;
    }
}
