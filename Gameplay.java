//package brickBreakerr;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Gameplay extends JPanel implements KeyListener, ActionListener, MouseMotionListener, MouseListener{
    private boolean play = false;
    private boolean win = true;
    private int spacePressed = 0;
    private boolean pause = false;


    private int totalBricks = 21;

    private Timer timer; //how fast the ball should move
    private int delay = 2;

    private int playerX = 310; // slider's starting position

    private int ballposX = (int) ((Math.random() * (690 - 10)) + 10); //starting pos
    private int ballposY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator map;

    private int temp1;
    private int temp2;

    public Gameplay(){
        map = new MapGenerator(3, 7);
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    
    /** 
     * @param g
     */
    public void paint(Graphics g){
        // background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        if(play == false && win == true && totalBricks == 21 && pause == false){
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 15));
            g.drawString("[click to start]", 290, 320);
        }

        if(play == true && win == true && totalBricks == 21){
            g.setColor(Color.black);
            g.setFont(new Font("SansSerif", Font.BOLD, 15));
            g.drawString("[click to start]", 290, 320);

        }

        if(pause == true && play == false){
            
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 15));
            g.drawString("[paused]", 310, 320);
            
            temp1 = ballXDir;
            temp2 = ballYDir;
        }
        
        
        if(pause == false && play == true && spacePressed > 0){
            g.setColor(Color.black);
            g.setFont(new Font("SansSerif", Font.BOLD, 15));
            g.drawString("[paused]", 310, 320);
        }
        

        //map
        map.draw((Graphics2D)g);

        //borders
        g.setColor(Color.white);
        g.fillRect(0,0,5,592);
        g.fillRect(0,0,692,5);
        g.fillRect(691,0,5,592);

        //paddle
        g.setColor(Color.white);
        g.fillRect(playerX, 550, 100, 8);
        //System.out.println("print: " + playerX);

        //ball
        g.setColor(Color.white);
        g.fillOval(ballposX, ballposY, 20, 20);

        if(ballposY > 570){
            win = false;
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            totalBricks = 0;

            map.loseDraw((Graphics2D)g);
            
            //paddle
            g.setColor(Color.red);
            g.fillRect(playerX, 550, 100, 8);

            g.setColor(Color.red);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            g.drawString("GAME OVER", 250, 300);

            g.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g.drawString("[press enter to restart]", 270, 320);

            /* //borders
            g.setColor(Color.red);
            g.fillRect(0,0,5,592);
            g.fillRect(0,0,692,5);
            g.fillRect(691,0,5,592); */
            
        }

        if(totalBricks == 0 && win == true){
            play = false;
            ballXDir = 0;
            ballYDir = 0;

            //paddle
            g.setColor(Color.green);
            g.fillRect(playerX, 550, 100, 8);

            //ball
            g.setColor(Color.green);
            g.fillOval(ballposX, ballposY, 20, 20);

            g.setColor(Color.green);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            g.drawString("WINNER", 275, 300);

            g.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g.drawString("[press enter to restart]", 262, 320);
        }

        g.dispose();

    }
    

    
    /** 
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();


        if(play){
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100,8))){
                ballYDir = -ballYDir;
            }


            A: for(int i=0; i<map.map.length; i++){
                for (int j=0; j< map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;

                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballposX += ballXDir;
            ballposY += ballYDir;
            
            if(ballposX<0){
                ballXDir = -ballXDir;
            }
            if(ballposY<0){
                ballYDir = -ballYDir;
            }
            if(ballposX>670){
                ballXDir = -ballXDir;
            }

            
        }
        

        repaint();
        
    }

    
    /** 
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 590){
                playerX = 590; // make sure its not out of border
            } else{
                moveRight();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10; // make sure its not out of border
            } else{
                moveLeft();
            }
            
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            spacePressed++;
            
            if(spacePressed % 2 != 0 && play == true){
                pause = true;
                play = false;
                //System.out.println("space 1 pressed; " + spacePressed);
            }

            if(spacePressed % 2 == 0 && play == false){
                pause = false;
                play = true;
                //System.out.println("space 2 pressed; " + spacePressed);

                ballXDir = temp1;
                ballYDir = temp2;
    
            }
            
            
        }

        

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                win = true;
                ballposX = (int) ((Math.random() * (690 - 10)) + 10);
                ballposY = 350;
                ballXDir = -1;
                ballYDir = -2;
                playerX = 310;
                totalBricks += 21;
                map = new MapGenerator(3, 7);
        
                temp1 = ballXDir;
                temp2 = ballYDir;
                
                repaint();
            }
        }
        
    }

    
    /** 
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if(play){
            boolean check = false;
        
            if(e.getX() >= 590){
                playerX = 590; // make sure its not out of border
                check = true;
                //System.out.println(playerX);
            } else{
                playerX = e.getX();
                //System.out.println("hit2");
            }

            if(e.getX() < 10){
                playerX = 10; // make sure its not out of border
            } else{
                playerX = e.getX();
            }
            
            /* if(check){
                System.out.println("check pass: " + playerX);

            }
            System.out.println("b4 print: " + playerX); */

            repaint();
        }    
    }

    
    /** 
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        play = true;
        //System.out.println(playerX);
    }

    public void moveRight(){
        play = true;
        playerX += 20;
    }
    public void moveLeft(){
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}


    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    
}
