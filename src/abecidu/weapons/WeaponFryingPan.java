package abecidu.weapons;

import abecidu.entity.Entity;

import java.awt.*;

public class WeaponFryingPan implements Weapon{

    @Override
    public void render(Graphics g, int x, int y){
        g.setColor(Color.GRAY);
        g.fillRect(x, y, 4, 10);
        g.setColor(Color.ORANGE);
        g.fillOval(x - 3, y - 10, 10, 10);
    }

    @Override
    public void deploy(Entity entity) {
        entity.setHealth(entity.getHealth() - 20);
    }

    @Override
    public void deploy(Point point) {} //not used

    public int getMaxDistance(){
        return 200;
    }

}
