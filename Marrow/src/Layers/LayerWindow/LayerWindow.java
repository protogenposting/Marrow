package Layers.LayerWindow;

import Animation.Timeline;
import Layers.BitmapLayer;
import Layers.Layer;
import Layers.ParentLayer;
import Tools.ToolContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class LayerWindow extends JPanel {
    Layer parentLayer;
    Timeline timeline;

    public LayerWindow(ParentLayer parentLayer, ToolContainer toolContainer, Timeline timeline)
    {
        this.timeline = timeline;
        this.setVisible(true);
        this.setSize(256,768);
        this.parentLayer = parentLayer;
        this.setLayout(new FlowLayout());

        JPanel panel = new JPanel();

        JButton layerAdding = getjButton(parentLayer, toolContainer);
        add(layerAdding);

        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setViewportView(panel);

        scrollPane.setPreferredSize(new Dimension(200,300));

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        parentLayer.onAddChild = (a) -> {
            LayerButton layerButton = new LayerButton(a.name);

            layerButton.layer = a;

            layerButton.parentLayer = parentLayer;

            panel.add(layerButton);

            timeline.addChannels();
        };

        this.add(scrollPane);
    }

    private JButton getjButton(ParentLayer parentLayer, ToolContainer toolContainer) {
        JButton layerAdding = new JButton("Add Layer");

        layerAdding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentLayer.addChild(new BitmapLayer(toolContainer,"Big Gay Layer " + new Random().nextInt()));
                revalidate();
                repaint();
            }
        });
        return layerAdding;
    }
}
