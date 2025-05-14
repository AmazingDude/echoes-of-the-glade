package entities;
import gamestates.Playing;
import main.Game;
import utils.Constants;
import utils.Constants.Direction;
import static utils.Constants.EnemyConstants.*;
import static utils.HelperMethods.CanMoveHere;
import java.util.ArrayList;

public abstract class Enemy extends Entity {

    private int animIndex, enemyState = IDLE, enemyType;
    private int animTick, animSpeed = 25;
    private float enemySpeed = 0.5f * Game.SCALE;
    private Playing playing;
    private boolean moving = false;
    private int[][] lvlData;
    private float separationDistance = 30.0f * Game.SCALE; // Minimum distance between enemies
    private Direction enemyDirection = Direction.DOWN; // Default direction
    private double distanceToPlayer = Double.MAX_VALUE; // Track distance to player
    private float reachedPlayerThreshold = 15.0f; // Distance threshold to consider player reached

    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);

        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
//        maxHealth = getMaxHealth(enemyType);
        maxHealth = 10;
        currentHealth = maxHealth;
    }

    public void hurt(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            enemyState = DEAD;
        } else {
            enemyState = HIT;
        }
    }

    private void updateAnimationTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= getSpriteAmount(enemyType, enemyState)) {
                animIndex = 0;

                switch (enemyState) {
                    case HIT -> enemyState = IDLE;
                    case DEAD -> {
                        // Only set inactive after the full death animation has played
                        if (animIndex == 0) {
                            active = false;
                        }
                    }
                }
            }
        }
    }

    public void update() {
        updateAnimationTick();
        updateEnemyPosition();
        attackPlayer();      // Call attackPlayer() before updateEnemyState() to prioritize attack state
        updateEnemyState();  // This will now only update state if not attacking
    }

    private void updateEnemyPosition() {
        if (playing == null || lvlData == null)
            return;

        // Get player position
        Player player = playing.getPlayer();
        float playerXPos = player.getHitBox().x;
        float playerYPos = player.getHitBox().y;

        // Calculate direction to player
        float xDiff = playerXPos - hitBox.x;
        float yDiff = playerYPos - hitBox.y;

        // Calculate distance to player
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        distanceToPlayer = distance;

        // If player is too close, don't move
        if (distance <= 5) {
            moving = false;
            return;
        }

        // Calculate movement speeds
        float xSpeed = (float) (enemySpeed * xDiff / distance);
        float ySpeed = (float) (enemySpeed * yDiff / distance);

        // Apply separation from other enemies
        float[] adjustedSpeeds = applySeparation(xSpeed, ySpeed);
        xSpeed = adjustedSpeeds[0];
        ySpeed = adjustedSpeeds[1];

        // Set direction based on primary movement axis
        updateEnemyDirection(xSpeed, ySpeed);

        // Try to move in both directions
        boolean canMove = tryMove(xSpeed, ySpeed);

        // If can't move in both directions, try individual axes
        if (!canMove) {
            // Try horizontal movement
            if (tryMove(xSpeed, 0)) {
                updateEnemyDirection(xSpeed, 0);
            }
            // Try vertical movement
            else if (tryMove(0, ySpeed)) {
                updateEnemyDirection(0, ySpeed);
            }
            // Can't move at all
            else {
                moving = false;
            }
        }
    }

    private boolean tryMove(float xSpeed, float ySpeed) {
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y + ySpeed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
            hitBox.y += ySpeed;
            moving = true;
            return true;
        }
        return false;
    }

    private void updateEnemyDirection(float xSpeed, float ySpeed) {
        if (Math.abs(xSpeed) > Math.abs(ySpeed)) {
            // Horizontal movement is primary
            enemyDirection = (xSpeed > 0) ? Direction.RIGHT : Direction.LEFT;
        } else {
            // Vertical movement is primary
            enemyDirection = (ySpeed > 0) ? Direction.DOWN : Direction.UP;
        }
    }

    private float[] applySeparation(float xSpeed, float ySpeed) {
        // If no playing state or no other enemies, return unchanged speeds
        if (playing == null)
            return new float[]{xSpeed, ySpeed};

        ArrayList<Slime> slimes = playing.getEnemyHandler().getSlimes();

        for (Slime other : slimes) {
            // Skip self
            if (other == this)
                continue;

            // Calculate distance to other enemy
            float xDiff = other.getHitBox().x - hitBox.x;
            float yDiff = other.getHitBox().y - hitBox.y;
            double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

            // Skip if not too close or at same position
            if (distance >= separationDistance || distance <= 0)
                continue;

            // Calculate separation force (stronger as enemies get closer)
            float separationFactor = (separationDistance - (float)distance) / separationDistance;

            // Apply separation force in opposite direction of other enemy
            float dirX = (float) (xDiff / distance);
            float dirY = (float) (yDiff / distance);
            xSpeed -= dirX * separationFactor * enemySpeed;
            ySpeed -= dirY * separationFactor * enemySpeed;
        }

        return new float[]{xSpeed, ySpeed};
    }

    private void updateEnemyState() {
        // Only update state if not already in ATTACK state (attack state is handled in attackPlayer method)
        if (enemyState != ATTACK || distanceToPlayer > reachedPlayerThreshold) {
            if (!moving) {
                enemyState = IDLE;
            } else {
                enemyState = RUNNING;
            }
        }
    }

    // Attack cooldown to prevent continuous damage
    private int attackCooldown = 0;
    private final int attackCooldownMax = 120; // About 1 second at 60 FPS

    private void attackPlayer() {
        if (distanceToPlayer <= reachedPlayerThreshold) {
            // Set enemy state to ATTACK when in attack range
            enemyState = ATTACK;

            if (attackCooldown <= 0) {
                // Get player and check if we can damage them
                Player player = playing.getPlayer();
                if (player != null) {
                    // Damage player (negative value reduces health)
                    player.updateHealth(-Constants.EnemyConstants.getEnemyDamage(SLIME)); // 10 damage points per attack

                    // Reset cooldown
                    attackCooldown = attackCooldownMax;

                    // Reset animation index to start attack animation from beginning
                    animIndex = 0;
                }
            }
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }
    }

    public int getAnimIndex() {
        return animIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }

    public Direction getEnemyDirection() {
        return enemyDirection;
    }

    public void setPlaying(Playing playing) {
        this.playing = playing;
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }
}
