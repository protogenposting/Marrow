package Tools;

import Bitmaps.Bitmap;
import Bitmaps.Pixel;
import Bitmaps.RGBColor;
import Layers.BitmapLayer;

import javax.swing.*;

public class LineTool extends Tool {

    public LineTool(){}

    public RGBColor currentColor = new RGBColor(0,0,0,255);

    int xStart;
    int yStart;

    /**
     * Sets xStart and yStart
     * @param x starting x position
     * @param y starting y position
     * @param bitmap the bitmap to add to
     */
    public void onPress(int x, int y, Bitmap bitmap)
    {
        xStart = x;
        yStart = y;
    }

    /**
     * Paints a line on the bitmap from the start position, gathered from onPress function, to end position, which is
     *              where the user releases the mouse cursor
     * @param x1 the starting x position
     * @param y1 the starting y position
     * @param x2 the ending x position
     * @param y2 the ending y position
     * @param bitmap the bitmap to add to
     */
    public void onRelease(int x1, int y1, int x2, int y2, Bitmap bitmap){
        x1 = xStart;
        y1 = yStart;
        bitmap.addPixel(x1,y1,new Pixel(currentColor));
        bitmap.addPixel(x2,y2,new Pixel(currentColor));

        int width = Math.abs(x1-x2);

        int height = Math.abs(y1-y2);

        int signX = (int)Math.signum(x2-x1);

        int signY = (int)Math.signum(y2-y1);

        double theta = Math.atan(((double)y2-y1)/(x2-x1));

        if(width>height)
        {
            int xProgress = signX;

            for(int i = 0; i < width; i++)
            {
                double tan = Math.tan(theta)*xProgress;
                int yResult = (int)Math.round(tan);
                bitmap.addPixel(xProgress + x1,yResult + y1,new Pixel(currentColor));
                xProgress += signX;
            }
        }
        else
        {
            int yProgress = signY;

            for(int i = 0; i < height; i++)
            {
                int xResult = (int)Math.round(yProgress/Math.tan(theta));
                bitmap.addPixel(xResult + x1,yProgress + y1,new Pixel(currentColor));
                yProgress += signY;
            }
        }

    }



}

/*
    private static class Line{
        final int x1;
        final int y1;
        final int x2;
        final int y2;
        final Color color;

        public Line(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }

    private final LinkedList<Line> lines = new LinkedList<Line>();

    public void addLine(int x1, int x2, int x3, int x4) {
        addLine(x1, x2, x3, x4, Color.black);
    }

    public void addLine(int x1, int x2, int x3, int x4, Color color) {
        lines.add(new Line(x1,x2,x3,x4, color));
        repaint();
    }

    public void clearLines() {
        lines.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Line line : lines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final LinesComponent comp = new LinesComponent();
        comp.setPreferredSize(new Dimension(320, 200));
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        JButton newLineButton = new JButton("New Line");
        JButton clearButton = new JButton("Clear");
        buttonsPanel.add(newLineButton);
        buttonsPanel.add(clearButton);
        testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        newLineButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int x1 = (int) (Math.random()*320);
                int x2 = (int) (Math.random()*320);
                int y1 = (int) (Math.random()*200);
                int y2 = (int) (Math.random()*200);
                Color randomColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
                comp.addLine(x1, y1, x2, y2, randomColor);
            }
        });
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                comp.clearLines();
            }
        });
        testFrame.pack();
        testFrame.setVisible(true);
    }

     */