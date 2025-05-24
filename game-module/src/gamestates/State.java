package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

//    public void setGameState(GameState state) {
//        switch (state) {
//            case MENU:
//                game.getAudioPlayer().playMusic(AudioPlayer.MENU_MUSIC);
//                break;
//            case PLAYING:
//                game.getAudioPlayer().playMusic(AudioPlayer.BACKGROUND_MUSIC);
//                break;
//        }
//        GameState.state = state;
//    }
}
