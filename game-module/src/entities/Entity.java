package entities;

import java.awt.*;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle hitBox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        initHitBox();
    }

    protected void drawHitBox(Graphics g) {
        // For debugging hitbox
        g.setColor(Color.MAGENTA);
        g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    private void initHitBox() {
        hitBox = new Rectangle((int) x, (int) y, width, height);
    }

    protected void updateHitBox() {
//        hitBox.x = (int) x + 13;
//        hitBox.y = (int) y + 26;
        hitBox.x = (int) x;
        hitBox.y = (int) y;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }
}
