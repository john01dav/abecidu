package abecidu.entity;

import abecidu.Abecidu;
import abecidu.AbeciduMap;

import java.awt.*;
import java.util.Random;

public class EntityMonster extends Entity{
    private Random random = new Random(System.currentTimeMillis());
    private Abecidu abecidu;
    private AbeciduMap abeciduMap;
    private EntityPlayer entityPlayer;
    private int fireCountdown = 100;

    public EntityMonster(Abecidu abecidu, AbeciduMap abeciduMap){
        super(abecidu, abeciduMap);

        this.abeciduMap = abeciduMap;

        setHealth(40);

        for(Entity entity : abeciduMap.getEntityManager().getEntityList()){
            if(entity instanceof EntityPlayer){
                entityPlayer = ((EntityPlayer) entity);
            }
        }
    }

    @Override
    public synchronized void onRender(Graphics g){
        g.setColor(Color.CYAN);
        g.fillRect(getLocation().x, getLocation().y, ENTITY_SIZE, ENTITY_SIZE);

        g.setColor(Color.RED);
        g.drawLine(getLocation().x, getLocation().y, getLocation().x, getLocation().y - getHealth());
    }

    @Override
    public synchronized void onAPITick(){
        if(random.nextBoolean()) {
            if (entityPlayer.getLocation().x < getLocation().x) {
                moveLeft();
            } else {
                moveRight();
            }
            if(getOnGround()){
                jump(false);
            }
        }

        fireCountdown--;
        if(fireCountdown < 0){
            fireCountdown = 25;
            fireProjectile();
        }
    }

    private synchronized void fireProjectile(){
        EntityProjectileThrowingKnife entityProjectileThrowingKnife = new EntityProjectileThrowingKnife(this, abecidu, abeciduMap, new Point(getLocation().x + 5, getLocation().y), new Point((getLocation().x + entityPlayer.getLocation().x) / 2, 0));
        abeciduMap.getEntityManager().spawnEntity(entityProjectileThrowingKnife);
    }

}
