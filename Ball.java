package com.lwenzel.BreakOut;

import java.awt.*;

public class Ball extends Box {

    // Variables and functions
    private BreakOut main;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean exists;
    private boolean stuck = true;
    private boolean paused = false;
    private Color color;
    private int xvel;
    private int yvel;
    int centerX = getX() + (getWidth() / 2);

    public boolean isPaused() {
        return paused;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getXvel() {
        return xvel;
    }
    public void setXvel(int xvel) {
        this.xvel = xvel;
    }

    public int getYvel() {
        return yvel;
    }
    public void setYvel(int yvel) {
        this.yvel = yvel;
    }

    public boolean isStuck() {
        return stuck;
    }
    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }




    // Constructor
    public Ball(BreakOut main, int width, int height) {
        super(main, width, height);
        xvel = 3;
        yvel = -3;
    }

}
