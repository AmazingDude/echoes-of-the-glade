package utils;

import entities.Slime;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.SLIME;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player.png";
    public static final String SLIME_ATLAS = "slime.png";
    public static final String LEVEL_ATLAS = "plains.png";
//    public static final String LEVEL_DATA = "level_one_data.png";
    public static final String LEVEL_DATA = "test_level_data.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";

    public static BufferedImage getSpriteAtlas(String fileName) {
        BufferedImage img = null;

        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
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
        return img;
    }

    public static ArrayList<Slime> getSlimes() {
        BufferedImage img = getSpriteAtlas(LEVEL_DATA);
        ArrayList<Slime> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getGreen();
                if (value == SLIME)
                    list.add(new Slime(j * Game.TILES_SIZE, i * Game.TILES_SIZE));
            }
        }
        return list;
    }

    public static int[][] GetLevelData() {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = getSpriteAtlas(LEVEL_DATA);

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getRed();
                if (value >= 72)
                    value = 0;
                lvlData[i][j] = value;
            }
        }
        return lvlData;
    }
}
