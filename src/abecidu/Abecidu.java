package abecidu;

import abecidu.entity.EntityMonster;
import abecidu.entity.EntityPlayer;
import abecidu.weapons.WeaponsManager;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class Abecidu extends JComponent{
    private Random random = new Random(1L);
    private int sunPos = 0;
    private int sunIncrement = -5;
    private int sunIncrementChangeCooldown = 10;

    private KeyCache keyCache;
    private AbeciduMap abeciduMap;
    private WeaponsManager weaponsManager;

    private JFrame jFrame;

    private Point mousePos;
    private BufferedImage bufferedImageCapybara;

    private int entitySpawnCountdown = 0;

    private Thread renderingThread;
    private BufferedImage preRenderedImage;
    private BufferedImage pushRenderedImage;

    private TimeCounter timeCounter;

    public static void main(String[] args){
        Abecidu abecidu = new Abecidu();
        try {
            HelpMenu.show();
            abecidu.jFrame = new JFrame();
            abecidu.jFrame.setTitle("abecidu");
            abecidu.jFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width - 100, Toolkit.getDefaultToolkit().getScreenSize().height - 100);
            abecidu.jFrame.setLocationRelativeTo(null);
            abecidu.jFrame.setResizable(false);
            abecidu.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            abecidu.jFrame.setVisible(true);

            abecidu.jFrame.setLayout(new BorderLayout());

            abecidu.launch();

            while(true){
                long start = System.currentTimeMillis();
                abecidu.onTick();
                long elapsed = System.currentTimeMillis() - start;

                double memoryPercent = (((double)Runtime.getRuntime().freeMemory()) / ((double)Runtime.getRuntime().maxMemory())) * 100;

                abecidu.jFrame.setTitle("abecidu (" + elapsed + ") " + memoryPercent + "% memory");

                try {
                    long sleepTime = 50 - elapsed;
                    if(sleepTime > 0){
                        Thread.sleep(sleepTime);
                    }else{
                        System.out.println("The game is lagging!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch(Throwable t){
            abecidu.crash(t);
        }
    }

    public void crash(Throwable t){
        t.printStackTrace();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);

        String stackTrace = stringWriter.toString();
        String errorMessage = "<html><body><h1>Oh Noes!</h1><br> <p>An unrecoverable error (crash) has occurred. You must now close the game. Below is a stack trace to include in your report of the error.<p> <pre>(thread: " + Thread.currentThread().getName() + ") " + stackTrace + "</pre></body></html>";

        jFrame.remove(this);
        JLabel jLabel = new JLabel();
        jFrame.add(jLabel, BorderLayout.CENTER);
        jFrame.repaint();
        jLabel.setText(errorMessage);
    }

    private void launch() throws Throwable{
        sunPos = getWidth() / 2;

        keyCache = new KeyCache();
        jFrame.addKeyListener(keyCache);

        abeciduMap = new AbeciduMap(this);
        abeciduMap.generateHeights();
        abeciduMap.getEntityManager().spawnEntity(new EntityPlayer(this, abeciduMap, keyCache));

        weaponsManager = new WeaponsManager(this, abeciduMap);
        addMouseListener(weaponsManager);

        jFrame.add(this, BorderLayout.CENTER);

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream("bgm.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Integer.MAX_VALUE);
        clip.start();

        bufferedImageCapybara = ImageIO.read(getClass().getResourceAsStream("scarycapy.png"));

        abeciduMap.launch();

        timeCounter = new TimeCounter();

        renderingThread = new Thread(new GraphicsRunnable());
        renderingThread.setName("Rendering Thread");
        renderingThread.start();
    }

    private void onTick(){
        if(keyCache.isKeyDown(KeyEvent.VK_ESCAPE)){
            System.exit(0);
        }

        if(getMousePosition() != null) {
            mousePos = getMousePosition();
        }

        sunPos+=sunIncrement;

        if(sunPos > getWidth()){
            sunPos = -50;
        }

        if(sunPos < -10){
            sunPos = getWidth();
        }

        sunIncrementChangeCooldown--;
        if(sunIncrementChangeCooldown < 0){
            sunIncrementChangeCooldown = 10;
            sunIncrement = random.nextInt(20) - 10;

            int half = getWidth() / 2;
            if(Math.abs(sunPos - half) >= (getWidth() * 0.5)) {
                if (sunPos < half) {
                    sunIncrement += 5;
                } else {
                    sunIncrement -= 5;
                }
            }
        }

        entitySpawnCountdown--;
        if(entitySpawnCountdown < 0){
            entitySpawnCountdown = random.nextInt(100);

            int mobCount = (int) Math.ceil(((double)timeCounter.getElapsedTime()) / 1000.0); //ceil to prevent values of 0
            for(int cmc=0;cmc<mobCount;cmc++) {
                EntityMonster entityMonster = new EntityMonster(this, abeciduMap);
                entityMonster.attemptMoveTo(new Point(sunPos + 25, 5));
                abeciduMap.getEntityManager().spawnEntity(entityMonster);
                AudioSpriteHandler.playSound(this, "monster_spawn", false);
            }
        }
    }

    public void paint(Graphics g){
        g.drawImage(pushRenderedImage, 0, 0, null);
    }

    private class GraphicsRunnable implements Runnable{
        public void run(){
            try{
                while(true){
                    paintThread();
                    repaint();
                }
            }catch(Throwable t){
                crash(t);
            }
        }
    }

    public void paintThread(){
        while(true){ //continually attempt to initialize the images until it works (waiting for the window to appera)
            try{
                if(preRenderedImage == null || pushRenderedImage == null){
                    preRenderedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
                    pushRenderedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
                }
                break;
            }catch(Exception e){
                try{
                    Thread.sleep(1500);
                }catch(InterruptedException e2){crash(e2);}
            }
        }

        Graphics g = preRenderedImage.getGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());

        for(int y=getHeight();y>0;y--){
            int ch = getHeight() - y;
            int p255 = ((int)((((float) ch) /  ((float)getHeight())) * 150f));
            g.setColor(new Color(0, 0, p255));
            g.drawLine(0, y, getWidth(), y);
        }

        abeciduMap.renderHeights(g, getWidth(), getHeight());
        abeciduMap.getEntityManager().render(g);

        weaponsManager.onRender(g);

        g.setColor(Color.YELLOW);
        g.drawImage(bufferedImageCapybara, sunPos, 5, 50, 50, null);

        float seconds = (float) (((double)timeCounter.getElapsedTime()) / 1000.0);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(seconds) + " seconds", 10, 15);

        pushRenderedImage.getGraphics().drawImage(preRenderedImage, 0, 0, null);
    }

    public Point getMousePos(){
        return mousePos;
    }

    public JFrame getjFrame(){
        return jFrame;
    }

}
