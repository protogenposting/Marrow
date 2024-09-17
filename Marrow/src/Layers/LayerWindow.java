package Layers;

import javax.swing.*;

public class LayerWindow extends JFrame {
    public LayerWindow(String name)
    {
        this.setTitle(name);
        this.setVisible(true);
        this.setSize(256,768);
        this.setResizable(true);
    }
}
