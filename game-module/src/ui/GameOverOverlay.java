package ui;

import gamestates.GameState;
import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;
    public GameOverOverlay(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0,200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.setColor(Color.red);
        g.drawString("Game Over", Game.GAME_WIDTH / 2 - 20, 150);
        g.setColor(Color.white);
        g.drawString("Press esc to enter main menu!", Game.GAME_WIDTH / 2 - 100, 300);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }
}
