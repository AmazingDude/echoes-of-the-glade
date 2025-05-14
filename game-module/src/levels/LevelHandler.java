package levels;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelHandler {

    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelHandler(Game game) {
        this.game = game;
//        levelSprite = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[72];
        for (int i = 0; i < 12; i++) { // Height of tile map is 12 blocks and width is 6 blocks
            for (int j = 0; j < 6; j++) {
                int index = i * 6 + j;
//                levelSprite[index] = img.getSubimage(i * 16, j * 16, 48, 48);
                levelSprite[index] = img.getSubimage(j * 16, i * 16, 16, 16);
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++) {
            for (int j = 0; j < Game.TILES_IN_WIDTH; j++) {
                int index = levelOne.getSpriteIndex(j, i);
                g.drawImage(levelSprite[index], j * Game.TILES_SIZE, i * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }
}
