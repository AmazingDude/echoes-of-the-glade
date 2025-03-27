package inputs;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import utils.Constants;
import static utils.Constants.Direction.*;


// Keyboard input listener
public class KeyboardInput implements KeyListener {
    private GamePanel gamePanel;

    public KeyboardInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel; // Access to gamePanel
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
//      System.out.println("A key is pressed");
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.setPlayerDirection(UP);
                break;
            case KeyEvent.VK_A:
                gamePanel.setPlayerDirection(LEFT);
                break;
            case KeyEvent.VK_S:
                gamePanel.setPlayerDirection(DOWN);
                break;
            case KeyEvent.VK_D:
                gamePanel.setPlayerDirection(RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                gamePanel.setMoving(false);
            break;
        }
    }
}
