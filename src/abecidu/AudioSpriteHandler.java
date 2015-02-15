package abecidu;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.BufferedInputStream;

public class AudioSpriteHandler {

    public static synchronized void playSound(Abecidu abecidu, String soundLabel, boolean looping){
        try {
            String fileName = soundLabel + ".wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(abecidu.getClass().getResourceAsStream(fileName)));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            if (looping) clip.loop(Integer.MAX_VALUE);
            clip.start();
        }catch(LineUnavailableException e){//there are too many sounds playing at once, simply skip the current sound}
        }catch(Exception e){
            abecidu.crash(e);
        }
    }

}
