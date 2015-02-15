package abecidu.entity;

import abecidu.Abecidu;
import abecidu.AbeciduMap;
import abecidu.AudioSpriteHandler;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class Entity {
    public static final int ENTITY_SIZE = 16;

    private AbeciduMap abeciduMap;
    private Abecidu abecidu;

    private Point entityLocation = new Point(32, 0);

    private boolean onGround = false;
    private int noGravityCountdown = 0;

    public Entity(Abecidu abecidu, AbeciduMap abeciduMap){
        this.abecidu = abecidu;
        this.abeciduMap = abeciduMap;
    }

    private int health = 128;

    public abstract void onRender(Graphics g);
    public abstract void onAPITick();

    public boolean isDead(){
        return health < 1;
    }

    protected synchronized void onTick(){
        onAPITick();

        onGround = false;
        if(noGravityCountdown > 0){
            for(int x=0;x>-3;x--){
                Point newPoint = new Point(entityLocation.x, entityLocation.y + x);
                if(!attemptMoveTo(newPoint)){
                    break;
                }
            }
            noGravityCountdown--;
        }else{
            for(int x=0;x<=3;x++){
                Point newPoint = new Point(entityLocation.x, entityLocation.y + x);
                if(!attemptMoveTo(newPoint)){
                    onGround = true;
                    break;
                }
            }
        }

        for(Entity entity : abeciduMap.getEntityManager().getEntityList()){
            if(entity instanceof EntityProjectile) {
                EntityProjectile entityProjectile = ((EntityProjectile) entity);
                if(entityProjectile.getShooter() != this) {
                    double distance = getDistance(entity.getLocation(), getLocation());
                    if (getDistance(entity.getLocation(), getLocation()) <= 25) {
                        setHealth(getHealth() - 10);
                    }
                }
            }
        }

    }

    private synchronized double getDistance(Point p1, Point p2){
        return Math.sqrt(square(p1.x - p2.x) + square(p1.y - p2.y));
    }

    private synchronized double square(double d){
        return d * d;
    }

    public synchronized boolean attemptMoveTo(Point newPoint){
        if(checkCollision(newPoint)){
            return false;
        }else{
            entityLocation = newPoint;
            return true;
        }
    }

    protected synchronized void forceMoveTo(Point newPoint){
        entityLocation = newPoint;
    }

    public synchronized boolean checkCollision(){
        return checkCollision(entityLocation);
    }

    public synchronized boolean checkCollision(Point point){
        int y = point.y + ENTITY_SIZE;

        if(point.x < 0){
            return true;
        }

        if(point.y > (Toolkit.getDefaultToolkit().getScreenSize().width - 100 - ENTITY_SIZE)){
            return true;
        }

        for(int x=point.x;x<=(point.x + ENTITY_SIZE);x++){
            int height = abeciduMap.getHeightAt(x);
            if(y >= height){
                return true;
            }
        }

        return false;
    }

    public synchronized void moveRight(){
        int movementAmount = 3;
        while(movementAmount > 0) {
            Point newPoint = new Point(getLocation().x + 3, getLocation().y);
            if(attemptMoveTo(newPoint)) break;
            movementAmount--;
        }
    }

    public synchronized void moveLeft(){
        int movementAmount = 3;
        while(movementAmount > 0) {
            Point newPoint = new Point(getLocation().x - 3, getLocation().y);
            if(attemptMoveTo(newPoint)) break;
            movementAmount--;
        }
    }

    public synchronized void jump(boolean checkGround){
        if(getOnGround() && checkGround) return;
        Point newPoint = new Point(getLocation().x, getLocation().y - 5);
        attemptMoveTo(newPoint);
        setNoGravityCountdown(3);
    }

    public Point getLocation(){
        return entityLocation;
    }

    public boolean getOnGround(){
        return onGround;
    }

    public int getNoGravityCountdown(){
        return noGravityCountdown;
    }

    public synchronized void setNoGravityCountdown(int noGravityCountdown){
        this.noGravityCountdown = noGravityCountdown;
    }

    public int getHealth(){
        return health;
    }

    public synchronized void setHealth(int health){
        this.health = health;
        if(health < 1){ //death
            AudioSpriteHandler.playSound(abecidu, "monster_death", false);
        }
    }

    public synchronized boolean isPointInEntity(Point point){
        int xMin = getLocation().x;
        int xMax = getLocation().x + ENTITY_SIZE;
        int yMin = getLocation().y;
        int yMax = getLocation().y + ENTITY_SIZE;

        int x = point.x;
        int y = point.y;

        return (x > xMin && x < xMax && y > yMin && y < yMax);
    }
}
