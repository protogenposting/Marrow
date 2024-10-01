package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;

public class DragTool extends Tool {

    /**
     * draws / sets an unfilled circle at the mouse cursor
     * @param xCenter the x-position of the center of the unfilled circle
     * @param yCenter the y-position of the center of the unfilled circle
     * @param bitmap where the unfilled circle is being drawn / set to
     */
    protected void drawCircle(int xCenter, int yCenter, Bitmap bitmap, int drawSize){
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
    protected void drawLine(int x1, int x2, int y, Bitmap bitmap){
        if(x1 > x2){
            int tempX1 = x1;
            x1 = x2;
            x2 = tempX1;
        }

        for (int i = 0; i < Math.abs(x1 - x2); i++) { //draw a line in the distance provided
            bitmap.addPixel(x1 + i, y, new Pixel(currentColor));
        }
    }
}
