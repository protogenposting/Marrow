import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        //the main frame we will be drawing on
        JFrame frame = new JFrame("Marrow");
        Container content = frame.getContentPane();

        //set layout
        content.setLayout(new BorderLayout());

        //create draw area
        BitmapLayer bitmapLayer = new BitmapLayer();

        //add the bitmap layer to the main window
        content.add(bitmapLayer, BorderLayout.CENTER);

        //controls
        JPanel controls = new JPanel();

        frame.setSize(600,600);

        frame.setResizable(true);

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}