package it.unibo.breakout.model.impl;
import it.unibo.breakout.model.api.Brick;

public class BrickImpl implements Brick {

    private int life;
    private final boolean indestructible;
    private double x, y;
    private int width;
    private int height;
    private final int rowId;
    private int type;
    private final int colIndex;
    /**
     * @param x    horizontal position in pixels
     * @param y    vertical position in pixels
     * @param type brick type: 1 = normal, 2 = double-hit, 3 = indestructible
     */
    public BrickImpl(double x, double y, int type, int width, int height, int rowId, int colIndex) {
        this.x = x;
        this.y = y;
        this.rowId = rowId;
        this.type = type ;
        if (type == 3) {
            this.indestructible = true;
            this.life = 1;
        } else {
            this.indestructible = false;
            if (type == 4 || type == 5){
                this.life = 1 ;
            }
            else this.life = type;
        }
        this.width = width;
        this.height = height;
        this.colIndex = colIndex;
    }


    @Override
    public int getRowId() { return this.rowId; }

    /** Moves the brick down by the given amount of pixels. */
    @Override
    public void moveDown(double amount) {
        this.y += amount;
    }

    /** Decreases life by one if the brick is not indestructible. */
    @Override
    public void hit() {
        if (!indestructible) life--;
        if (type == 2) type = 1 ;
    }

    /** Returns true if the brick has no life left and is not indestructible. */
    @Override
    public boolean isDestroyed() {
        return !indestructible && life <= 0;
    }

    /** Returns the brick's X position. */
    @Override
    public double getX() {
        return this.x;
    }

    /** Returns the brick's Y position. */
    @Override
    public double getY() {
        return this.y;
    }

    /** Returns true if the brick cannot be destroyed. */
    @Override
    public boolean isIndestructible() {
        return this.indestructible;
    }

    @Override
    public int getWidth(){
        return this.width;
    }

    @Override
    public int getHeight(){
        return this.height;
    }

    @Override
    public int getLife(){ return this.life ;}

    @Override
    public int getType(){return this.type; }

    @Override
    public void setX(double x){
        this.x = x ;
    }

    @Override
    public void setWidth(int width){
        this.width = width;
    }

    @Override
    public void setY(double y){
        this.y = y ;
    }

    @Override
    public void setHeight(int height){
        this.height = height;
    }

    @Override
    public int getColIndex() { return this.colIndex; }
}
