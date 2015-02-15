package abecidu.entity;

import abecidu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class EntityPlayer extends Entity{
    public static final int PLAYER_SIZE = 16;
    private AbeciduMap abeciduMap;
    private Abecidu abecidu;
    private KeyCache keyCache;

    public EntityPlayer(Abecidu abecidu, AbeciduMap abeciduMap, KeyCache keyCache){
        super(abecidu, abeciduMap);
        this.abeciduMap = abeciduMap;
        this.abecidu = abecidu;
        this.keyCache = keyCache;
    }

    public boolean isDead() {return false;}

    public synchronized void onAPITick(){
        if(keyCache.isKeyDown(KeyEvent.VK_A)){
            moveLeft();
        }

        if(keyCache.isKeyDown(KeyEvent.VK_D)){
            moveRight();
        }

        if(keyCache.isKeyDown(KeyEvent.VK_SPACE) && getOnGround()){
            jump(false);
        }

        if(getHealth() < 1){
            abecidu.getjFrame().setVisible(false);
            JOptionPane.showMessageDialog(null, "You lose! The game will now close.", "Message From The God Capybara", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public synchronized void onRender(Graphics g){
        g.setColor(Color.ORANGE);
        g.fillRect(getLocation().x, getLocation().y, PLAYER_SIZE, PLAYER_SIZE);

        g.setColor(Color.GREEN);
        g.drawLine(getLocation().x, getLocation().y, getLocation().x, getLocation().y - getHealth());
    }

}
