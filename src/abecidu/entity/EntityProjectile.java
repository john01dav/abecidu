package abecidu.entity;

import abecidu.Abecidu;
import abecidu.AbeciduMap;

import java.awt.*;

public abstract class EntityProjectile extends Entity{
    public static final int ENTITY_SIZE = 8;

    private Abecidu abecidu;

    private Entity shooter;

    private Point source;
    private Point vertex;

    private int currentX;
    private int currentY;
    private int increment;

    private boolean dead = false;

    protected EntityProjectile(Entity shooter, Abecidu abecidu, AbeciduMap abeciduMap, Point source, Point vertex){
        super(abecidu, abeciduMap);

        this.abecidu = abecidu;

        this.shooter = shooter;
        this.source = source;
        this.vertex = vertex;

        currentX = source.x;

        if(vertex.x < source.x){
            increment = -10;
        }else{
            increment = 10;
        }
    }

    public synchronized void onTick(){
        onAPITick();

        currentX += increment;
        forceMoveTo(new Point(currentX, (int) Math.round(calculateY(currentX))));

        if(abecidu != null){
            if (getLocation().x < 0 || getLocation().getY() < 0 || getLocation().getX() > abecidu.getWidth() || getLocation().getY() > abecidu.getHeight()) {
                dead = true;
            }
        }
    }

    public boolean checkCollision(Point point){
        return true; //projectiles don't collide
    }

    public synchronized double calculateY(int x){
        double xD = ((double) x);
        double sourceX = ((double) source.x);
        double sourceY = ((double) source.y);
        double vertexX = ((double) vertex.x);
        double vertexY = ((double) vertex.y);

        return ((sourceY - vertexY)/square(sourceX - vertexX)) * square(xD - vertexX) + vertexY;
    }

    private synchronized double square(double d){
        return d * d;
    }

    public synchronized Point getSource(){
        return source;
    }

    public synchronized Point getVertex(){
        return vertex;
    }

    @Override
    public synchronized boolean isDead(){
        return dead;
    }

    public Entity getShooter(){
        return shooter;
    }

}
