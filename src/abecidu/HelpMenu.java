package abecidu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelpMenu {
    private static Thread thread;
    private static boolean close = false;

    public static void show() throws IOException{
        thread = Thread.currentThread();

        String finalLine = "", currentLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new HelpMenu().getClass().getResourceAsStream("instructions")));
        while ((currentLine = reader.readLine()) != null) {
            finalLine += (currentLine);
        }


        JFrame jFrame = new JFrame("Abecidu Instructions");
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setLayout(new BorderLayout());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jLabel = new JLabel(finalLine);
        jFrame.add(jLabel, BorderLayout.CENTER);

        JButton button = new JButton("Start Game!");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread.interrupt();
            }
        });
        jFrame.add(button, BorderLayout.SOUTH);

        jFrame.setVisible(true);

        try{
            while(true){
                Thread.sleep(Long.MAX_VALUE);
            }
        }catch(InterruptedException e){/*time to continue launching game*/}
        jFrame.setVisible(false);
    }

}
