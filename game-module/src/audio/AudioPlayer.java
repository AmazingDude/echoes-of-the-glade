package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {
    public static int BACKGROUND_MUSIC = 0;
    public static int MENU_MUSIC = 1;
    public static int DEATH_MUSIC = 2;

    public static int ATTACK_SOUND = 0;
    public static int DEATH_SOUND = 1;
    public static int HIT_SOUND = 2;
    public static int MOVE_SOUND = 3;

    private Clip[] music, effects;
    private int currentMusicID;
    private float volume = 0.8F;
    private boolean musicMute, effectMute;

    // Track if move sound is currently playing
    private boolean moveSoundPlaying = false;

    public AudioPlayer () {
        loadMusic();
        loadEffects();
        playMusic(MENU_MUSIC);
    }

    private void loadMusic() {
        String[] names = {"thick_of_it", "beach_lasagna", "rickroll"};
        music = new Clip[names.length];
        for (int i = 0; i < names.length; i++) {
            music[i] = getClip(names[i]);
        }
    }

    private void loadEffects() {
        String[] names = {"attack_sound", "death_sound", "hit", "walk"};
        effects = new Clip[names.length];
        for (int i = 0; i < names.length; i++) {
            effects[i] = getClip(names[i]);
        }
        updateEffectsVol();
    }

    private Clip getClip (String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip clip = null;
            clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }

    public void toggleMusicMute () {
        musicMute = !musicMute;
        for (Clip clip : music) {
            BooleanControl booleanControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(musicMute);
        }
    }

    public void playAttackSound() {
        playEffect(ATTACK_SOUND);
    }

    public void playEffect (int effect) {
        effects[effect].setMicrosecondPosition(0); // Start the audio from the beginning
        effects[effect].start();
    }

    public void playMusic (int music) {
        if (this.music[currentMusicID].isActive())
            this.music[currentMusicID].stop();

        currentMusicID = music;
        updateMusicVol();
        this.music[currentMusicID].setMicrosecondPosition(0);
        this.music[currentMusicID].loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void updateMusicVol () {
        FloatControl gainControl = (FloatControl) music[currentMusicID].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectsVol () {
        for (Clip clip : effects) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    public void playMoveSound() {
        if (!moveSoundPlaying) {
            Clip moveClip = effects[MOVE_SOUND];
            moveClip.setMicrosecondPosition(0);
            moveClip.loop(Clip.LOOP_CONTINUOUSLY);
            moveSoundPlaying = true;
        }
    }

    public void stopMoveSound() {
        if (moveSoundPlaying) {
            Clip moveClip = effects[MOVE_SOUND];
            moveClip.stop();
            moveSoundPlaying = false;
        }
    }
}
