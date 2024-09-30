package Tools;

import Bitmaps.RGBColor;

import java.awt.*;

/**
 * this holds data that is used by tools, allows us to transfer stuff between the bitmap layer, tools, and the toolbox
 */
public class ToolContainer {
    public Tool currentTool = new Paintbrush();
    public RGBColor currentColor = new RGBColor(0,0,0,255);
}
