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
    //mouse cooridnates
    private int currentX, currentY, oldX, oldY;

    public BitmapLayer() {
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
                //draw some lines
                //graphics.drawLine(oldX, oldY, currentX, currentY);
                oldX = currentX;
                oldY = currentY;
            }
        });
    }
}