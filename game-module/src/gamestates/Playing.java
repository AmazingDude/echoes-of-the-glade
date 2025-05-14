package gamestates;

import entities.EnemyHandler;
import entities.Player;
import entities.WaveManager;
import levels.LevelHandler;
import main.Game;
import ui.GameOverOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelHandler levelHandler;
    private EnemyHandler enemyHandler;
    private WaveManager waveManager;
    private boolean gameOver;
    private GameOverOverlay gameOverOverlay;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelHandler = new LevelHandler(game);
        enemyHandler = new EnemyHandler(this);
        player = new Player(200, 200, (int) (48 * Game.SCALE), (int) (48 * Game.SCALE), this);
        player.loadLvlData(levelHandler.getCurrentLevel().getLevelData());
        waveManager = new WaveManager(this, enemyHandler);
        gameOverOverlay = new GameOverOverlay(this);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void resetAll() {
        gameOver = false;

        // Reset player
        player = new Player(200, 200, (int) (48 * Game.SCALE), (int) (48 * Game.SCALE), this);
        player.loadLvlData(levelHandler.getCurrentLevel().getLevelData());

        // Reset enemy handler
        enemyHandler = new EnemyHandler(this);

        // Reset wave manager
        waveManager = new WaveManager(this, enemyHandler);
    }

    public Player getPlayer() {
        return player;
    }

    public LevelHandler getLevelHandler() {
        return levelHandler;
    }

    public EnemyHandler getEnemyHandler() {
        return enemyHandler;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    @Override
    public void update() {
        if (!gameOver) {
            player.update();
            levelHandler.update();
            enemyHandler.update();
            waveManager.update();
        }
    }

    @Override
    public void test() {

    }

    @Override
    public void draw(Graphics g) {
        levelHandler.draw(g);
        player.render(g);
        enemyHandler.draw(g);

        if (!gameOver) {
            waveManager.draw(g);
        } else {
            gameOverOverlay.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch(e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setUp(true);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_J:
                    player.setAttacking(true);
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch(e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setUp(false);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_ESCAPE:
                    GameState.state = GameState.MENU;
            }
    }

}
