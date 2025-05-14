package entities;

import gamestates.Playing;
import utils.LoadSave;
import utils.Constants.Direction;
import main.Game;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.EnemyConstants.getAnimationRow;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class EnemyHandler {
    private Playing playing;
    private BufferedImage[][] slimeArr;
    private ArrayList<Slime> slimes = new ArrayList<>();
    private Random random = new Random();

    public EnemyHandler(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public ArrayList<Slime> getSlimes() {
        return slimes;
    }

    private void addEnemies() {
        slimes = LoadSave.getSlimes();
        // Set the playing reference for each slime
        for (Slime s : slimes) {
            s.setPlaying(playing);
            s.loadLvlData(playing.getLevelHandler().getCurrentLevel().getLevelData());
        }
    }

    // Check if all enemies are dead
    public boolean allEnemiesDead() {
        if (slimes.isEmpty()) {
            return true;
        }

        for (Slime s : slimes) {
            if (s.isActive()) {
                return false;
            }
        }
        return true;
    }

    // Spawn a specific number of enemies
    public void spawnEnemies(int count) {
        // Clear any inactive enemies
        slimes.removeIf(slime -> !slime.isActive());

        // Get level dimensions
        int[][] lvlData = playing.getLevelHandler().getCurrentLevel().getLevelData();
        int levelWidth = lvlData[0].length;
        int levelHeight = lvlData.length;

        // Spawn new enemies
        for (int i = 0; i < count; i++) {
            // Find a valid spawn position
            float x, y;
            boolean validPosition;
            int attempts = 0;
            int maxAttempts = 100; // Prevent infinite loop

            do {
                // Generate random position within level bounds
                x = random.nextInt(levelWidth - 4) * Game.TILES_SIZE + Game.TILES_SIZE;
                y = random.nextInt(levelHeight - 4) * Game.TILES_SIZE + Game.TILES_SIZE;

                // Check if position is valid (not colliding with level or too close to player)
                validPosition = isValidSpawnPosition(x, y, attempts > 50); // Relax constraints after 50 attempts
                attempts++;

                // Break out of loop if too many attempts
                if (attempts > maxAttempts) {
                    // Use a default position that's at least not colliding with level
                    for (int tileY = 1; tileY < levelHeight - 1; tileY++) {
                        for (int tileX = 1; tileX < levelWidth - 1; tileX++) {
                            if (lvlData[tileY][tileX] == 0) { // Empty tile
                                x = tileX * Game.TILES_SIZE;
                                y = tileY * Game.TILES_SIZE;
                                validPosition = true;
                                break;
                            }
                        }
                        if (validPosition) break;
                    }
                    break; // Exit the loop regardless
                }
            } while (!validPosition);

            // Create and add new slime
            Slime newSlime = new Slime(x, y);
            newSlime.setPlaying(playing);
            newSlime.loadLvlData(lvlData);
            slimes.add(newSlime);
        }
    }

    private boolean isValidSpawnPosition(float x, float y) {
        return isValidSpawnPosition(x, y, false);
    }

    private boolean isValidSpawnPosition(float x, float y, boolean relaxConstraints) {
        // Check if position is valid (not colliding with level)
        int[][] lvlData = playing.getLevelHandler().getCurrentLevel().getLevelData();
        int tileX = (int) (x / Game.TILES_SIZE);
        int tileY = (int) (y / Game.TILES_SIZE);

        if (tileX < 0 || tileX >= lvlData[0].length || tileY < 0 || tileY >= lvlData.length) {
            return false;
        }

        if (lvlData[tileY][tileX] != 0) {
            return false;
        }

        // If we're relaxing constraints, just make sure it's on a valid tile
        if (relaxConstraints) {
            return true;
        }

        // Check if too close to player
        Player player = playing.getPlayer();
        float playerX = player.getHitBox().x;
        float playerY = player.getHitBox().y;

        double distance = Math.sqrt(Math.pow(x - playerX, 2) + Math.pow(y - playerY, 2));
        return distance > 200 * Game.SCALE; // Minimum distance from player
    }

    public void update() {
        for (Slime s : slimes)
            if (s.isActive())
                s.update();
    }

    public void draw(Graphics g) {
        drawSlimes(g);

    }

    private void drawSlimes(Graphics g) {
        for (Slime s : slimes) {

            int animationRow = getAnimationRow(s.getEnemyState(), s.getEnemyDirection());
            if (s.isActive()) {
                // Get the animation row based on state and direction

                // Draw the enemy with the correct animation and direction
                if (s.getEnemyDirection() == Direction.LEFT) {
                    // Flip horizontally for left-facing enemies
                    g.drawImage(slimeArr[animationRow][s.getAnimIndex()],
                            (int) s.getHitBox().x + (int) (SLIME_WIDTH * 1) + 3,
                            (int) s.getHitBox().y - 20,
                            -(int) (SLIME_WIDTH * 1.5),
                            (int) (SLIME_HEIGHT * 1.5),
                            null);
                } else {
                    // Normal rendering for other directions
                    g.drawImage(slimeArr[animationRow][s.getAnimIndex()],
                            (int) s.getHitBox().x - (int) (SLIME_WIDTH) + 25,
                            (int) s.getHitBox().y - 20,
                            (int) (SLIME_WIDTH * 1.5),
                            (int) (SLIME_HEIGHT * 1.5),
                            null);
                }
                g.drawRect((int) s.getHitBox().x, (int) s.getHitBox().y, (int) s.getHitBox().width, (int) s.getHitBox().height);
            }
        }
    }

    // Checks if player's attackBox overlaps the enemy's hitBox
    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Slime s : slimes) {
            if (attackBox.intersects(s.getHitBox())) {
                s.hurt(9999);
                return;
            }
        }
    }

    private void loadEnemyImgs() {
        slimeArr = new BufferedImage[13][7]; //13 rows
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SLIME_ATLAS);
        for (int i = 0; i < slimeArr.length; i++)
            for (int j = 0; j < slimeArr[i].length; j++)
                slimeArr[i][j] = temp.getSubimage(j * SLIME_WIDTH_DEFAULT, i * SLIME_HEIGHT_DEFAULT, SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
    }
}
