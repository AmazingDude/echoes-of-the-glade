package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel() {

    }

    // A method that we never call but it gets called whenever the game starts

    // We will use paintComponent method for JPanel and we will pass Graphics object as input
    // Graphics allows us to draw

    // The paintComponent method is a method provided by the JPanel class. It is automatically invoked whenever the panel needs to be redrawn, which happens when the window is resized, uncovered
    public void paintComponent(Graphics g) {
        // super keyword is used here to call the paintComponent of GamePanel's super class i-e JPanel. So basically we are calling JPanel's paintComponent method.
        // Calling this method is important because it clears the previous form so there won't be any visual artifacts
        super.paintComponent(g); // Ensures proper rendering setup
    }
}
