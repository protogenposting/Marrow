package Layers;

import javax.swing.*;
import java.util.ArrayList;

public class LayerWindow extends JFrame {
    ArrayList<Layer> layers;

    public LayerWindow(String name, ArrayList<Layer> layers)
    {
        this.setTitle(name);
        this.setVisible(true);
        this.setSize(256,768);
        this.setResizable(true);
        this.layers = layers;
    }
}
