package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;

public class Eraser extends Tool{
    int size = 1;
    public void onPress(int x, int y, Bitmap bitmap)
    {
        bitmap.removePixel(x,y);
    }
    public void onDrag(int x1, int y1, int x2, int y2, Bitmap bitmap)
    {
        bitmap.removePixel(x1,y1);

        bitmap.removePixel(x2,y2);

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
                bitmap.removePixel(xProgress + x1,yResult + y1);
                xProgress += signX;
            }
        }
        else
        {
            int yProgress = signY;

            for(int i = 0; i < height; i++)
            {
                int xResult = (int)Math.round(yProgress/Math.tan(theta));
                bitmap.removePixel(xResult + x1,yProgress + y1);
                yProgress += signY;
            }
        }
    }
}
