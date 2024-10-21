package Layers;

import Animation.AnimationDataStorage;
import Animation.Keyframe;
import Animation.Transform2D;
import Animation.TransformChannels;
import Main.Main;
import Tools.ToolContainer;

import javax.swing.*;
import javax.xml.crypto.dsig.Transform;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * The layer on top of the layer hierarchy. Contains all other layers and draws them on a canvas.
 * Responsible for drawing everything.
 */
public class ParentLayer extends Layer {

    Graphics2D graphics;
    Image image;
    ToolContainer toolContainer;
    AnimationDataStorage animDataStorage = new AnimationDataStorage();
    public ChildLayer currentLayer;


    public ParentLayer(ToolContainer toolContainer, AnimationDataStorage animDataStorage)
    {
        this.animDataStorage = animDataStorage;
        this.toolContainer = toolContainer;
    }

    /**
     * this function adds a child
     * @param layer the layer to add as a child
     */
    public void addChild(ChildLayer layer)
    {
        //if we have no child then the current layer is the one we just added
        children.add(layer);
        //run the function that runs on child add (used for the layer organizer)
        onAddChild.accept(layer);
        layer.setOpaque(false);
        layer.parent = this;
        layer.setSize(getWidth(),getHeight());
        if(layer instanceof BitmapLayer)
        {
            ((BitmapLayer) layer).bitmap.setSize(getWidth(),getHeight());
            System.out.println(getWidth());
            System.out.println(getHeight());
        }
        if(children.isEmpty()) {
            setChildTo(layer);
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

        loopThroughChildren(children, (child) -> {


            if (child.getClass().equals(BitmapLayer.class)) {
                //draw the bitmap layer's image :3
                BitmapLayer bitmapChild = (BitmapLayer) child;
                AffineTransform currentTransform = new AffineTransform();
                //selection rectangle
                if (bitmapChild == currentLayer) {
                    graphics.drawRect(
                            (int) bitmapChild.transform.x,
                            (int) bitmapChild.transform.y,
                            (int) (bitmapChild.transform.x + bitmapChild.getWidth() * bitmapChild.transform.scaleX),
                            (int) (bitmapChild.transform.y + bitmapChild.getHeight() * bitmapChild.transform.scaleY)
                    );
                }

                // this section deals with actually animating!

                if(animDataStorage.isInAnimateMode) {
                    int channelID = 0;
                    for (TransformChannels channel : TransformChannels.values()) {
                        int currentFrame = animDataStorage.currentFrame;

                        ArrayList<Keyframe> keyframes = bitmapChild.keyframes.get(channelID);

                        int[] lastKeyframes = new int[bitmapChild.keyframes.size()];

                        Arrays.fill(lastKeyframes, -1);

                        int[] nextKeyframes = new int[bitmapChild.keyframes.size()];

                        Arrays.fill(nextKeyframes, -1);

                        int lastKeyframe = lastKeyframes[channelID];
                        int nextKeyframe = nextKeyframes[channelID];

                        //get next and last keyframes
                        for (int keyframe = 0; keyframe < keyframes.size(); keyframe++) {
                            Keyframe currentKeyframe = keyframes.get(keyframe);

                            if (!currentKeyframe.isActive) {
                                continue;
                            }

                            if (keyframe <= currentFrame) {
                                lastKeyframe = keyframe;
                            }

                            if (keyframe > currentFrame && nextKeyframe == -1) {
                                nextKeyframe = keyframe;
                            }
                        }

                        if (lastKeyframe == -1) {
                            channelID++;
                            continue;
                        }

                        double value;

                        Keyframe last = keyframes.get(lastKeyframe);

                        if(nextKeyframe == -1)
                        {
                            value = last.value;
                        }
                        else
                        {
                            Keyframe next = keyframes.get(nextKeyframe);

                            int distance = nextKeyframe - lastKeyframe;

                            double percent = (double) (currentFrame - lastKeyframe) / distance;

                            value = Keyframe.valueBetweenPoints(last.value, next.value, percent, last.easing);
                        }

                        switch (channel) {
                            case TransformChannels.x:
                                currentTransform.setToTranslation(value, currentTransform.getTranslateY());
                                break;
                            case TransformChannels.y:
                                currentTransform.setToTranslation(currentTransform.getTranslateX(), value);
                                break;
                            case TransformChannels.scaleX:
                                currentTransform.setToScale(value,currentTransform.getScaleY());
                                break;
                            case TransformChannels.scaleY:
                                currentTransform.setToScale(currentTransform.getScaleX(),value);
                                break;
                            case TransformChannels.rotation:
                                double width = bitmapChild.transform.rotationCenterX * bitmapChild.getWidth();
                                double height = bitmapChild.transform.rotationCenterY * bitmapChild.getHeight();
                                currentTransform.setToRotation(Math.toRadians(value), width, height);
                                break;
                            case TransformChannels.shearX:
                                currentTransform.setToShear(value,currentTransform.getShearY());
                                break;
                            case TransformChannels.shearY:
                                currentTransform.setToShear(currentTransform.getShearX(),value);
                                break;
                            case TransformChannels.opacity:
                                //OH GOD IT ISN'T IN THE AFFINE TRANSFORM HELP-
                                break;
                        }
                        channelID++;
                    }
                }

                //draw the image with the transform
                graphics.drawImage(bitmapChild.drawnImage, currentTransform, this);
            }
        });
        
        g.drawImage(image,0,0,null);
    }

    private void loopThroughChildren(ArrayList<ChildLayer> childrenArray, Consumer<ChildLayer> importedFunction){
        ChildLayer child;
        for(int chuldNum = 0; chuldNum < childrenArray.size(); chuldNum++) {
            child = childrenArray.get(chuldNum);

            if (!child.getChildren().isEmpty()){
                loopThroughChildren(child.getChildren(), importedFunction);
            }

            importedFunction.accept(child);

        }
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
