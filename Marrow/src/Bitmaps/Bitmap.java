package Bitmaps;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Bitmaps contain a bunch of pixels.
 */
public class Bitmap {
    public ArrayList<ArrayList<Pixel>> bitmap = new ArrayList<>();

    public void setSize(int width, int height) {
        for (int x = 0; x < width; x++) {
            bitmap.add(new ArrayList<>());
            for (int y = 0; y < height; y++) {
                bitmap.get(x).add(new Pixel(0,0,0,0));
            }
        }
    }

    public boolean addPixel(int x, int y, Pixel pixel)
    {
        if(x<0||y<0||x>bitmap.size()||y>bitmap.getFirst().size()) {
            System.out.println("OUT OF BOUNDS: "+x+","+y);
            return false;
        }
        bitmap.get(x).set(y,new Pixel(0,0,0,0));
        return true;
    }
}