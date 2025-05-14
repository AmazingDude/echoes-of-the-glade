package entities;
import main.Game;

import static utils.Constants.EnemyConstants.*;

public class Slime extends Enemy{

    public Slime(float x, float y) {
        super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
        initHitBox(x, y, (int) (22 * Game.SCALE), (int) (22 * Game.SCALE));
    }
}
