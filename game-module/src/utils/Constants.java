package utils;

import main.Game;

public class Constants {

    public static class EnemyConstants {
        public static final int SLIME = 0;
        public static final int IDLE = 1;
        public static final int RUNNING = 2;
        public static final int ATTACK = 3;
        public static final int HIT = 4;
        public static final int DEAD = 5;
        public static final int SLIME_WIDTH_DEFAULT = 32;
        public static final int SLIME_HEIGHT_DEFAULT = 32;
        public static final int SLIME_WIDTH = (int) (SLIME_WIDTH_DEFAULT * Game.SCALE);
        public static final int SLIME_HEIGHT = (int) (SLIME_HEIGHT_DEFAULT * Game.SCALE);

        public static int getSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case SLIME:
                    switch (enemyState) {
                        case IDLE:
                            return 4;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 3;
                        case DEAD:
                            return 5;
                    }
            }
            return 0;
        }
    }

//    public enum EnemyConstants {
//        SLIME, IDLE, RUNNING, ATTACKING, HIT, DEAD
//    }

    public static class UI {
        public static class Buttons {
            public static final int BTN_DEFAULT_WIDTH = 140;
            public static final int BTN_DEFAULT_HEIGHT = 56;
            public static final int BTN_WIDTH = (int) (BTN_DEFAULT_WIDTH * Game.SCALE);
            public static final int BTN_HEIGHT = (int) (BTN_DEFAULT_HEIGHT * Game.SCALE);
        }
    }

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
