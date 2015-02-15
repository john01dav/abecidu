package abecidu.weapons;

import abecidu.AbeciduMap;
import abecidu.Abecidu;
import abecidu.entity.Entity;
import abecidu.entity.EntityPlayer;
import abecidu.entity.EntityProjectile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class WeaponsManager extends MouseAdapter{
    private AbeciduMap abeciduMap;
    private Abecidu abecidu;

    private EntityPlayer entityPlayer;
    private ArrayList<LineCacheInstance> lineCacheInstanceArrayList;

    private Weapon[] weapons = new Weapon[6];
    private int currentWeaponIndex = 0;
    private Weapon currentWeapon;

    private long nextFire = 0L;

    public WeaponsManager(Abecidu abecidu, AbeciduMap abeciduMap){
        this.abecidu = abecidu;
        this.abeciduMap = abeciduMap;

        lineCacheInstanceArrayList = new ArrayList<>();

        for(Entity entity : abeciduMap.getEntityManager().getEntityList()){
            if(entity instanceof EntityPlayer){
                entityPlayer = ((EntityPlayer) entity);
                break;
            }
        }

        weapons[0] = new WeaponFryingPan();
        weapons[1] = new WeaponThrowingKnife(abecidu, abeciduMap, entityPlayer);
        /*weapons[2]
        weapons[3]
        weapons[4]
        weapons[5]
        weapons[6]*/

        currentWeapon = weapons[currentWeaponIndex];
    }

    public void onTick(){

    }

    public void onRender(Graphics g){
        Point mousePos = abecidu.getMousePos();
        if(mousePos != null){
            if(mousePos.x < entityPlayer.getLocation().x){
                currentWeapon.render(g, entityPlayer.getLocation().x - 3, entityPlayer.getLocation().y - 4);
            }else{
                currentWeapon.render(g, (entityPlayer.getLocation().x + Entity.ENTITY_SIZE) - 3, entityPlayer.getLocation().y - 4);
            }
        }

        g.setColor(Color.WHITE);
        Iterator<LineCacheInstance> lineCacheInstanceIterator = lineCacheInstanceArrayList.iterator();
        while(lineCacheInstanceIterator.hasNext()){
            LineCacheInstance lineCacheInstance = lineCacheInstanceIterator.next();
            if(lineCacheInstance.expires <= System.currentTimeMillis()){
                lineCacheInstanceIterator.remove();
            }else{
                g.drawLine(lineCacheInstance.p1.x, lineCacheInstance.p1.y, lineCacheInstance.p2.x, lineCacheInstance.p2.y);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){
            System.out.println();
            System.out.println(System.currentTimeMillis());
            System.out.println(nextFire);
            if(System.currentTimeMillis() < nextFire){
                System.out.println("Cancelling Fire: cooldown");
                return; //player must wait to fire next weapon
            }else{
                System.out.println("Allowing Fire: cooldown");
            }

            Entity entity = abeciduMap.getEntityManager().getEntityAt(e.getPoint());

            currentWeapon.deploy(e.getPoint()); //the weapon will always be notified when there is a click, it is up to the weapon whether or not to do anything
            nextFire = System.currentTimeMillis() + 500L;

            if(entity != null){
                if (!(entity instanceof EntityProjectile)){
                    double distance = getDistance(entity.getLocation(), entityPlayer.getLocation());
                    System.out.println(distance + " : " + currentWeapon.getMaxDistance());
                    if (distance < currentWeapon.getMaxDistance()) {
                        currentWeapon.deploy(entity);

                        LineCacheInstance lineCacheInstance = new LineCacheInstance();
                        lineCacheInstance.expires = System.currentTimeMillis() + 3000;
                        lineCacheInstance.p1 = e.getPoint();
                        lineCacheInstance.p2 = entityPlayer.getLocation();
                        lineCacheInstanceArrayList.add(lineCacheInstance);
                    }
                }
            }
        }else if(e.getButton() == MouseEvent.BUTTON3){
            do{
                currentWeaponIndex++;
                if(currentWeaponIndex >= weapons.length){
                    currentWeaponIndex = 0;
                }
                currentWeapon = weapons[currentWeaponIndex];
            }while(currentWeapon == null);
       }
    }

    public Weapon getCurrentWeapon(){
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon){
        this.currentWeapon = currentWeapon;
    }

    private class LineCacheInstance{
        public Point p1;
        public Point p2;
        public long expires;
    }

    private double getDistance(Point p1, Point p2){
        return Math.sqrt(square(p1.x - p2.x) + square(p1.y - p2.y));
    }

    private double square(double d){
        return d * d;
    }

}
