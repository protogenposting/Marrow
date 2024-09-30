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

    /**
     * this function adds a child
     * @param layer the layer to add as a child
     */
    public void addChild(ChildLayer layer)
    {
        //if we have no child then the current layer is the one we just added
        if(children.isEmpty())
        {
            setChildTo(layer);
        }
        children.add(layer);
        //run the function that runs on child add (used for the layer organizer)
        onAddChild.accept(layer);
        layer.setOpaque(false);
        layer.parent = this;
        layer.setSize(1366,768);
    }

    protected void paintComponent(Graphics g) {
        //if the image is null then make some graphics and stuff
        if(image==null)
        {
            //we create an image here
            image = createImage(getSize().width,getSize().height);
            graphics = (Graphics2D) image.getGraphics();
            //enable antialiasing
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            //reset the image
            clear();
        }

        graphics.setColor(Color.black);
        //clear the image again
        clear();
        //RENDERING
        for(int i = 0; i < children.size(); i++)
        {
            ChildLayer child = children.get(i);

            if(child.getClass().equals(BitmapLayer.class))
            {
                //draw the bitmap layer's image :3
                BitmapLayer bitmapChild = (BitmapLayer)child;
                graphics.drawImage(bitmapChild.drawnImage,0,0,this);
                //System.out.println(child.isCurrentLayer);
            }
        }

        g.drawImage(image,0,0,null);
    }

    /**
     * clears the entire window with white
     */
    public void clear()
    {
        graphics.setPaint(Color.WHITE);
        //draw white on the entire draw area
        graphics.fillRect(0,0, getSize().width, getSize().height);
        graphics.setPaint(Color.black);
        repaint();
    }

    /**
     * use this to get the image of the layer, which is basically all the layers combined
     * @return returns the image
     */
    public Image getImage()
    {
        return image;
    }

    public void setChildTo(ChildLayer layer) {
        currentLayer = layer;
        for(int i = 0; i < children.size(); i++)
        {
            ChildLayer child = children.get(i);

            child.isCurrentLayer = currentLayer == child;

            frame.getContentPane().remove(child);
        }
        layer.requestFocus();
        layer.isCurrentLayer = true;
        frame.getContentPane().add(layer);
    }
}
