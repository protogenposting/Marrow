package Layers;

import Bitmaps.Bitmap;
import Bitmaps.RGBColor;
import Tools.Tool;
import Tools.ToolContainer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

/**
 * This is where the user draws images as well as rendering images
 * we will have to separate this into a rendering and an image component later
 */
public class BitmapLayer extends ChildLayer {
    //mouse coordinates
    ToolContainer toolContainer;

    private int currentX, currentY, oldX, oldY;

    public Bitmap bitmap;

    public RGBColor currentColor = new RGBColor(0,0,0,255);

    public BufferedImage drawnImage;

    public BitmapLayer(ToolContainer toolContainer,String name) {
        this(toolContainer,name,new Bitmap());
        bitmap.setSize(1366,768);
        drawnImage = bitmap.toImage();
    }
    public BitmapLayer(ToolContainer toolContainer,String name, Bitmap bitmap) {
        setDoubleBuffered(false);
        this.toolContainer = toolContainer;

        this.name = name;

        this.bitmap = bitmap;

        //listener for mouse being pressed.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //save the old coords
                oldX = e.getX();
                oldY = e.getY();
                if(isCurrentLayer) {
                    Tool currentTool = toolContainer.currentTool;
                    currentTool.onPress(oldX, oldY, bitmap);
                }
                System.out.println(toolContainer.currentTool.toString());
                drawnImage = bitmap.toImage();
                parent.repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                if(isCurrentLayer) {
                    Tool currentTool = toolContainer.currentTool;
                    currentTool.onRelease(oldX, oldY, currentX, currentY, bitmap);
                }
                drawnImage = bitmap.toImage();
                parent.repaint();
            }
        });
        //listened for mouse movement
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                //draw some lines
                if(isCurrentLayer) {
                    Tool currentTool = toolContainer.currentTool;
                    currentTool.onDrag(oldX,oldY,currentX,currentY,bitmap);
                }
                oldX = currentX;
                oldY = currentY;
                drawnImage = bitmap.toImage();
                parent.repaint();
            }
        });
    }
}