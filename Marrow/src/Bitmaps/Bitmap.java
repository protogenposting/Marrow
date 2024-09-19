package Bitmaps;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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
        if(!isInBounds(x,y)) {
            return false;
        }
        bitmap.get(x).set(y,pixel);
        return true;
    }
    public boolean removePixel(int x, int y)
    {
        if(!isInBounds(x,y)) {
            return false;
        }
        bitmap.get(x).set(y,new Pixel(0,0,0,0));
        return true;
    }
    public boolean pixelAt(int x, int y)
    {
        if(!isInBounds(x,y)) {
            return false;
        }
        return bitmap.get(x).get(y).alpha>0;
    }
    public Pixel getPixelAt(int x, int y)
    {
        return bitmap.get(x).get(y);
    }
    public boolean pixelColorMatches(int x, int y,Pixel pixel)
    {
        if(!isInBounds(x,y)) {
            return false;
        }
        return pixel.equals(getPixelAt(x,y));
    }
    public boolean isInBounds(int x, int y)
    {
        if(x<0||y<0||x>=bitmap.size()||y>=bitmap.getFirst().size()) {
            return false;
        }
        return true;
    }
    public Image toImage()
    {
        Image image = new BufferedImage(1,1,0);
        return image;
    }
}