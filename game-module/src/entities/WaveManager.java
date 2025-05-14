package entities;

import gamestates.Playing;
import main.Game;

import java.awt.*;

public class WaveManager {
    private Playing playing;
    private EnemyHandler enemyHandler;
    
    private int currentWave = 0;
    private int enemiesPerWave = 3; // Starting number of enemies
    private int enemiesIncrementPerWave = 2; // How many more enemies to add each wave
    
    private int waveTimerStart = 10 * 60; // 10 seconds at 60 FPS
    private int waveTimer = waveTimerStart;
    private boolean waveStarted = false;
    
    private Font timerFont;
    
    public WaveManager(Playing playing, EnemyHandler enemyHandler) {
        this.playing = playing;
        this.enemyHandler = enemyHandler;
        timerFont = new Font("Arial", Font.BOLD, 30);
    }
    
    public void update() {
        if (!waveStarted) {
            waveTimer--;
            if (waveTimer <= 0) {
                startWave();
            }
        } else {
            // Check if all enemies are dead to end the wave
            if (enemyHandler.allEnemiesDead()) {
                endWave();
            }
        }
    }
    
    private void startWave() {
        Player.updateHealth(Player.maxHealth);
        waveStarted = true;
        currentWave++;
        
        // Calculate number of enemies for this wave
        int enemiesToSpawn = enemiesPerWave + (currentWave - 1) * enemiesIncrementPerWave;
        
        // Spawn enemies
        enemyHandler.spawnEnemies(enemiesToSpawn);
    }
    
    private void endWave() {
        waveStarted = false;
        waveTimer = waveTimerStart;
    }
    
    public void draw(Graphics g) {
        // Draw wave timer at the top of the screen
        g.setFont(timerFont);
        g.setColor(Color.WHITE);
        
        String timerText;
        if (waveStarted) {
            timerText = "WAVE " + currentWave;
        } else {
            timerText = "NEXT WAVE IN: " + (waveTimer / 120 + 1); // Convert frames to seconds
        }
        
        int textWidth = g.getFontMetrics().stringWidth(timerText);
        g.drawString(timerText, Game.GAME_WIDTH / 2 - textWidth / 2, 50);
    }
    
    public void reset() {
        currentWave = 0;
        waveTimer = waveTimerStart;
        waveStarted = false;
    }
}