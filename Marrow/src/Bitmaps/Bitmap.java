package Bitmaps;

import java.util.LinkedList;

/**
 * Bitmaps contain a bunch of pixels.
 */
public class Bitmap {
    public LinkedList<LinkedList<Pixel>> bitmap = new LinkedList<>();

    public void setSize(int width, int height) {
        for (int x = 0; x < width; x++) {
            bitmap.add(new LinkedList<>());
            for (int y = 0; y < height; y++) {
                bitmap.get(x).add(new Pixel(0,0,0,100));
            }
        }
    }

    public boolean addPixel(int x, int y, Pixel pixel)
    {
        if(x<0||y<0||x>bitmap.size()||y>bitmap.getFirst().size()) {
            return false;
        }
        bitmap.get(x).set(y,new Pixel(0,0,0,0));
        return true;
    }
}