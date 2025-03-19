package main;

public class Game {
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    // Constructor
    public Game() {

        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
    }
}
