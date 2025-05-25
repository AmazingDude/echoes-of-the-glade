package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utils.Constants;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
    private boolean attackChecked = false;
    private float playerSpeed = 1.2f * Game.SCALE;
    private int[][] lvlData;
    private float xOffset = 25 * Game.SCALE;
    private float yOffset = 30 * Game.SCALE;

    private BufferedImage statBarImg;

    private int statBarWidth = (int) (63 * Game.SCALE);
    private int statBarHeight = (int) (14 * Game.SCALE);
    private int statBarX = (int) (10 * Game.SCALE);
    private int statBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (53 * Game.SCALE);
    private int healthBarHeight = (int) (6 * Game.SCALE);
    private int healthBarXStart = (int) (5 * Game.SCALE);
    private int healthBarYStart = (int) (4 * Game.SCALE);

    public static int maxHealth = 100;
    public static int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    private Rectangle2D.Float attackBox;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, (int) (14 * Game.SCALE), (int) (24 * Game.SCALE));
        initAttackBox();
        this.playing = playing;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (25 * Game.SCALE), (int) (25 * Game.SCALE));
    }

    private boolean isDying = false;

    public void update() {
        // Play or stop move sound based on moving state
        if (moving) {
            playing.getGame().getAudioPlayer().playMoveSound();
        } else {
            playing.getGame().getAudioPlayer().stopMoveSound();
        }
        if (currentHealth <= 0) {
            isDying = true;
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DEATH_SOUND);
            // Only set game over after death animation completes
            if (playerAction == DYING && animIndex >= getSpriteAmount(DYING) - 1) {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().playMusic(AudioPlayer.DEATH_MUSIC);
                return;
            }
        }

        updateHealthBar();
        if (!isDying) {
            // Check for enemy hits when player is attacking
            if (attacking && !attackChecked) {
                playing.getGame().getAudioPlayer().playAttackSound();
                playing.getEnemyHandler().checkEnemyHit(attackBox);
                attackChecked = true;
            }
            updatePos();
            updateAttackBox();
        }
        updateAnimationTick();
        setAnimation();
    }

    private void updateAttackBox() {
        // Horizontal attack direction
        if (right) {
            attackBox.x = hitBox.x + hitBox.width - (int) (4 * Game.SCALE);
            attackBox.y = hitBox.y + (6 * Game.SCALE);
        } else if (left) {
            attackBox.x = hitBox.x - hitBox.width - (int) (6 * Game.SCALE);
            attackBox.y = hitBox.y + (6 * Game.SCALE);
        }

        // Vertical attack direction
        if (up) {
            attackBox.y = hitBox.y - hitBox.height + (int) (19 * Game.SCALE);
            attackBox.x = hitBox.x - (5 * Game.SCALE);
        } else if (down) {
            attackBox.y = hitBox.y + hitBox.height - (int) (12 * Game.SCALE);
            attackBox.x = hitBox.x - (3 * Game.SCALE);
        }
    }


    private void updateHealthBar() {
        healthWidth = (int) (currentHealth / (float) maxHealth * healthBarWidth);
    }

    public void render(Graphics g) {
        if (playerDirection == LEFT)
//            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) x + 96, (int) y, -96, 96, null);
            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) (hitBox.x - xOffset) + 96, (int) (hitBox.y - yOffset), -96, 96, null);
        else
//            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) x, (int) y, 96, 96, null);
            g.drawImage(animations[animIndex][getAnimationRow(playerAction, playerDirection)], (int) (hitBox.x - xOffset), (int) (hitBox.y - yOffset), 96, 96, null);

        // Draws Attack Box and HitBox for debugging purpose
//        drawHitBox(g);
//        drawAttackBox(g);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statBarImg, statBarX, statBarY, (int) (statBarWidth), (int) (statBarHeight), null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statBarX + 1, healthBarYStart + statBarY, (int) (healthWidth), (int) (healthBarHeight));
    }


    private void updateAnimationTick() {

        animTick++;

        if (animTick >= animSpeed) {
            animTick = 0; // Resets the tick
            animIndex++;
            if (animIndex >= getSpriteAmount(playerAction)) {
                animIndex = 0; // Resets the index
                if (playerAction == ATTACKING) {
                    attacking = false;
                    attackChecked = false;
                }
            }
        }
    }

    private void setAnimation() {
        Constants.PlayerState startAnim = playerAction;

        if (isDying) {
            playerAction = DYING;
        } else if (attacking) {
            playerAction = ATTACKING;
        } else if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
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
            dx = -2;
            playerDirection = LEFT;

        } else if (right && !left) {
            dx = 2;
            playerDirection = RIGHT;
        }

        if (up && !down) {
            dy = -2;
            playerDirection = UP;

        } else if (down && !up) {
            dy = 2;
            playerDirection = DOWN;
        }



        if (dy != 0 || dx != 0) {
            if (CanMoveHere(hitBox.x + dx, hitBox.y + dy, hitBox.width, hitBox.height, lvlData)) {
                double length = Math.sqrt(dx * dx + dy * dy);
                hitBox.x += (playerSpeed * dx / length);
                hitBox.y += (playerSpeed * dy / length);
                moving = true;

            }
        }
    }

    public static void updateHealth(int value) {
        currentHealth += value;
        if (currentHealth <= 0) {
            currentHealth = 0;
            //gameOver();
        }
        if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
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
        statBarImg = LoadSave.getSpriteAtlas(LoadSave.HEALTH_BAR);
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
