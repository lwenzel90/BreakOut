package com.lwenzel.BreakOut;
// Class for bricks that are loaded at the top of the screen
// Each brick is given a position and a box to account for hit 
// detection with the ball

public class Bricks {
    private Box[][] bricklist;
    private BreakOut main;
    private int row;
    private int col;



    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    public Bricks(BreakOut main, int row, int col){
        this.main = main;
        this.row = row;
        this.col = col;
        bricklist = new Box[row][col];
        for (int i = 0; i < row; i++) {
            for(int j = 0 ; j < col; j++) {
                bricklist[i][j] = new Box(main, 75, 15);

            }
        }
        setPos();
    }

    public void reset(){
        for (int i = 0; i < row; i++) {
            for(int j = 0 ; j < col; j++) {
                bricklist[i][j].setExists(true);
            }
        }
    }

    private void setPos(){
        int x = 20;
        int y = 70;

        for (int i = 0; i < row; i++) {
            for(int j = 0 ; j < col; j++) {
                bricklist[i][j].setX(x);
                bricklist[i][j].setY(y);
                x += bricklist[0][0].getWidth() + 1;
            }
            x = 20;
            y += bricklist[0][0].getHeight() + 1;
        }
    }

    public Box getBox(int row, int col){
        return bricklist[row][col];
    }


}
