package com.lwenzel.BreakOut;

import java.awt.*;

public class Box {

    // Variables and functions
    private BreakOut main;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean exists;
    private Color color;

    public void setExists(boolean exists) {
        this.exists = exists;
    }
    public boolean exists() {
        return exists;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void addX(int x) {
        this.x += x;
    }
    public void addY(int y) {
        this.y += y;
    }
    public void centerX(int x) {
        this.x = (x - (width / 2));
    }
    public void centerY(int y) {
        this.y = (y - (height / 2));
    }
    public void centerBox() {
        this.x = (main.getWindowWidth() / 2) - (width / 2);
        this.y = (main.getWindowHeight() / 2) - (height / 2);
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle getRectangle(){
        return new Rectangle(x, y, width, height);
    }
    public Rectangle getTopRectangle(){
        return new Rectangle(x, y, width, 1);
    }
    public Rectangle getBottomRectangle(){
        return new Rectangle(x, y + height - 1, width, 1);
    }
    public Rectangle getLeftRectangle(){
        return new Rectangle(x, y, 1, height);
    }
    public Rectangle getRightRectangle(){
        return new Rectangle(x + width - 1, y , 1, height);
    }
    public Rectangle getLeftHalf(){
        return new Rectangle(x, y, width / 2, height);
    }
    public Rectangle getRightHalf(){
        return new Rectangle((x + (width / 2)), y, width / 2, height);
    }
    // Constructor
    public Box(BreakOut main, int width, int height) {
        this.main = main;
        exists = true;
        this.width = width;
        this.height = height;
    }

}
