package Animation;

import javax.swing.*;

public class Timeline extends JPanel {

    AnimationDataStorage animDataStorage = new AnimationDataStorage();

    public Timeline(AnimationDataStorage animDataStorage)
    {
        this.animDataStorage = animDataStorage;
    }
}
