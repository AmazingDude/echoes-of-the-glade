package main;

// JFrame is a collection of components used for making GUI
// We'll use JFrame for making our game window.

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame();

        jframe.setTitle("Echoes of the Glade");
        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.pack(); // will fit the window size to the preferred size of components but the only component we have is GamePanel so it will fit the window to it's preferred size.
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                gamePanel.requestFocusInWindow(); // Ensure GamePanel gets focus for keyboard input
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}
