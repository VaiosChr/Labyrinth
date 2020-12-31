package com.company;

/**
 * the class for the supplies found in the board
 */
public class Supply
{
    int supplyId, supplyTileId, x, y;

    //getters
    public int getSupplyId()
    {
        return supplyId;
    }

    public int getSupplyTileId()
    {
        return supplyTileId;
    }

    public int getX() {
        return x;
    }

    public int getY()
    {
        return y;
    }

    //setters
    public void setSupplyId(int set)
    {
        supplyId = set;
    }

    public void setX(int set)
    {
        x = set;
    }

    public void setY(int set)
    {
        y = set;
    }

    public void setSupplyTileId(int set)
    {
        supplyTileId = set;
    }

    //constructors
    public Supply(){}

    public Supply(int supplyId, int supplyTileId, int x, int y)
    {
        this.supplyId = supplyId;
        this.supplyTileId = supplyTileId;
        this.x = x;
        this.y = y;
    }
}
