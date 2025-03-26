package main;

// JFrame is a collection of components used for making GUI
// We'll use JFrame for making our game window.

import javax.swing.*;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame();

        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null);
        jframe.pack(); // will fit the window size to the preferred size of components but the only component we have is GamePanel so it will fit the window to it's preferred size.
//        jframe.setResizable(false);
        jframe.setVisible(true);
    }
}
