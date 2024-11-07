package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;

public class Eraser extends Tool{
    public void onPress(int x, int y, Bitmap bitmap)
    {
        removeCircle(x,y, bitmap, drawSize);
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

                removeCircle(xProgress + x1, yResult + y1, bitmap, drawSize);
                xProgress += signX;
            }
        }
        else
        {
            int yProgress = signY;

            for(int i = 0; i < height; i++)
            {
                int xResult = (int)Math.round(yProgress/Math.tan(theta));

                removeCircle(xResult + x1,yProgress + y1, bitmap, drawSize);
                yProgress += signY;
            }
        }
    }

    /**
     * removes a circle at the mouse cursor
     * @param xCenter the x-position of the center of the unfilled circle
     * @param yCenter the y-position of the center of the unfilled circle
     * @param bitmap where the unfilled circle is being drawn / set to
     */
    protected void removeCircle(int xCenter, int yCenter, Bitmap bitmap, int drawSize){
        int radius = (drawSize) / 2;
        int x = radius, y = 0;

        int P = 1 - radius;

        if (drawSize == 1){
            bitmap.removePixel(xCenter,yCenter);
            return;
        }

        if (radius > 0){
            removeLine((x + xCenter), (-x + xCenter), (yCenter), bitmap);
            bitmap.removePixel((x + xCenter), (-y + yCenter));
            bitmap.removePixel((y + xCenter), (x + yCenter));
            bitmap.removePixel((-y + xCenter), (x + yCenter));
        }

        while (x > y){
            y++;

            if(P <= 0){
                P = P + 2 * y + 1;
            }
            else{
                x--;
                P = P + 2 * y - 2 * x + 1;
            }

            if(x < y){
                break;
            }

            removeLine((x + xCenter), (-x + xCenter), (y + yCenter), bitmap);
            removeLine((x + xCenter), (-x + xCenter), (-y + yCenter), bitmap);

            if(x != y){
                removeLine((y + xCenter), (-y + xCenter), (x + yCenter), bitmap);
                removeLine((y + xCenter), (-y + xCenter), (-x + yCenter), bitmap);
            }
        }

    }

    /**
     * removes a horizontal line between two points
     * @param x1 x position of first point
     * @param x2 x position of second point
     * @param y y position of both points
     * @param bitmap where the line is being drawn on
     */
    protected void removeLine(int x1, int x2, int y, Bitmap bitmap){
        if(x1 > x2){
            int tempX1 = x1;
            x1 = x2;
            x2 = tempX1;
        }

        for (int i = 0; i < Math.abs(x1 - x2); i++) { //draw a line in the distance provided
            bitmap.removePixel(x1 + i, y);
        }
    }

}
