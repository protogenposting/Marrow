package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;

public class Paintbrush extends Tool{

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
     * @param x1 x position from last frame
     * @param y1 y position from last frame
     * @param x2 x position from current frame
     * @param y2 y position from current frame
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

        if(width>Math.abs(y1-y2)) {
            int xProgress = signX;

            for(int j = 0; j < width; j++) {

                double tan = (Math.tan(theta) * xProgress);
                int yResult = (int)Math.round(tan);

                if(drawSize == 1){
                    bitmap.addPixel(xProgress + x1,yResult + y1, new Pixel(currentColor));
                }
                else {
                    drawCircle(xProgress + x1, yResult + y1, bitmap, drawSize);
                }

                xProgress += signX;
            }
        }
        else {
            int yProgress = signY;

            for(int j = 0; j < height; j++) {
                int xResult = (int)Math.round(yProgress/Math.tan(theta));

                if(drawSize == 1){
                    bitmap.addPixel(xResult + x1,yProgress + y1, new Pixel(currentColor));
                }
                else {
                    drawCircle(xResult + x1, yProgress + y1, bitmap, drawSize);
                }

                yProgress += signY;
            }
        }
    }

    /**
     * draws / sets an unfilled circle at the mouse cursor
     * @param xCenter the x-position of the center of the unfilled circle
     * @param yCenter the y-position of the center of the unfilled circle
     * @param bitmap where the unfilled circle is being drawn / set to
     */
    private void drawCircle(int xCenter, int yCenter, Bitmap bitmap, int drawSize){
        int radius = (drawSize) / 2;
        int x = radius, y = 0;

        int P = 1 - radius;

        if (radius > 0){
            drawLine((x + xCenter), (-x + xCenter), (yCenter), bitmap);
            bitmap.addPixel((x + xCenter), (-y + yCenter), new Pixel(currentColor));
            bitmap.addPixel((y + xCenter), (x + yCenter), new Pixel(currentColor));
            bitmap.addPixel((-y + xCenter), (x + yCenter), new Pixel(currentColor));
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

            drawLine((x + xCenter), (-x + xCenter), (y + yCenter), bitmap);
            drawLine((x + xCenter), (-x + xCenter), (-y + yCenter), bitmap);

            if(x != y){
                drawLine((y + xCenter), (-y + xCenter), (x + yCenter), bitmap);
                drawLine((y + xCenter), (-y + xCenter), (-x + yCenter), bitmap);
            }
        }

    }

    /**
     * draws a horizontal line between two points
     * @param x1 x position of first point
     * @param x2 x position of second point
     * @param y y position of both points
     * @param bitmap where the line is being drawn on
     */
    private void drawLine(int x1, int x2, int y, Bitmap bitmap){
        for (int i = 0; i < Math.abs(x1 - x2); i++) { //draw a line in the distance provided
            bitmap.addPixel(x1 + i, y, new Pixel(currentColor));
        }
    }
}
