package entities;

import gamestates.Playing;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyHandler {
    private Playing playing;
    private BufferedImage[][] slimeArr;
    private ArrayList<Slime> slimes = new ArrayList<>();

    public EnemyHandler(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        slimes = LoadSave.getSlimes();
    }

    public void update() {
        for (Slime s : slimes)
            s.update();
    }

    public void draw(Graphics g) {
        drawSlimes(g);
    }

    private void drawSlimes(Graphics g) {
        for (Slime s : slimes)
//            g.drawImage(slimeArr[s.getEnemyState()][s.getAnimIndex()], (int) s.getHitBox().x, (int) s.getHitBox().y, SLIME_WIDTH , SLIME_HEIGHT, null);
            g.drawImage(slimeArr[s.getEnemyState()][s.getAnimIndex()], (int) s.getHitBox().x, (int) s.getHitBox().y, (int) (SLIME_WIDTH * 1.5), (int) (SLIME_HEIGHT * 1.5), null);
    }

    private void loadEnemyImgs() {
        slimeArr = new BufferedImage[13][7]; //13 rows
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SLIME_ATLAS);
        for (int i = 0; i < slimeArr.length; i++)
            for (int j = 0; j < slimeArr[i].length; j++)
                slimeArr[i][j] = temp.getSubimage(j * SLIME_WIDTH_DEFAULT, i * SLIME_HEIGHT_DEFAULT, SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
    }
}
