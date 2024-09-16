package Layers;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;

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

    private boolean canBeDrawnOn = true;

    public Bitmap bitmap = new Bitmap();

    public Layer parent;

    public BitmapLayer() {
        setDoubleBuffered(false);

        bitmap.setSize(1366,768);
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
                if(canBeDrawnOn) {
                    int x1 = oldX;
                    int x2 = currentX;
                    int y1 = oldY;
                    int y2 = currentY;

                    int width = Math.abs(x1-x2);

                    int signX = (int)Math.signum(x1-x2);

                    int signY = (int)Math.signum(y1-y2);

                    double theta = Math.atan(((double)y1-y2)/(x1-x2));

                    if(width>Math.abs(y1-y2))
                    {
                        System.out.println("HORIZONTAL");
                        int xProgress = x1;

                        for(int i = 0; i < width; i++)
                        {
                            int yResult = (int)Math.round(Math.tan(theta)*xProgress);
                            bitmap.addPixel(xProgress,yResult,new Pixel(255,0,0,100));
                            xProgress += signX;
                        }
                    }
                    else
                    {
                        System.out.println("VERTICAL");
                        int yProgress = y1;

                        for(int i = 0; i < width; i++)
                        {
                            int xResult = (int)Math.round(yProgress/Math.tan(theta));
                            bitmap.addPixel(xResult,yProgress,new Pixel(0,0,0,100));
                            yProgress += signY;
                        }
                    }
                    parent.repaint();
                }
                oldX = currentX;
                oldY = currentY;
            }
        });
    }
}