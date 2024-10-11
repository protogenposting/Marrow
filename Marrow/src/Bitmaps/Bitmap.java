package Bitmaps;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Bitmaps contain a bunch of pixels.
 */
public class Bitmap {
    public ArrayList<ArrayList<Pixel>> bitmap = new ArrayList<>();

    public Bitmap() {}

    public Bitmap(BufferedImage image) {

        int temp;
        int alpha;
        int red;
        int green;
        int blue;
        Pixel pixel;
        setSize(image.getWidth(), image.getHeight());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                temp = image.getRGB(x, y);
                alpha = (temp & 0xFF000000) >>> 24;
                red = (temp   & 0x00FF0000) >> 16;
                green = (temp & 0x0000FF00) >> 8;
                blue = temp & 0x000000FF;

                if((red > 0 || green > 0 || blue > 0) || alpha > 0){
                    System.out.println("has added non transparent pixel");
                }

                pixel = new Pixel(new RGBColor(red, green, blue, alpha));
                addPixel(x, y, pixel);
            }
        }
    }

    /**
     * sets the size of the bitmap
     * @param width width of the bitmap
     * @param height height of the bitmap
     */
    public void setSize(int width, int height){
        for (int x = 0; x < width; x++) {
            bitmap.add(new ArrayList<>());
            for (int y = 0; y < height; y++) {
                bitmap.get(x).add(new Pixel(0, 0, 0, 0));
            }
        }
    }

    /**
     * adds / sets a pixel at the specified coordinate
     * @param x the x coordinate
     * @param y the y coordinate
     * @param pixel the pixel being added / set
     * @return true if pixel could be added / set, false if out of bounds
     */
    public boolean addPixel(int x, int y, Pixel pixel){
        if(!isInBounds(x,y)) {
            return false;
        }
        bitmap.get(x).set(y,pixel);
        return true;
    }

    public boolean removePixel(int x, int y){
        if(!isInBounds(x,y)) {
            return false;
        }
        bitmap.get(x).set(y,new Pixel(0,0,0,0));
        return true;
    }

    public boolean pixelAt(int x, int y) {
        if(!isInBounds(x,y)) {
            return false;
        }
        return bitmap.get(x).get(y).alpha>0;
    }

    public Pixel getPixelAt(int x, int y){
        return bitmap.get(x).get(y);
    }

    public boolean pixelColorMatches(int x, int y,Pixel pixel){
        if(!isInBounds(x,y)) {
            return false;
        }
        return pixel.equals(getPixelAt(x,y));
    }

    public boolean isInBounds(int x, int y){
        return x >= 0 && y >= 0 && x < bitmap.size() && y < bitmap.getFirst().size();
    }

    public BufferedImage toImage(){
        BufferedImage image = new BufferedImage(
                bitmap.size(),
                bitmap.getFirst().size(),
                BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if(getPixelAt(x,y).alpha > 0) {
                    image.setRGB(x, y, getPixelAt(x, y).toColor().getRGB());
                }
            }
        }

        return image;
    }
}