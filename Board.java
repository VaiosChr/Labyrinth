package com.company;

import java.util.Random;

/**
 * the class for the board of the game
 */
public class Board
{
    int N, S, W;
    Tile[] tiles;
    Supply[] supplies;

    //getters
    public int getN()
    {
        return N;
    }

    public int getS()
    {
        return S;
    }

    public Tile[] getTiles()
    {
        return tiles;
    }

    public Supply[] getSupplies()
    {
        return supplies;
    }

    //constructor
    public Board(int N, int S, int W)
    {
        this.N = N;
        this.S = S;
        this.W = W;

        tiles = new Tile[N * N];
        java.util.Arrays.fill(tiles, new Tile());

        supplies = new Supply[S];
        java.util.Arrays.fill(supplies, new Supply());

        for(int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                int k = N * i + j;
                //initialising the tile with tileId = - 1, x = - 1 and y = - 1
                //and initialising it to its correct values afterwards
                //up, down, left and right have to initially be false
                tiles[k] = new Tile(- 1, - 1, - 1, false, false, false, false);
                tiles[k].setTileId(N * (N - i - 1) + j);
                tiles[k].setX(j);
                tiles[k].setY(N - i - 1);
            }
        }
    }

    /**
     * initialise every wall of the game
     */
    public void createTile()
    {
        //counter: the number of walls added at a given point
        int counter = 0;

        //creation of the outside walls, in total (4 * N - 1) walls
        for(int i = 0; i < N; i++)
        {
            //up wall
            tiles[N * (N - 1) + i].setUp(true);
            //down wall
            tiles[i].setDown(true);
            //left wall
            tiles[N * i].setLeft(true);
            //right wall
            tiles[N * (i + 1) - 1].setRight(true);
        }
        counter += 4 * N - 1;

        //creation of the random inside walls, in total (W - 4 * N + 1) walls
        for(; counter < W; counter++)
        {
            //rand: the random generator
            Random rand = new Random();
            //index: the randomly chosen index of the tiles[], in which the check -if a wall can be added- begins
            //wall: the index of the first wall that gets checked (0 for up, 1 for left)
            //corrector: the number of times the following while loop runs, if corrector > N * N, break it
            int index = - 1, wall, corrector = 0;
            //up: if the above tile is available to add a wall
            //left: if the left tile is available to add a wall
            boolean up = false, left = false;

            //this loop runs until it finds a tile, in which a wall can be added
            //selects randomly a direction (up, down, left, right)
            //checks if a wall can be added in the selected tile and
            //if yes, the respective variable becomes true and the loop exits
            //if not, it cycles through the neighboring tiles (up -> left -> down -> right)
            //if none of the neighboring tiles is available to add a wall, it finds another tile, randomly
            while(!(up || left || corrector > N * N))
            {
                corrector++;
                up = false;
                //down = false;
                left = false;
                //right = false;

                do index = rand.nextInt(N * N - 1);
                while(hasMoreThanOneWalls(index));

                wall = rand.nextInt(2);
                wall = (wall + 1) % 2;
                //up
                if(wall == 0)
                {
                    if(index < N * (N - 1))
                    {
                        if(!hasMoreThanOneWalls(index + N))
                        {
                            up = true;
                            break;
                        }
                    }
                }
                //left
                else if(wall == 1)
                {
                    if(index % N != 0)
                    {
                        if(!hasMoreThanOneWalls(index - 1))
                        {
                            left = true;
                            break;
                        }
                    }
                }

            }

            //when the tile is finally chosen, the wall gets added
            if(up)
            {
                tiles[index].setUp(true);
                //add a wall to the bottom part of the above tile as well
                tiles[index + N].setDown(true);
            }
            else if(left)
            {
                tiles[index].setLeft(true);
                //add a wall to the right part of the left tile as well
                tiles[index - 1].setRight(true);
            }
        }
    }

    /**
     * initialise every supply of the game
     */
    public void createSupply()
    {
        Random rand = new Random();
        int x, y;
        boolean foundPosition;

        for(int i = 0; i < S; i++)
        {
            do
            {
                foundPosition = false;
                x = rand.nextInt(N - 1);
                y = rand.nextInt(N - 1);

                //if the selected coordinates are the starting ones of theseus or minotaur, find new ones
                if((x == 0 && y == 0) || (x == N / 2 && y == N / 2)) continue;

                //if the selected coordinates are the same as another supply, find new ones
                for(int j = 0; j < i; j++)
                {
                    if(supplies[j].getX() == x && supplies[j].getY() == y)
                    {
                        foundPosition = true;
                        break;
                    }
                }
                foundPosition = !foundPosition;
            }
            while(!foundPosition);

            supplies[i] = new Supply(i + 1, x + N * y, x, y);
        }
    }

    /**
     * check if a given tile (tiles[index]) has more than, or equal to, two walls
     * @param index the index to the tiles array
     * @return true if it has, or false otherwise
     */
    public boolean hasMoreThanOneWalls(int index)
    {
        //check if the tile has more than one wall
        int temp = 0;

        if(tiles[index].getUp()) temp++;
        if(tiles[index].getDown()) temp++;
        if(tiles[index].getLeft()) temp++;
        if(tiles[index].getRight()) temp++;

        return temp >= 2;
    }

    /**
     * initialise the 2D String array
     * @param theseusTile the theseus' tileId
     * @param minotaurTile the minotaur's tileId
     * @return the 2D String array
     */
    public String[][] getStringRepresentation(int theseusTile, int minotaurTile)
    {
        //boardToString: the 2D String array to return
        String[][] boardToString = new String[2 * N + 1][N + 1];

        //initialise every object of boardToString to "+   " and change it afterwards
        for(int i = 0; i < 2 * N; i++) java.util.Arrays.fill(boardToString[i], "+   ");

        //add every wall to boardToString
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
                //draw the walls
                //horizontal
                if(tiles[(N - i - 1) * N + j].getUp()) boardToString[2 * i][j] = "+---";
                else boardToString[2 * i][j] = "+   ";
                //vertical
                if(tiles[(N - i - 1) * N + j].getLeft()) boardToString[2 * i + 1][j] = "|   ";
                else boardToString[2 * i + 1][j] = "    ";
            }
            //draw the rightmost '+'
            boardToString[2 * i][N] = "+";
        }

        //draw the down wall
        for(int j = 1; j < N; j++) boardToString[2 * N][j] = "+---";
        boardToString[2 * N][0] = "+   ";

        //draw the right corners' '+'
        boardToString[0][N] = "+";
        boardToString[2 * N][N] = "+";

        //draw the right outside wall
        for(int i = 0; i < N; i++) boardToString[2 * i + 1][N] = "|";

        //draw the supplies
        for(int i = 0; i < S; i++)
        {
            int x = supplies[i].getX();
            int y = supplies[i].getY();

            if(supplies[i].getSupplyTileId() == - 1) continue;

            if(tiles[supplies[i].getSupplyTileId()].getLeft()) boardToString[2 * (N - y) - 1][x] = "|";
            else boardToString[2 * (N - y) - 1][x] = " ";
            boardToString[2 * (N - y) - 1][x] += "s" + (i + 1);
            if(supplies[i].getSupplyId() <= 9) boardToString[2 * (N - y) - 1][x] += " ";
        }

        //draw the players
        //theseus
        if(theseusTile != - 1)
        {
            if(tiles[theseusTile].getLeft()) boardToString[2 * (N - (theseusTile / N)) - 1][theseusTile % N] = "|";
            else boardToString[2 * (N - theseusTile / N) - 1][theseusTile % N] = " ";
            boardToString[2 * (N - theseusTile / N) - 1][theseusTile % N] += " T ";
        }
        //minotaur
        if(tiles[minotaurTile].getLeft()) boardToString[2 * (N - (minotaurTile / N)) - 1][minotaurTile % N] = "|";
        else boardToString[2 * N - 2 * (minotaurTile / N) - 1][minotaurTile % N] = " ";
        boardToString[2 * N - 2 * (minotaurTile / N) - 1][minotaurTile % N] += " M ";

        return boardToString;
    }
}
