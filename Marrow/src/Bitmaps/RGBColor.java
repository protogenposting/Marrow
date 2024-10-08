package Bitmaps;

public class RGBColor {
    public int red;
    public int green;
    public int blue;
    public int alpha;
    public RGBColor(int red, int green, int blue, int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    public boolean equals(RGBColor color)
    {
        return red == color.red&&
               green == color.green&&
               blue == color.blue&&
               alpha == color.alpha;
    }
}
