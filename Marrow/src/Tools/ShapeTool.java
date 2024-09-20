package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;

public class ShapeTool extends Tool {

    public RGBColor currentColor = new RGBColor(0,0,0,255);

    int xStart;
    int yStart;

    public void onPress(int x, int y, Bitmap bitmap)
    {
        xStart = x;
        yStart = y;
    }
    public void onRelease(int x1, int y1, int x2, int y2, Bitmap bitmap) {
        x1 = xStart;
        y1 = yStart;
        bitmap.addPixel(x1, y1, new Pixel(currentColor));
        bitmap.addPixel(x2, y2, new Pixel(currentColor));

        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);

        int signX = (int) Math.signum(x2 - x1);
        int signY = (int) Math.signum(y2 - y1);

        int xProgress = signX;
        for (int i = 0; i < width; i++) {
            bitmap.addPixel(xProgress + x1, y1, new Pixel(currentColor));
            xProgress += signX;
        }
        xProgress = -1 * signX;
        for (int i = 0; i < width; i++) {
            bitmap.addPixel(xProgress + x2, y2, new Pixel(currentColor));
            xProgress += -1 * signX;
        }

        int yProgress = signY;
        for (int i = 0; i < height; i++) {
            bitmap.addPixel(x1, yProgress + y1, new Pixel(currentColor));
            yProgress += signY;
        }
        yProgress = -1 * signY;
        for (int i = 0; i < height; i++) {
            bitmap.addPixel(x2, yProgress + y2, new Pixel(currentColor));
            yProgress += -1 * signY;
        }


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

}
