package com.lwenzel.BreakOut;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GamePanel extends JPanel {

    // Variables
    private BufferedImage buffer;
    private BreakOut main;
    private Rectangle2D.Double paddle;
    private Rectangle2D.Double ball;
    private Rectangle2D.Double[][] bricklist;
    private Graphics2D g2;
    private int timer = 0;
    private boolean lose = false;
    private Color semiGrey = new Color(30, 30, 30, 255);
    private Color fullBlack = new Color(10, 10, 10, 255);
    public void setLose(boolean lose) {
        this.lose = lose;
    }



    // Constructor
    public GamePanel(BreakOut main) {
        this.main = main;
        paddle = new Rectangle2D.Double(1, 1, 1, 1);  // Create rectangle
        ball = new Rectangle2D.Double(1, 1, 1, 1);  // Create rectangle
        makeBricks(main.brick);
        setOpaque(false);
    }

    private void makeBricks(Bricks bricks){
        bricklist = new Rectangle2D.Double[bricks.getRow()][bricks.getCol()];
        for (int i = 0; i < bricks.getRow(); i++) {
            for(int j = 0 ; j < bricks.getCol(); j++) {
                bricklist[i][j] = new Rectangle2D.Double(1, 1, 1, 1);
            }
        }
    }

    private void drawBricks(Bricks bricks){
        for (int i = 0; i < bricks.getRow(); i++) {
            for(int j = 0 ; j < bricks.getCol(); j++) {
                if (bricks.getBox(i, j).exists()) {
                    // Gets the row of balls being added and then sets a color for a rainbow effect 
                    bricklist[i][j].setRect(bricks.getBox(i, j).getX(), bricks.getBox(i, j).getY(), bricks.getBox(i, j).getWidth(), bricks.getBox(i, j).getHeight()); // Set rectangle to "ball" bounds
                    if(i == 0){
                        g2.setColor(new Color(255, 29, 0, 255)); // Set color
                    }
                    else if (i == 1){
                        g2.setColor(new Color(255, 140, 0, 255)); // Set color
                    }
                    else if (i == 2){
                        g2.setColor(new Color(255, 255, 0, 255)); // Set color
                    }
                    else if (i == 3){
                        g2.setColor(new Color(102, 255, 3, 255)); // Set color
                    }
                    else if (i == 4){
                        g2.setColor(new Color(0, 141, 255, 255)); // Set color
                    }
                    else if (i == 5){
                        g2.setColor(new Color(23, 0, 255, 255)); // Set color
                    }
                    else if (i == 6){
                        g2.setColor(new Color(151, 0, 255, 255)); // Set color
                    }

                    g2.fill(bricklist[i][j]);  // Fill rectangle
                }
            }
        }
    }


    // Custom paint function
    protected void paintComponent(Graphics g) {

        // Setup
        super.paintComponent(g);
        buffer = new BufferedImage(main.getWindowWidth(), main.getWindowHeight(), BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) buffer.getGraphics();

        // Background
        GradientPaint gp = new GradientPaint(0, 0, semiGrey, 0, main.getWindowHeight(), fullBlack);
        g2.setPaint(gp);
        //g2.setColor(new Color(27, 27, 27, 255));   // Set color
        g2.fillRect(0, 0, main.getWindowWidth(), main.getWindowHeight());   // Fill background

        // Start
        paddle.setRect(main.paddle.getX(), main.paddle.getY(), main.paddle.getWidth(), main.paddle.getHeight()); // Set rectangle to "paddle" bounds
        g2.setColor(new Color(0, 255, 248, 255)); // Set color
        g2.fill(paddle);  // Fill rectangle

        ball.setRect(main.ball.getX(), main.ball.getY(), main.ball.getWidth(), main.ball.getHeight()); // Set rectangle to "ball" bounds
        g2.setColor(new Color(0, 255, 248, 255)); // Set color
        g2.fill(ball);  // Fill rectangle

        drawBricks(main.brick);

        if(lose){
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2.setColor(Color.red);
            g2.drawString("You Lost", 350, 300);
            timer++;
            if(timer == 100){
                timer = 0;
                main.setPaused(false);
                lose = false;
            }
        }
        else {
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2.setColor(Color.GREEN);
            g2.drawString("Score: " + main.getScore() + "/ " + (main.getRow() * main.getCol() * 10), main.getWindowWidth() - 150, main.getWindowHeight() - 50);
        }

        
        // End
        g.drawImage(buffer, 0, 0, getWidth(), getHeight(), this);
        g2.dispose();
        g.dispose();

    }

    // Needed, idk man
    public Dimension getPreferredSize() {
        return new Dimension(main.getWindowWidth(), main.getWindowHeight());
    }
}
