package Layers;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * This is where the user draws images as well as rendering images
 * we will have to separate this into a rendering and an image component later
 */
public class BitmapLayer extends Layer {

    //image being drawn
    private Image image;
    //a silly billy graphics2D object
    private Graphics2D graphics;
    //mouse coordinates
    private int currentX, currentY, oldX, oldY;
    public BitmapLayer(){
        setDoubleBuffered(false);
        //listener for mouse being pressed.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //save the old coords
                oldX = e.getX();
                oldY = e.getY();
            }
        });
        //listened for mouse movement
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();

                if(graphics!=null)
                {
                    //draw some lines
                    graphics.drawLine(oldX,oldY,currentX,currentY);
                    //refresh
                    repaint();
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });
    }

    @Override
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
