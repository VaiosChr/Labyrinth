package com.company;

/**
 * the class for the tiles found in the board
 */
public class Tile
{
    int tileId, x, y;
    boolean up, down, left, right;

    //getters
    public int getTileId()
    {
        return tileId;
    }

    public boolean getUp()
    {
        return up;
    }

    public boolean getDown()
    {
        return down;
    }

    public boolean getLeft()
    {
        return left;
    }

    public boolean getRight()
    {
        return right;
    }

    //setters
    public void setTileId(int set)
    {
        tileId = set;
    }

    public void setX(int set)
    {
        x = set;
    }

    public void setY(int set)
    {
        y = set;
    }

    public void setUp(boolean set)
    {
        up = set;
    }

    public void setDown(boolean set)
    {
        down = set;
    }

    public void setLeft(boolean set)
    {
        left = set;
    }

    public void setRight(boolean set)
    {
        right = set;
    }

    //constructors
    public Tile(){}

    public Tile(int tileId, int x, int y, boolean up, boolean down, boolean left, boolean right)
    {
        this.tileId = tileId;
        this.x = x;
        this.y = y;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
}
