package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;

public class ShapeTool extends Tool {

    public RGBColor currentColor = new RGBColor(0,0,0,255);

    int xStart;
    int yStart;

    /**
     * Sets xStart and yStart
     * @param x starting x position
     * @param y starting y position
     * @param bitmap the bitmap to add to
     */
    public void onPress(int x, int y, Bitmap bitmap)
    {
        xStart = x;
        yStart = y;
    }

    /**
     * On release, calls the functions that paint a box as the user designed
     * @param x1 the starting x position
     * @param y1 the starting y position
     * @param x2 the ending x position
     * @param y2 the ending y position
     * @param bitmap the bitmap to add to
     */
    public void onRelease(int x1, int y1, int x2, int y2, Bitmap bitmap) {
        x1 = xStart;
        y1 = yStart;
        bitmap.addPixel(x1, y1, new Pixel(currentColor));
        bitmap.addPixel(x2, y2, new Pixel(currentColor));

        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);

        int signX = (int) Math.signum(x2 - x1);
        int signY = (int) Math.signum(y2 - y1);

        addXPixelsInALine(x1, y1, signX, 1, width, bitmap);
        addXPixelsInALine(x2, y2, signX, -1, width, bitmap);

        addYPixelsInALine(x1, y1, signY, 1, height, bitmap);
        addYPixelsInALine(x2, y2, signY, -1, height, bitmap);

        /*
        if (width > height) {
            int xProgress = signX;

            for (int i = 0; i < width; i++) {
                double tan = Math.tan(theta) * xProgress;
                int yResult = (int) Math.round(tan);
                bitmap.addPixel(xProgress + x1, yResult + y1, new Pixel(currentColor));
                xProgress += signX;
            }
        } else {
            int yProgress = signY;

            for (int i = 0; i < height; i++) {
                int xResult = (int) Math.round(yProgress / Math.tan(theta));
                bitmap.addPixel(xResult + x1, yProgress + y1, new Pixel(currentColor));
                yProgress += signY;
            }
        }
        */
    }


    /**
     * Paints a line along the X axis
     * @param x
     * @param y
     * @param xProgress
     * @param progressDirection
     * @param width
     * @param bitmap
     */
    public void addXPixelsInALine(int x, int y, int xProgress, int progressDirection, int width, Bitmap bitmap){
        int signX = xProgress;
        xProgress = xProgress * progressDirection;
        for(int i = 0; i < width; i++){
            bitmap.addPixel(x + xProgress, y, new Pixel(currentColor));
            xProgress += progressDirection * signX;
        }
    }

    /**
     * Paints a line along the Y axis
     * @param x
     * @param y
     * @param yProgress
     * @param progressDirection
     * @param height
     * @param bitmap
     */
    public void addYPixelsInALine(int x, int y, int yProgress, int progressDirection, int height, Bitmap bitmap){
        int signY = yProgress;
        yProgress = yProgress * progressDirection;
        for(int i = 0; i < height; i++){
            bitmap.addPixel(x, y + yProgress, new Pixel(currentColor));
            yProgress += progressDirection * signY;
        }
    }

}
