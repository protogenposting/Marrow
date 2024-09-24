package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;

public class Paintbrush extends Tool{

    int size = 1;

    /**
     * Paints a new pixel on the screen at the click location
     * @param x starting x position
     * @param y starting y position
     * @param bitmap the bitmap to add to
     */
    public void onPress(int x, int y, Bitmap bitmap)
    {
        bitmap.addPixel(x,y,new Pixel(currentColor));
    }

    /**
     * Paints a new pixel wherever the cursor is moved as long as the user is holding down the mouse
     * @param x1 the starting x position
     * @param y1 the starting y position
     * @param x2 the ending x position
     * @param y2 the ending y position
     * @param bitmap the bitmap to add to
     */
    public void onDrag(int x1, int y1, int x2, int y2, Bitmap bitmap)
    {
        bitmap.addPixel(x1,y1,new Pixel(currentColor));

        bitmap.addPixel(x2,y2,new Pixel(currentColor));

        int width = Math.abs(x1-x2);

        int height = Math.abs(y1-y2);

        int signX = (int)Math.signum(x2-x1);

        int signY = (int)Math.signum(y2-y1);

        double theta = Math.atan(((double)y2-y1)/(x2-x1));

        if(width>Math.abs(y1-y2))
        {
            int xProgress = signX;

            for(int i = 0; i < width; i++)
            {
                double tan = Math.tan(theta)*xProgress;
                int yResult = (int)Math.round(tan);
                bitmap.addPixel(xProgress + x1,yResult + y1,new Pixel(currentColor));
                xProgress += signX;
            }
        }
        else
        {
            int yProgress = signY;

            for(int i = 0; i < height; i++)
            {
                int xResult = (int)Math.round(yProgress/Math.tan(theta));
                bitmap.addPixel(xResult + x1,yProgress + y1,new Pixel(currentColor));
                yProgress += signY;
            }
        }
    }
}
