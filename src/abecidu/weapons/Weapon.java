package abecidu.weapons;

import abecidu.entity.Entity;

import java.awt.*;

public interface Weapon {

    public void render(Graphics g, int x, int y);
    public void deploy(Entity entity);
    public void deploy(Point point);
    public int getMaxDistance();

}
