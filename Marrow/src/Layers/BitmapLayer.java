package Layers;

import Animation.EaseType;
import Animation.Keyframe;
import Animation.TransformChannels;
import Bitmaps.Bitmap;
import Bitmaps.RGBColor;
import Tools.Tool;
import Tools.ToolContainer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This is where the user draws images as well as rendering images
 * we will have to separate this into a rendering and an image component later
 */
public class BitmapLayer extends ChildLayer {
    //mouse coordinates
    ToolContainer toolContainer;

    private int currentX, currentY, oldX, oldY;

    public Bitmap bitmap;

    public RGBColor currentColor = new RGBColor(0, 0, 0, 255);

    public BufferedImage drawnImage;

    public BitmapLayer(ToolContainer toolContainer, String name) {
        this(toolContainer, name, new Bitmap());

        //TEST CODE FOR KEYFRAMES

        Keyframe frame1 = new Keyframe(true);

        frame1.value = 0;

        Keyframe frame2 = new Keyframe(true);

        frame2.value = 100;

        Keyframe frame3 = new Keyframe(true);

        frame3.value = 0;

        setSize(120);

        // note: index out of bounds occurs because "keyframes" has 128 keyframes, and each keyframe has 7 channels
        // to fix, probably change frame1-3 into Arraylists
        //TIGER DON'T DO THAT NO TIGER YOU'RE GONNA BREAK MY SYSTEM

        bitmap.setSize(800, 400);
    }

    public BitmapLayer(ToolContainer toolContainer, String name, Bitmap bitmap) {
        setDoubleBuffered(false);
        this.toolContainer = toolContainer;

        this.name = name;

        this.bitmap = bitmap;
        if (!this.bitmap.bitmap.isEmpty()) {
            drawnImage = bitmap.toImage();
        }

        //this.bitmap.setSize(800,400);
    }

    public void mousePressed(MouseEvent e) {
        //save the old coords
        oldX = e.getX();
        oldY = e.getY();
        if (isCurrentLayer) {
            Tool currentTool = toolContainer.currentTool;
            currentTool.onPress(oldX, oldY, bitmap);
        }
        System.out.println(toolContainer.currentTool.toString());
        drawnImage = bitmap.toImage();
        parent.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        currentX = e.getX();
        currentY = e.getY();
        if (isCurrentLayer) {
            Tool currentTool = toolContainer.currentTool;
            currentTool.onRelease(oldX, oldY, currentX, currentY, bitmap);
        }
        drawnImage = bitmap.toImage();
        parent.repaint();
    }

    public void mouseDragged(MouseEvent e) {
        currentX = e.getX();
        currentY = e.getY();
        //draw some lines
        if (isCurrentLayer) {
            Tool currentTool = toolContainer.currentTool;
            currentTool.onDrag(oldX, oldY, currentX, currentY, bitmap);
        }
        oldX = currentX;
        oldY = currentY;
        drawnImage = bitmap.toImage();
        parent.repaint();
    }

    public void setSize(int size) {
        keyframes = new ArrayList<>();
        for (TransformChannels channel : TransformChannels.values()) {
            ArrayList<Keyframe> list2 = new ArrayList<>();
            keyframes.add(list2);
            //size is added by 1 so the user doesn't accidentally go out of bounds when setting frame
            for (int i = 0; i < size + 1; i++) {
                list2.add(new Keyframe());
            }
        }
    }
}