package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Tools.Brushes.Brush;

import java.util.Random;

public class Paintbrush extends DragTool{

    /**
     * Paints a new pixel wherever the cursor is moved as long as the user is holding down the mouse
     * @param x1 x position from last frame
     * @param y1 y position from last frame
     * @param x2 x position from current frame
     * @param y2 y position from current frame
     * @param bitmap the bitmap to add to
     */

    public Brush brush;

    public int lastX = 0;

    public int lastY = 0;

    public void onDrag(int x1, int y1, int x2, int y2, Bitmap bitmap)
    {
        int width = Math.abs(x1-x2);
        int height = Math.abs(y1-y2);

        int signX = (int)Math.signum(x2-x1);
        int signY = (int)Math.signum(y2-y1);

        double theta = Math.atan(((double)y2-y1)/(x2-x1));

        int distance = 1;

        if(brush!=null) {
            distance = brush.distance;
        }

        if(width>Math.abs(y1-y2)) {
            int xProgress = signX;

            for(int j = 0; j < width; j += distance) {

                double tan = (Math.tan(theta) * xProgress);
                int yResult = (int)Math.round(tan);

                if(brush == null) {
                    drawCircle(xProgress + x1, yResult + y1, bitmap, drawSize);
                }
                else {
                    System.out.println(Math.abs(Math.sqrt(Math.pow(x1 - lastX,2) + Math.pow(y1 - lastY,2))));
                    if(distance <= Math.abs(Math.sqrt(Math.pow(x1 - lastX,2) + Math.pow(y1 - lastY,2)))) {
                        int selectedMap = new Random().nextInt(0, brush.brushMap.length);
                        drawBitmap(xProgress + x1, yResult + y1, brush.brushMap[selectedMap], bitmap);

                        lastX = x1;

                        lastY = y1;
                    }
                }

                xProgress += signX * distance;
            }
        }
        else {
            int yProgress = signY;

            for(int j = 0; j < height; j += distance) {
                int xResult = (int)Math.round(yProgress/Math.tan(theta));

                if(brush == null) {
                    drawCircle(xResult + x1, yProgress + y1, bitmap, drawSize);
                }
                else {
                    if(distance <= Math.abs(Math.sqrt(Math.pow(x1 - lastX,2) + Math.pow(y1 - lastY,2)))) {
                        int selectedMap = new Random().nextInt(0, brush.brushMap.length);
                        drawBitmap(xResult + x1, yProgress + y1, brush.brushMap[selectedMap], bitmap);

                        lastX = x1;

                        lastY = y1;
                    }
                }

                yProgress += signY * distance;
            }
        }
    }
}
