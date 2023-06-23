//package brickBreakerr;
import javax.swing.JFrame;

public class Main {
    
    
    /** 
     * @param args
     */
    public static void main(String[] args){
        JFrame obj = new JFrame();
        Gameplay gamePlay = new Gameplay();
        obj.setBounds(10,10,710,600);
        obj.setTitle("BrickBreakerr");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }
    
}