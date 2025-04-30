package utils;

import entities.Entity;
import main.Game;

public class HelperMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!isSolid(x, y, lvlData))
            if (!isSolid(x + width, y + height, lvlData))
                if (!isSolid(x + width, y, lvlData))
                    if (!isSolid(x, y + width, lvlData))
                        return true;
        return false;

    }

    private static boolean isSolid(float x, float y, int[][] lvlData) {
        // Check for boundaries
        if (x < 0 || x >= Game.GAME_WIDTH)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];
        if (value >= 72 || value < 0)
            return true;
        return false;
    }
}
