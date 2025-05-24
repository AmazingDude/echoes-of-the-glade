package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods{

    private MenuButton[] buttons = new MenuButton[2];
    private BufferedImage backgroundImg, menuBackgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        menuBackgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE) - 90;
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE) - 90;
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (Game.SCALE * 150), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (Game.SCALE * 220), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void test() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(menuBackgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed())
                    mb.applyGameState();
                if (mb.getGameState() == GameState.PLAYING)
                    game.getAudioPlayer().playMusic(AudioPlayer.BACKGROUND_MUSIC);
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton mb : buttons)
            mb.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            mb.setMouseOver(false);

        for (MenuButton mb : buttons)
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
            game.getAudioPlayer().playMusic(AudioPlayer.BACKGROUND_MUSIC);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
