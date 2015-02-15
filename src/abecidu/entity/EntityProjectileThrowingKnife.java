package abecidu.entity;

import abecidu.Abecidu;
import abecidu.AbeciduMap;

import java.awt.*;

public class EntityProjectileThrowingKnife extends EntityProjectile{
    private Entity entity;

    public EntityProjectileThrowingKnife(Entity entity, Abecidu abecidu, AbeciduMap abeciduMap, Point source, Point vertex){
        super(entity, abecidu, abeciduMap, source, vertex);
    }

    @Override
    public synchronized void onRender(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(getLocation().x, getLocation().y, 8, 8);
    }

    @Override
    public synchronized void onAPITick(){

    }

}
