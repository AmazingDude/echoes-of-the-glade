package entities;

import utils.Constants;
import utils.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.Direction.*;
import static utils.Constants.PlayerState.*;
import static utils.HelperMethods.CanMoveHere;
import static utils.Constants.getAnimationRow;
import static utils.Constants.getSpriteAmount;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int animTick, animIndex, animSpeed = 15; // animSpeed = FPS / number of animations per second?
    private Constants.PlayerState playerAction = IDLE;
    private Constants.Direction playerDirection = LEFT;
    private boolean moving = false;
    private boolean up, right, down, left;
    private boolean attacking = false;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePos();
        updateHitBox();
    }

    public void render(Graphics g) {
        if (playerDirection == LEFT)
            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) x + 96, (int) y, -96, 96, null);
        else
            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) x, (int) y, 96, 96, null);
        drawHitBox(g);
    }


    private void updateAnimationTick() {

        animTick++;

        if (animTick >= animSpeed) {
            animTick = 0; // Resets the tick
            animIndex++;
            if (animIndex >= getSpriteAmount(playerAction)) {
                animIndex = 0; // Resets the index
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        Constants.PlayerState startAnim = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if (attacking) {
            playerAction = ATTACKING;
        }
        // If there is a change in the player animation then reset the Animation tick and index
        if (startAnim != playerAction)
            resetAnimTick();
    }

    private void resetAnimTick() {
        animTick = 0;
        animIndex = 0;
    }

    private void updatePos() {
        moving = false;

        int dx = 0, dy = 0;

        if (!left && !right && !up && !down)
            return;

        if (left && !right) {
//            x -= playerSpeed;
            dx = -2;
//            moving = true;
            playerDirection = LEFT;

        } else if (right && !left) {
//            x += playerSpeed;
            dx = 2;
//            moving = true;
            playerDirection = RIGHT;
        }

        if (up && !down) {
//            y -= playerSpeed;
            dy = -2;
//            moving = true;
            playerDirection = UP;

        } else if (down && !up) {
//            y += playerSpeed;
            dy = 2;
//            moving = true;
            playerDirection = DOWN;
        }



        if (dy != 0 || dx != 0) {
            if (CanMoveHere(x + dx, y + dy, width, height, lvlData)) {
                double length = Math.sqrt(dx * dx + dy * dy);
                this.x += (playerSpeed * dx / length);
                this.y += (playerSpeed * dy / length);
                moving = true;
            }
        }
    }

    private void loadAnimations() {

        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[6][10];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(i * 48, j * 48 , 48, 48);
            }
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void resetDirBooleans() {
        up = false;
        right = false;
        down = false;
        left = false;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

}
