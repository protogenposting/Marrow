package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;

import java.util.ArrayList;

class CheckPoint
{
    int x;
    int y;
    Bitmap bitmap;
    RGBColor currentColor;
    RGBColor startingColor;
    CheckPoint(int x, int y, Bitmap bitmap, RGBColor currentColor,RGBColor startingColor)
    {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.currentColor = currentColor;
        this.startingColor = startingColor;
    }
    public ArrayList<CheckPoint> updateNearbyPoints()
    {
        ArrayList<CheckPoint> list = new ArrayList<>();
        for(int xDir = -1; xDir < 2; xDir++) {
            for(int yDir = -1; yDir < 2; yDir++) {
                if(xDir==yDir||xDir==-yDir)
                {
                    continue;
                }
                if (
                        bitmap.isInBounds(x + xDir, y + yDir)&&
                        bitmap.pixelColorMatches(x + xDir, y + yDir, new Pixel(startingColor))
                )
                {
                    bitmap.addPixel(x + xDir, y + yDir, new Pixel(currentColor));
                    list.add(new CheckPoint(x + xDir, y + yDir, bitmap, currentColor, startingColor));
                }
            }
        }
        return list;
    }
}

public class Bucket extends Tool {
    public void onPress(int x, int y, Bitmap bitmap) {
        RGBColor startColor = bitmap.getPixelAt(x, y).toRGBColor();

        ArrayList<CheckPoint> pixels = new ArrayList<>();

        pixels.add(new CheckPoint(x, y, bitmap, currentColor, startColor));

        while (!pixels.isEmpty()) {
            ArrayList<CheckPoint> newPixels = new ArrayList<>();

            for (int i = 0; i < pixels.size(); i++) {
                ArrayList<CheckPoint> tempPixels = pixels.get(i).updateNearbyPoints();

                newPixels.addAll(tempPixels);
            }

            pixels = newPixels;
        }
    }
}