package abecidu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyCache implements KeyListener{
    private boolean[] keyStates = new boolean[8192];

    public KeyCache(){
        for(int x=0;x<keyStates.length;x++){
            keyStates[x] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e){
        keyStates[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e){
        keyStates[e.getKeyCode()] = false;
    }

    public boolean isKeyDown(int keyCode){
        return keyStates[keyCode];
    }

}
