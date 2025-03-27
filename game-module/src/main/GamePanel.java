package main;

import inputs.KeyboardInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utils.Constants.PlayerState.*;
import static utils.Constants.Direction.*;
import static utils.Constants.*;

public class GamePanel extends JPanel {
    private float xDelta = 100, yDelta = 100;

    private BufferedImage img;
    private BufferedImage[][] animations;
    private int animTick, animIndex, animSpeed = 15; // animSpeed = FPS / number of animations per second?
    private PlayerState playerAction = IDLE;
    private Direction playerDirection = LEFT;
    private boolean moving = false;

    // Constructor
    public GamePanel() {
        addKeyListener(new KeyboardInput(this));
        setPanelSize();
        importImg();
        loadAnimations();
    }

    private void loadAnimations() {
        animations = new BufferedImage[6][10];

        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(i * 48, j * 48 , 48, 48);
            }
        }
    }

    // A method for importing images (sprites) to our gamePanel
    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/player.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // sets the Dimensions of the gamePanel
    private void setPanelSize() {
        Dimension size = new Dimension(1296, 720);
        setPreferredSize(size);
    }

    // Changes Direction
    public void setPlayerDirection(Direction playerDirection) {
        this.playerDirection = playerDirection;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updateAnimationTick() {

        animTick++;

        if (animTick >= animSpeed) {
            animTick = 0; // Resets the tick
            animIndex++;
            if (animIndex >= getSpriteAmount(playerAction)) {
                animIndex = 0; // Resets the index
            }
        }
    }

    private void setAnimation() {
        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
    }

    private void updatePos() {
        if (moving) {
            switch (playerDirection) {
                case DOWN:
                    yDelta += 2;
                    break;
                case RIGHT:
                    xDelta += 2;
                    break;
                case UP:
                    yDelta -= 2;
                    break;
                case LEFT:
                    xDelta -= 2;
                    break;
            }
        }
    }

    // A method that we never call, but it gets called whenever the game starts
    // We will use paintComponent method for JPanel and we will pass Graphics object as input
    // Graphics allows us to draw
    // The paintComponent method is a method provided by the JPanel class. It is automatically invoked whenever the panel needs to be redrawn, which happens when the window is resized, uncovered
    public void paintComponent(Graphics g) {
        // super keyword is used here to call the paintComponent of GamePanel's super class i-e JPanel. So basically we are calling JPanel's paintComponent method.
        // Calling this method is important because it clears the previous form so there won't be any visual artifacts
        super.paintComponent(g); // Ensures proper rendering setup
        setAnimation();
        updatePos();
        updateAnimationTick();
        // Draws the sprite image
        if (playerDirection == LEFT)
            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) xDelta + 96, (int) yDelta, -96, 96, null);
        else
            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) xDelta, (int) yDelta, 96, 96, null);
    }

}
