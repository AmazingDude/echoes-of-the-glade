package main;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoopThread;
    private final int FPS_LIMIT = 120;

    // Constructor
    public Game() {

        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus(); // For input focus
        startGameLoop(); // Calling this method will start the thread
    }

    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    @Override
    public void run() {
        // Time each frame should take in nanoseconds
        double timePerFrame = 1_000_000_000.0 / FPS_LIMIT;
        long lastFrameTime = System.nanoTime();
        long now;

        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while (true) {

            now = System.nanoTime();
            // This ensures that the time for each frame is at least as long as the timePerFrame, thus keeping the FPS limited to the specified FPS_LIMIT
            // Current time in nanoseconds - time taken by last frame to display >= time that each frame should take to display
            if (now - lastFrameTime >= timePerFrame) {
                gamePanel.repaint(); // Draws a new frame or redraws (basically the same thing);
                lastFrameTime = now;
                frames++;
            }

            // FPS COUNTER
            // If one second has passed since the last FPS check, we'll do a new FPS check. Save the new FPS check as the lastFPS check and repeat.
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }
}
