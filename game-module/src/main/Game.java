package main;

import entities.Player;
import levels.LevelHandler;

import java.awt.*;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoopThread;
    private final int FPS_LIMIT = 120;
    private final int UPS_LIMIT = 200;
    private Player player;
    private LevelHandler levelHandler;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    // Constructor
    public Game() {

        initClasses(); // Initialize classes
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus(); // For input focus


        startGameLoop(); // Calling this method will start the thread
    }

    private void initClasses() {
        levelHandler = new LevelHandler(this);
        player = new Player(200, 200, (int) (48 * SCALE), (int) (48 * SCALE));
        player.loadLvlData(levelHandler.getCurrentLevel().getLevelData());
    }

    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    public void update() {
        player.update();
        levelHandler.update();
    }

    public void render(Graphics g) {
        levelHandler.draw(g);
        player.render(g);
    }

    @Override
    public void run() {
        // Time each frame should take in nanoseconds
        double timePerFrame = 1_000_000_000.0 / FPS_LIMIT;
        double timePerUpdate = 1_000_000_000.0 / UPS_LIMIT;

        long prevTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();
            deltaU += (currentTime - prevTime) / timePerUpdate;
            deltaF += (currentTime - prevTime) / timePerFrame;
            prevTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            // This ensures that the time for each frame is at least as long as the timePerFrame, thus keeping the FPS limited to the specified FPS_LIMIT
            // Current time in nanoseconds - time taken by last frame to display >= time that each frame should take to display


            // FPS COUNTER
            // If one second has passed since the last FPS check, we'll do a new FPS check. Save the new FPS check as the lastFPS check and repeat.
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }
}
