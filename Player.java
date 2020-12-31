package com.company;

import java.util.Random;

/**
 * the class for the players of the game
 */
public class Player
{
    //playerId: 0 for theseus, 1 for minotaur
    public int playerId, playerTileId, score, x, y;
    String name;
    Board board;
    Tile[] tiles;
    Supply[] supplies;

    //getters
    public int getPlayerTileId()
    {
        return playerTileId;
    }

    public int getScore()
    {
        return score;
    }

    //setter
    public void setPlayerTileId(int set)
    {
        playerTileId = set;
    }

    Player(int playerId, int playerTileId, int score, int x, int y, String name, Board board)
    {
        this.playerId = playerId;
        this.playerTileId = playerTileId;
        this.score = score;
        this.x = x;
        this.y = y;
        this.name = name;
        this.board = board;
        this.tiles = board.getTiles();
        this.supplies = board.getSupplies();
    }


    /**
     * move the players randomly around the board
     * this method is overrided in heuristic player for theseus
     */
    public void move()
    {
        System.out.println("[" + name + "]");

        Random rand = new Random();
        int[] possibleMoves = new int[]{1, 3, 5, 7};
        int numberOfWalls = 0;

        //wherever there is a wall, the possibleMoves[index] becomes 0
        if(tiles[playerTileId].getUp()) possibleMoves[0] = 0;
        if(tiles[playerTileId].getDown()) possibleMoves[2] = 0;
        if(tiles[playerTileId].getLeft()) possibleMoves[3] = 0;
        if(tiles[playerTileId].getRight()) possibleMoves[1] = 0;

        //checks if the player can move
        for(int i = 0; i < 4; i++)
        {
            if(possibleMoves[i] == 0) numberOfWalls++;
        }
        if(numberOfWalls == 4) System.out.println("Player " + playerId + " can't move!");

        int randomNumber = rand.nextInt(4);

        //get random int of the possibleMoves array, that fulfills the requirements
        while(possibleMoves[randomNumber] == 0) randomNumber = rand.nextInt(4);

        //move to the chosen direction (update the correct coordinate)
        switch(possibleMoves[randomNumber])
        {
            //up
            case 1 ->
            {
                y++;
                playerTileId += board.getN();
            }
            //down
            case 5 ->
            {
                y--;
                playerTileId -= board.getN();
            }
            //left
            case 7 ->
            {
                x--;
                playerTileId -= 1;
            }
            //right
            case 3 ->
            {
                x++;
                playerTileId += 1;
            }
        }
        
        System.out.println("x: " + x + ", y: " + y);
    }
}
