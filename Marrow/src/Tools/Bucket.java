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
    CheckPoint(int x, int y, Bitmap bitmap, RGBColor currentColor)
    {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.currentColor = currentColor;

    }
    public ArrayList<CheckPoint> updateNearbyPoints()
    {
        ArrayList<CheckPoint> list = new ArrayList<>();
        if(bitmap.isInBounds(x-1,y) && !bitmap.pixelAt(x-1,y))
        {
            bitmap.addPixel(x-1,y,new Pixel(currentColor));
            list.add(new CheckPoint(x-1,y,bitmap,currentColor));
        }
        if(bitmap.isInBounds(x+1,y) && !bitmap.pixelAt(x+1,y))
        {
            bitmap.addPixel(x+1,y,new Pixel(currentColor));
            list.add(new CheckPoint(x+1,y,bitmap,currentColor));
        }
        if(bitmap.isInBounds(x,y-1) && !bitmap.pixelAt(x,y-1))
        {
            bitmap.addPixel(x,y-1,new Pixel(currentColor));
            list.add(new CheckPoint(x,y-1,bitmap,currentColor));
        }
        if(bitmap.isInBounds(x,y+1) && !bitmap.pixelAt(x,y+1))
        {
            bitmap.addPixel(x,y+1,new Pixel(currentColor));
            list.add(new CheckPoint(x,y+1,bitmap,currentColor));
        }
        return list;
    }
}

public class Bucket extends Tool {
    public void onPress(int x, int y, Bitmap bitmap) {
        ArrayList<CheckPoint> pixels = new ArrayList<>();

        pixels.add(new CheckPoint(x, y, bitmap, currentColor));

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