package Layers;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;
import Tools.Paintbrush;
import Tools.Tool;
import Tools.ToolContainer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * This is where the user draws images as well as rendering images
 * we will have to separate this into a rendering and an image component later
 */
public class BitmapLayer extends ChildLayer {

    //image being drawn
    private Image image;
    //a silly billy graphics2D object
    public Graphics2D graphics; //changing this to public so it can use drawLine
    //mouse coordinates
    ToolContainer toolContainer;

    private int currentX, currentY, oldX, oldY;

    public Bitmap bitmap = new Bitmap();

    public RGBColor currentColor = new RGBColor(0,0,0,100);

    public BitmapLayer(ToolContainer toolContainer) {
        setDoubleBuffered(false);
        this.toolContainer = toolContainer;

        bitmap.setSize(1366,768);
        //listener for mouse being pressed.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //save the old coords
                oldX = e.getX();
                oldY = e.getY();
                Tool currentTool = toolContainer.currentTool;
                currentTool.onPress(oldX,oldY,bitmap);
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
                parent.repaint();
            }
        });
    }
}