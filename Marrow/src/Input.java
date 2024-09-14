import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Input {
    private JFrame frame;

    private ArrayList<Integer> keysPressed = new ArrayList<>();

    KeyAdapter adapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            int index = keysPressed.indexOf(e.getKeyCode());
            if(index == -1)
            {
                keysPressed.add(e.getKeyCode());
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int index = keysPressed.indexOf(e.getKeyCode());
            if(index > -1)
            {
                keysPressed.remove(index);
            }
        }
    };

    public Input(JFrame frame)
    {
        this.frame = frame;
        frame.addKeyListener(adapter);
    }

    public boolean getKeyHeld(int keyID)
    {
        int index = keysPressed.indexOf(keyID);

        //System.out.println("huh");

        return index > -1;
    }
}
