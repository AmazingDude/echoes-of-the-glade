package main;

import inputs.KeyboardInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {
    private float xDelta = 100, yDelta = 100;

    private BufferedImage img, subImg;

    // Constructor
    public GamePanel() {
        addKeyListener(new KeyboardInput(this));
        setPanelSize();
        importImg();
    }

    // A method for importing images (sprites) to our gamePanel
    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/player.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    // sets the Dimensions of the gamePanel
    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    // Changes x-position
    public void changeXDelta(int pos) {
        this.xDelta += pos;
    }

    public void changeYDelta(int pos) {
        this.yDelta += pos;
    }

    public void setPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
    }
    // A method that we never call, but it gets called whenever the game starts

    // We will use paintComponent method for JPanel and we will pass Graphics object as input
    // Graphics allows us to draw

    // The paintComponent method is a method provided by the JPanel class. It is automatically invoked whenever the panel needs to be redrawn, which happens when the window is resized, uncovered
    public void paintComponent(Graphics g) {
        // super keyword is used here to call the paintComponent of GamePanel's super class i-e JPanel. So basically we are calling JPanel's paintComponent method.
        // Calling this method is important because it clears the previous form so there won't be any visual artifacts
        super.paintComponent(g); // Ensures proper rendering setup

        // Okay so we're creating sub-images from the main sprite atlas
        // Since each sprite is 48 x 48 pixels in dimension, we can choose any one of them by multiplying the position or number with it's dimensions.
        subImg = img.getSubimage(1 * 48, 2 * 48, 48, 48);
        // Draws the sprite image
        g.drawImage(subImg, (int) xDelta, (int) yDelta, 96, 96, null);

//        repaint(); // Re-render
    }
}
