package abecidu.weapons;

import abecidu.Abecidu;
import abecidu.AbeciduMap;
import abecidu.entity.Entity;
import abecidu.entity.EntityPlayer;
import abecidu.entity.EntityProjectileThrowingKnife;

import java.awt.*;

public class WeaponThrowingKnife implements Weapon{
    private Abecidu abecidu;
    private AbeciduMap abeciduMap;
    private EntityPlayer entityPlayer;

    public WeaponThrowingKnife(Abecidu abecidu, AbeciduMap abeciduMap, EntityPlayer entityPlayer){
        this.abecidu = abecidu;
        this.abeciduMap = abeciduMap;
        this.entityPlayer = entityPlayer;
    }

    @Override
    public void render(Graphics g, int x, int y){
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y + 14, 8, 14);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, 4, 20);
    }

    @Override
    public void deploy(Entity entity){} //not used

    @Override
    public void deploy(Point point){
        EntityProjectileThrowingKnife entityProjectileThrowingKnife = new EntityProjectileThrowingKnife(entityPlayer, abecidu, abeciduMap, entityPlayer.getLocation(), point);
        abeciduMap.getEntityManager().spawnEntity(entityProjectileThrowingKnife);
    }

    @Override
    public int getMaxDistance(){
        return Integer.MAX_VALUE;
    }

}
