package entities;
import static utils.Constants.EnemyConstants.*;

public class Slime extends Enemy{

    public Slime(float x, float y) {
        super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
    }
}
