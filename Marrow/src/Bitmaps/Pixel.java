package Bitmaps;

/**
 * Pixels are used to make up a bitmap.
 */
public class Pixel {
    public int red;
    public int green;
    public int blue;
    public int alpha;

    public Pixel(int red, int green, int blue, int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
}