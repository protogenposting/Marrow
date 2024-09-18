package Tools;

import Bitmaps.Bitmap;
import Bitmaps.RGBColor;

import java.awt.*;

/**
 * Tools are used to draw in different ways
 */
public class Tool {
    public RGBColor currentColor = new RGBColor(0,0,0,100);
    /**
     * call this when the mouse is clicked
     * @param x
     * @param y
     * @param bitmap
     */
    public void onPress(int x, int y, Bitmap bitmap)
    {

    }

    /**
     * call this when the mouse is dragged.
     * @param x1 the starting x position
     * @param y1 the starting y position
     * @param x2 the ending x position
     * @param y2 the ending y position
     * @param bitmap the bitmap to add to
     */
    public void onDrag(int x1, int y1, int x2, int y2, Bitmap bitmap)
    {

    }
}
