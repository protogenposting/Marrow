package Layers;

import Bitmaps.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * This layer should always be on top of the hierarchy. It contains all other layers and draws them on a canvas. It should be responsible for drawing everything.
 */
public class ParentLayer extends Layer {
    JFrame frame;
    Graphics2D graphics;
    Image image;
    public ParentLayer(JFrame frame)
    {
        this.frame = frame;
    }

    public void addChild(BitmapLayer layer)
    {
        children.add(layer);
        frame.getContentPane().add(layer);
        layer.setOpaque(false);
        layer.parent = this;
    }
    protected void paintComponent(Graphics g) {
        if(image==null)
        {
            //we create an image here
            image = createImage(getSize().width,getSize().height);
            graphics = (Graphics2D) image.getGraphics();
            //enable antialiasing
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }

        graphics.setColor(Color.black);

        for(int i = 0; i < children.size(); i++)
        {
            Layer child = children.get(i);
            if(child.getClass().equals(BitmapLayer.class))
            {
                //draw lines from the bitmap
                BitmapLayer bitmapChild = (BitmapLayer)children.get(i);
                Bitmap bitmap = bitmapChild.bitmap;
                ArrayList<ArrayList<Pixel>> map = bitmap.bitmap;
                for(int xPos = 0; xPos < map.size(); xPos++)
                {
                    for(int yPos = 0; yPos < map.get(xPos).size(); yPos++)
                    {
                        Pixel pixel = map.get(xPos).get(yPos);
                        if(pixel.alpha>0) {
                            graphics.fillRect(xPos, yPos, 1, 1);
                        }
                    }
                }
            }
        }
        long time2 = Calendar.getInstance().getTimeInMillis();

        g.drawImage(image,0,0,null);
    }

    public void clear()
    {
        graphics.setPaint(Color.WHITE);
        //draw white on the entire draw area
        graphics.fillRect(0,0,getSize().width,getSize().height);
        graphics.setPaint(Color.black);
        repaint();
    }

    public Image getImage()
    {
        return image;
    }
}
