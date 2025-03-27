package utils;

public class Constants {
    public enum PlayerState {
        IDLE, RUNNING, ATTACKING, DYING;
    }

    public enum Direction {
        DOWN, RIGHT, UP, LEFT;
    }

    // Maps PlayerState + Direction to animation row index
    public static int getAnimationRow(PlayerState state, Direction direction) {
        switch (state) {
            case IDLE:
                return (direction == Direction.LEFT) ? 1 : direction.ordinal(); // IDLE animations (row 0-2, no left)
            case RUNNING:
                switch (direction) {
                    case DOWN: return 3;  // Running Down (row 4)
                    case RIGHT: return 4; // Running Right (row 5)
                    case UP: return 5;    // Running Up (row 6)
                    case LEFT: return 4;  // Running Left → Use Right's animation (row 5)
                }
            case ATTACKING:
                switch (direction) {
                    case DOWN: return 6;
                    case RIGHT: return 7;
                    case UP: return 8;
                    case LEFT: return 7;  // Attack Left → Use Right's animation (row 8)
                }
            case DYING:
                return 9; // Single death animation (row 10)
            default:
                return 0;
        }
    }

    public static int getSpriteAmount(PlayerState state) {
        switch (state) {
            case IDLE:
            case RUNNING:
                return 6;
            case ATTACKING:
                return 4;
            case DYING:
            default:
                return 3;
        }
    }
}
