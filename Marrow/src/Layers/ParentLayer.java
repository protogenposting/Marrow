package Layers;

import Bitmaps.*;
import Tools.ToolContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * The layer on top of the layer hierarchy. Contains all other layers and draws them on a canvas.
 * Responsible for drawing everything.
 */
public class ParentLayer extends Layer {
    JFrame frame;
    Graphics2D graphics;
    Image image;
    ToolContainer toolContainer;
    public ChildLayer currentLayer;
    public ParentLayer(JFrame frame, ToolContainer toolContainer)
    {
        this.frame = frame;
        this.toolContainer = toolContainer;
    }

    public void addChild(ChildLayer layer)
    {
        if(children.isEmpty())
        {
            currentLayer = layer;
        }
        children.add(layer);
        onAddChild.accept(1);
        frame.getContentPane().add(layer);
        layer.setOpaque(false);
        layer.parent = this;
        layer.setSize(1366,768);
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

        clear();
        //RENDERING
        for(int i = 0; i < children.size(); i++)
        {
            ChildLayer child = children.get(i);
            
            if(currentLayer.equals(child))
            {
                child.isCurrentLayer = true;
            }

            if(child.getClass().equals(BitmapLayer.class))
            {
                //draw lines from the bitmap
                BitmapLayer bitmapChild = (BitmapLayer)children.get(i);
                Bitmap bitmap = bitmapChild.bitmap;
                ArrayList<ArrayList<Pixel>> map = bitmap.bitmap;
                graphics.drawImage(bitmapChild.drawnImage,0,0,this);
            }
        }

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
