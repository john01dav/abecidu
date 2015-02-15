package abecidu.entity;

import abecidu.Abecidu;
import abecidu.AbeciduMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class EntityManager implements Runnable{
    private Thread thread;
    private Abecidu abecidu;
    private AbeciduMap abeciduMap;
    private ArrayList<Entity> entityArrayList;

    public EntityManager(Abecidu abecidu, AbeciduMap abeciduMap){
        this.abeciduMap = abeciduMap;
        entityArrayList = new ArrayList<>();
    }

    public synchronized void spawnEntity(Entity entity){
        entityArrayList.add(entity);
    }

    public void launch(){
        thread = new Thread(this);
        thread.setName("EntityManager Thread");
        thread.start();
    }

    public void run(){
        try {
            while (true) {
                long start = System.currentTimeMillis();
                onTick();
                long elapsed = System.currentTimeMillis() - start;
                long sleepTime = 50 - elapsed;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch(Throwable t){
            abecidu.crash(t);
        }
    }

    public synchronized void onTick() throws ConcurrentModificationException{
        try {
            ArrayList<Entity> toRemove = new ArrayList<Entity>();
            for(int x=0;x<entityArrayList.size();x++){
                Entity entity = entityArrayList.get(x);
                entity.onTick();
                if (entity.isDead()) {
                    toRemove.add(entity);
                }
            }

            for (Entity entity : toRemove) {
                entityArrayList.remove(entity);
            }
        }catch(ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    public synchronized void render(Graphics g){
        try {
            for (Entity entity : entityArrayList) {
                entity.onRender(g);
            }
        }catch(ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    public AbeciduMap getMap(){
        return abeciduMap;
    }

    public List<Entity> getEntityList(){
        return entityArrayList;
    }

    public synchronized Entity getEntityAt(Point point){
        for(Entity entity : getEntityList()){
            if(entity.isPointInEntity(point))
                return entity;
        }
        return null;
    }

}
