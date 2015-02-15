package abecidu;

import abecidu.entity.EntityManager;

import java.awt.*;
import java.util.Random;

public class AbeciduMap{
    public static final int MAX_HEIGHT = 600;
    public static final int HEIGHT_INCREMENT = 10;
    public static final int DISTANCE_INCREMENT = 25;

    private Random random = new Random(System.currentTimeMillis());

    private int[] heights = new int[8192];

    private EntityManager entityManager;

    private float[] colorRedGradients = null;


    public AbeciduMap(Abecidu abecidu){
        entityManager = new EntityManager(abecidu, this);
    }

    public void launch(){
        entityManager.launch();
    }

    public void generateHeights(){
        int currentHeight = MAX_HEIGHT - random.nextInt(25);
        heights[0] = currentHeight;

        for(int cx=1;cx<heights.length;cx++){
            if(cx % DISTANCE_INCREMENT == 0){
                currentHeight += (random.nextInt(HEIGHT_INCREMENT) - (HEIGHT_INCREMENT / 2));

                if(currentHeight >= MAX_HEIGHT){
                    currentHeight = MAX_HEIGHT;
                }

                if(currentHeight < 5){
                    currentHeight = 5;
                }
            }

            heights[cx] = currentHeight;
        }
    }

    public void renderHeights(Graphics g, int width, int height){
        try{
            if(colorRedGradients == null){
                float[] colorRedGradients = new float[8192];
                for (int x = 0; x < width; x++) {
                    for (int y = heights[x]; y < height; y++) {
                        colorRedGradients[y] = (((float) y) / ((float) height)) * .1f;
                        g.setColor(new Color(colorRedGradients[y], 0, 0));
                        g.drawLine(x, y, x, y);
                    }
                }
            }else{
                for (int x = 0; x < width; x++) {
                    for (int y = heights[x]; y < height; y++) {
                        g.setColor(new Color(colorRedGradients[y], 0, 0));
                        g.drawLine(x, y, x, y);
                    }
                }
            }
        }catch(Throwable t){
            t.printStackTrace();
        }
    }

    public int getHeightAt(int x){
        return heights[x];
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

}
