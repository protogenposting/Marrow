package Layers;

import Main.Main;
import Tools.ToolContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;

/**
 * The layer on top of the layer hierarchy. Contains all other layers and draws them on a canvas.
 * Responsible for drawing everything.
 */
public class ParentLayer extends Layer {

    Graphics2D graphics;
    Image image;
    ToolContainer toolContainer;
    public ChildLayer currentLayer;
    public int keyFrameID;

    public ParentLayer(ToolContainer toolContainer, int keyFrameID)
    {
        this.keyFrameID = keyFrameID;
        this.toolContainer = toolContainer;
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Parent layer clicked");
                if(currentLayer != null) {
                    if(currentLayer instanceof BitmapLayer) {
                        BitmapLayer bitmapLayer =  (BitmapLayer) currentLayer;
                        bitmapLayer.mousePressed(e);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(currentLayer != null) {
                    if(currentLayer instanceof BitmapLayer) {
                        BitmapLayer bitmapLayer =  (BitmapLayer) currentLayer;
                        bitmapLayer.mouseReleased(e);
                    }
                }
            }
        } );

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentLayer != null) {
                    if (currentLayer instanceof BitmapLayer) {
                        BitmapLayer bitmapLayer = (BitmapLayer) currentLayer;
                        bitmapLayer.mouseDragged(e);
                    }
                }
            }

        });
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
        layer.setSize(getWidth(),getHeight());
        if(layer instanceof BitmapLayer)
        {
            ((BitmapLayer) layer).bitmap.setSize(getWidth(),getHeight());
        }
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
                AffineTransform currentTransform = new AffineTransform();

                //currentTransform.scale(1,0.1);

                if(bitmapChild == currentLayer) {
                    graphics.drawRect(
                            (int) bitmapChild.transform.x,
                            (int) bitmapChild.transform.y,
                            (int) (bitmapChild.transform.x + bitmapChild.getWidth() * bitmapChild.transform.scaleX),
                            (int) (bitmapChild.transform.y + bitmapChild.getHeight() * bitmapChild.transform.scaleY)
                    );
                }


                graphics.drawImage(bitmapChild.drawnImage, currentTransform, this);
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

    /**
     * changes the current selected layer.
     * @param layer the layer that will activate
     */
    public void setChildTo(ChildLayer layer) {
        currentLayer = layer;
        for(int i = 0; i < children.size(); i++)
        {
            ChildLayer child = children.get(i);

            child.isCurrentLayer = currentLayer == child;

            this.remove(child);
        }
        layer.isCurrentLayer = true;
        this.add(layer);
    }
}
