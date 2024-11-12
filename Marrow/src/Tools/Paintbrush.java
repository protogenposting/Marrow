package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;

public class Paintbrush extends DragTool{

    /**
     * Paints a new pixel wherever the cursor is moved as long as the user is holding down the mouse
     * @param x1 x position from last frame
     * @param y1 y position from last frame
     * @param x2 x position from current frame
     * @param y2 y position from current frame
     * @param bitmap the bitmap to add to
     */

    public Bitmap brushMap = null;

    public void onDrag(int x1, int y1, int x2, int y2, Bitmap bitmap)
    {
        bitmap.addPixel(x1,y1,new Pixel(currentColor));
        bitmap.addPixel(x2,y2,new Pixel(currentColor));

        int width = Math.abs(x1-x2);
        int height = Math.abs(y1-y2);

        int signX = (int)Math.signum(x2-x1);
        int signY = (int)Math.signum(y2-y1);

        double theta = Math.atan(((double)y2-y1)/(x2-x1));

        if(width>Math.abs(y1-y2)) {
            int xProgress = signX;

            for(int j = 0; j < width; j++) {

                double tan = (Math.tan(theta) * xProgress);
                int yResult = (int)Math.round(tan);

                if(brushMap == null) {
                    drawCircle(xProgress + x1, yResult + y1, bitmap, drawSize);
                }
                else {
                    //this is when you draw using an image brush
                }

                xProgress += signX;
            }
        }
        else {
            int yProgress = signY;

            for(int j = 0; j < height; j++) {
                int xResult = (int)Math.round(yProgress/Math.tan(theta));

                if(brushMap == null) {
                    drawCircle(xResult + x1, yProgress + y1, bitmap, drawSize);
                }
                else {
                    //freaky :3
                }

                yProgress += signY;
            }
        }
    }
}
