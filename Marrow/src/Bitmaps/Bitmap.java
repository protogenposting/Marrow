package Bitmaps;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Bitmaps contain a bunch of pixels.
 */
public class Bitmap {
    public ArrayList<ArrayList<Pixel>> bitmap = new ArrayList<>();

    public void setSize(int width, int height) {
        for (int x = 0; x < width; x++) {
            bitmap.add(new ArrayList<>());
            for (int y = 0; y < height; y++) {
                Random random = new Random();
                bitmap.get(x).add(new Pixel(0, 0, 0, 0));
            }
        }
    }

    public boolean addPixel(int x, int y, Pixel pixel)
    {
        if(x<0||y<0||x>bitmap.size()||y>bitmap.getFirst().size()) {
            return false;
        }
        bitmap.get(x).set(y,pixel);
        return true;
    }
}