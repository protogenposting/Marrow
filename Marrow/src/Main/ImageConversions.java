package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageConversions {
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    public static void SaveImage(Image image,String location)
    {
        BufferedImage newImage = (BufferedImage) image;

        File outputfile = new File(location);

        try {
            ImageIO.write(newImage,"png",outputfile);
        } catch (IOException | IllegalArgumentException t) {
            writeTransparentImage(outputfile);
        }

        System.out.println("saved image");
    }

    private static void writeTransparentImage(File outputfile){
        try {
            Files.copy(new File("IconImages/Empty/TransparentBG.png").toPath(), outputfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //ImageIO.write(image, "png", outputfile);
        }
        catch (IOException ignore){
            ignore.printStackTrace();
        }
    }
}
