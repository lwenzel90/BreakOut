package com.lwenzel.BreakOut;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.soap.SOAPPart;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BreakOut {

    // Main variables
    private JFrame frame;
    private java.util.Timer timer;
    private TimerTask renderTask;
    private GamePanel GamePanel;

    // Sound Variables
    private InputStream sound;
    private AudioInputStream audio;
    private Clip clip;

    // Window Variables
    private int windowWidth = 800;
    private int windowHeight = 600;
    public int getWindowWidth() {
        return windowWidth;
    }
    public int getWindowHeight() {
        return windowHeight;
    }

    // Input variables
    private boolean state = true;
    private boolean paused = false;
    private boolean escape = false;
    private boolean space = false;
    private boolean left = false;
    private boolean right = false;
    public void setPaused(boolean paused){
        this.paused = paused;
    }



    //  Variables
    private int score = 0;
    private int row = 5;
    private int col = 10;

    // getters and setters
    public int getScore() {
        return score;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    // Objects
    protected Box paddle = new Box(this, 70, 8);
    protected Ball ball = new Ball(this, 10, 10);
    protected Bricks brick = new Bricks(this, row, col);

    // Constructor
    private BreakOut() {
        GamePanel = new GamePanel(this);    // Create instance of drawing panel
        createFrame();  // Create frame
        init(); // Setup
        frame.setVisible(true); // Make frame visible after setup
        loop(); // Start repaint loop
    }

    // Main
    public static void main( String[ ] args ) {
        SwingUtilities.invokeLater(BreakOut::new);  // Call constructor
    }

    // Loop
    private void loop() {
        timer = new java.util.Timer();
        renderTask = new TimerTask() {
            @Override
            public void run() {
                // Logic
                pause();
                if (!paused) {
                    boxMovement(paddle);
                    ballMovement(ball);
                    hitDetection();
                    Loss();
                }
                // Repaint screen
                frame.repaint();

            }
        };
        timer.schedule(renderTask, 0, 10L);  // Redraw every 10 milliseconds
    }

    // Moving a "paddle"
    private void boxMovement(Box box) {
        if (left && box.getX() > 0) {
            box.addX(-7);
        }
        else if (right && box.getX() + box.getWidth() < windowWidth) {
            box.addX(7);
        }
    }

    private void ballMovement(Ball ball){
        if (ball.isStuck()) {
            ball.centerX(paddle.getX() + (paddle.getWidth() / 2));    // Set paddle position
            ball.setY(paddle.getY() - ball.getHeight());
            if (space){
                ball.setStuck(false);
                ball.addY( -1 );

            }
        }
        else {
            ball.addX(ball.getXvel());
            ball.addY(ball.getYvel());

        }
    }

    private void pause() {
        //isOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        if (escape && state) {
            state = false;
            if (!paused) {
                paused = true;
            }
            else if (paused) {
                paused = !paused;
            }
        }
        else if (!escape) {
            state = true;
        }
    }

    private void Loss(){
        if(ball.getY() > windowHeight){
            paused = true;
            GamePanel.setLose(true);
            brick.reset();
            paddle.centerX(windowWidth / 2);
            score = 0;
            ball.setStuck(true);

        }

    }

    private void hitDetection(){
        hitBricks(brick);
        hitWall();
        hitPaddle(paddle);
    }

    private void hitPaddle(Box box){
        //Q is a quadrant of the paddle the determinds a new way of moving
        //int centerXPos =  (ball.getX() + (ball.getWidth() / 2));

       // System.out.println(centerXPos);
        if (ball.getRectangle().intersects(box.getRectangle())){
            Random rand = new Random();
            //left half
            if(ball.getRectangle().intersects(box.getLeftHalf()) && !ball.getRectangle().intersects(box.getRightHalf())){
                ball.setXvel((rand.nextInt(2) + 3 ) * -1);
            }
            //right half
            else if(!ball.getRectangle().intersects(box.getLeftHalf()) && ball.getRectangle().intersects(box.getRightHalf())){
                ball.setXvel((rand.nextInt(2) + 3 ));
            }
            else{
                ball.setXvel((rand.nextInt(2) + 1) * (ball.getXvel() / Math.abs(ball.getXvel())));
            }
            ball.setYvel(Math.abs(ball.getYvel()) * -1);
            if (!ball.isStuck()){
                playSound(clip);
            }
        }
    }

    private void hitWall(){
        if (ball.getX() <= 0){
            ball.setXvel(Math.abs(ball.getXvel()));
        }
        else if (ball.getX() + ball.getWidth() >= windowWidth){
            ball.setXvel(Math.abs(ball.getXvel()) * -1 );
        }

        if (ball.getY() <= 0){
            ball.setYvel(Math.abs(ball.getYvel()));
        }
    }

    private boolean wincheck(){
        for (int i = 0; i < brick.getRow(); i++) {
            for(int j = 0 ; j < brick.getCol(); j++) {
                if (brick.getBox(i, j).exists()){
                   return false;
                }
            }
        }
        return true;
    }

    private void hitBricks(Bricks brick){
        boolean blockHit = false;
        for (int i = 0; i < brick.getRow(); i++) {
            for(int j = 0 ; j < brick.getCol(); j++) {

               if (brick.getBox(i, j).exists() && brick.getBox(i, j).getRectangle().intersects(ball.getRectangle())) {
                    /*  int temp, type = 1;
                    int area = 0;
                    temp = brick.getBox(i, j).getBottomRectangle().intersection(ball.getRectangle()).height * brick.getBox(i, j).getBottomRectangle().intersection(ball.getRectangle()).width;
                    if (temp > area) {
                        area = temp;
                    }
                    temp = brick.getBox(i, j).getTopRectangle().intersection(ball.getRectangle()).height * brick.getBox(i, j).getTopRectangle().intersection(ball.getRectangle()).width;
                    if (temp > area) {
                        area = temp;
                        type = 2;
                    }
                    temp = brick.getBox(i, j).getLeftRectangle().intersection(ball.getRectangle()).height * brick.getBox(i, j).getLeftRectangle().intersection(ball.getRectangle()).width;
                    if (temp > area) {
                        area = temp;
                        type = 3;
                    }
                    temp = brick.getBox(i, j).getRightRectangle().intersection(ball.getRectangle()).height * brick.getBox(i, j).getRightRectangle().intersection(ball.getRectangle()).width;
                    if (temp > area) {
                        area = temp;
                        type = 4;
                    }
                    switch (type) {
                        case 1:
                            ball.setYvel(ball.getYvel() * -1);
                            brick.getBox(i, j).setExists(false);
                            playSound(clip);
                            System.out.println("bottom");
                            break;
                        case 2:
                            ball.setYvel(ball.getYvel() * -1);
                            brick.getBox(i, j).setExists(false);
                            playSound(clip);
                            System.out.println("top");
                            break;
                        case 3:
                            ball.setXvel(ball.getXvel() * -1);
                            brick.getBox(i, j).setExists(false);
                            playSound(clip);
                            System.out.println("left");
                            break;
                        case 4:
                            ball.setXvel(ball.getXvel() * -1);
                            brick.getBox(i, j).setExists(false);
                            playSound(clip);
                            System.out.println("right");
                            break;
                    }
                    blockHit = true;
                    if(wincheck()){
                        paused = true;
                        JOptionPane.showMessageDialog(null, "You win!", "Breakout Boii", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    break;
                   */
                   score += 10;
                   if(ball.getRectangle().intersects(brick.getBox(i, j).getBottomRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getLeftRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getRightRectangle())){

                       ball.setYvel(Math.abs(ball.getYvel()));

                   }
                   else if (ball.getRectangle().intersects(brick.getBox(i, j).getTopRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getLeftRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getRightRectangle())){

                       ball.setYvel(Math.abs(ball.getYvel()) * -1);
                   }
                   else if (ball.getRectangle().intersects(brick.getBox(i, j).getLeftRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getTopRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getBottomRectangle())){

                       ball.setXvel(Math.abs(ball.getXvel()) * -1);

                   }
                   else if (ball.getRectangle().intersects(brick.getBox(i, j).getRightRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getTopRectangle())
                          && !ball.getRectangle().intersects(brick.getBox(i, j).getBottomRectangle())){

                       ball.setXvel(Math.abs(ball.getXvel()));

                   }

                   else if (ball.getRectangle().intersects(brick.getBox(i, j).getLeftRectangle()) && ball.getRectangle().intersects(brick.getBox(i, j).getBottomRectangle())){
                      ball.setXvel(Math.abs(ball.getXvel()) * -1);
                      ball.setYvel(Math.abs(ball.getYvel()));
                   }
                   else if (ball.getRectangle().intersects(brick.getBox(i, j).getLeftRectangle()) && ball.getRectangle().intersects(brick.getBox(i, j).getTopRectangle())){
                       ball.setXvel(Math.abs(ball.getXvel()) * -1);
                       ball.setYvel(Math.abs(ball.getYvel()) * -1);
                   }
                   else if (ball.getRectangle().intersects(brick.getBox(i, j).getRightRectangle()) && ball.getRectangle().intersects(brick.getBox(i, j).getBottomRectangle())){
                       ball.setXvel(Math.abs(ball.getXvel()));
                       ball.setYvel(Math.abs(ball.getYvel()));
                   }
                   else if (ball.getRectangle().intersects(brick.getBox(i, j).getRightRectangle()) && ball.getRectangle().intersects(brick.getBox(i, j).getTopRectangle())){
                       ball.setXvel(Math.abs(ball.getXvel()));
                       ball.setYvel(Math.abs(ball.getYvel()) * -1);
                   }
                   brick.getBox(i, j).setExists(false);
                   playSound(clip);
                   System.out.println("a thing");
                   blockHit = true;
                   if(wincheck()){
                       paused = true;
                       JOptionPane.showMessageDialog(null, "You win!", "Breakout Boii", JOptionPane.INFORMATION_MESSAGE);
                       System.exit(0);
                   }
                   break;
               }
            }
            if (blockHit) {
                break;
            }
        }
    }

    // Play sound
    private void playSound(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    // Setup
    private void init() {
        paddle.centerX(windowWidth / 2);    // Set paddle position
        paddle.centerY((int) ((float) windowHeight * (float) 0.85));
        ball.centerX(windowWidth / 2);    // Set paddle position
        ball.setY(paddle.getY() - ball.getHeight());
        try {
            sound = getClass().getResourceAsStream("/sound.wav");
            audio = AudioSystem.getAudioInputStream(new BufferedInputStream(sound));
            clip = AudioSystem.getClip();
            clip.open(audio);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error 404, sound not found.", "Breakoutboii", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    // Create JFrame
    private void createFrame() {
        frame = new JFrame("Break Out Boii");
        frame.getContentPane().add(GamePanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    left = true;
                } if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    right = true;
                } if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    space = true;
                } if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    escape = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    left = false;
                } if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    right = false;
                } if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    space = false;
                } if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    escape = false;
                }
            }
        });
    }
}
