package com.company;

import java.util.ArrayList;

/**
 * the class for the smart player which implements the min - max algorithm
 */
public class MinMaxPlayer extends Player
{
    /**
     * index 0: the move (1, 3, 5, 7),
     * index 1: the move's tileId,
     * index 2: the collected points
     */
    ArrayList<int[]> path = new ArrayList<>();
    //the depth for the min-max algorithm to search
    int minMaxDepth = 2;

    //constructor
    MinMaxPlayer(int playerId, int playerTileId, int score, int x, int y, String name, Board board)
    {
        super(playerId, playerTileId, score, x, y, name, board);
    }

    /**
     * evaluate a given move
     * @param currentPos the current position of the player
     * @param dice the dice, whose evaluation will be calculated
     * @param opponentCurrentPos the current position of minotaur
     * @return the evaluation of the dice
     */
    int evaluate(int currentPos, int dice, int opponentCurrentPos)
    {
        //evaluation: the total evaluation to return at the end
        //placeHolder: the tileId the player will get if he moves at dice
        int evaluation = 0, placeHolder = currentPos + tileIfHeMoves(dice);
        //supplyDistance: the distance from the closest supply, 100 if it is bigger than 3
        int supplyDistance = calculateSupplyDistance(dice, currentPos);
        //opponentDistance: the distance from minotaur, 100 if it is bigger than 3
        int opponentDistance = calculateOpponentDistance(dice, currentPos, opponentCurrentPos);

        //if the player is about to go on a tile in which he has already been, decrease evaluation by 2 points for every time he's been there
        for(int i = path.size() - 1; i >= 0; i--)
        {
            if(path.get(i)[1] == placeHolder) evaluation -= 2;
        }

        //else if minotaur less than or equal to 3 tiles away, decrease evaluation by 20 - opponentDistance
        if(opponentDistance <= 3) evaluation -= 20 - opponentDistance;

        //else if there is a supply nearby, increase evaluation by 5 - supplyDistance
        if(supplyDistance < 3) evaluation += 5 - supplyDistance;

        return evaluation;
    }

    /**
     * implements the min - max algorithm
     * @param parent the parent of the node to check
     * @param depth the depth of the search at a given point
     * @param isMaxPlayer true if the moves to check are theseus', false otherwise
     * @return an array: index 1 -> the evaluation of that move, index 0 -> the best move
     */
    int[] chooseMinMaxMove(Node parent, int depth, boolean isMaxPlayer)
    {
        //return the array if the search has reached maximum depth
        if(depth == 0) return new int[]{parent.getNodeEvaluation(), parent.getNodeMove()};

        //for the maximising player
        if(isMaxPlayer)
        {
            //maxEvaluation: the evaluation for the comparisons
            int maxEvaluation = - 100;
            //placeHolder: the place holder for the node with the best evaluation
            Node placeHolder = new Node();

            //get the min - max move for every child
            for(Node child: parent.getChildren())
            {
                int evaluation = chooseMinMaxMove(child, depth - 1, false)[0];

                if(maxEvaluation < evaluation)
                {
                    maxEvaluation = evaluation;
                    placeHolder = child;
                }
            }

            //return the array
            return new int[]{maxEvaluation, placeHolder.getNodeMove()};
        }
        //for the minimising player
        else
        {
            //minEvaluation: the evaluation for the comparisons
            int minEvaluation = 100;
            //placeHolder: the place holder for the node with the best evaluation
            Node placeHolder = new Node();

            //get the min - max move for every child
            for(Node child: parent.getChildren())
            {
                int evaluation = chooseMinMaxMove(child, depth - 1, true)[0];

                if(minEvaluation > evaluation)
                {
                    minEvaluation = evaluation;
                    placeHolder = child;
                }
            }

            //return the array
            return new int[]{minEvaluation, placeHolder.getNodeMove()};
        }
    }

    /**
     * checks which of the available moves is the best with chooseMinMaxMove()
     * @param opponentCurrentPos the current position of the opponent
     * @return the best move
     */
    int getNextMove(int opponentCurrentPos)
    {
        //initialise the root of the tree
        Node root = new Node(null, 0, 0);

        //crete the subtrees
        createMySubTree(playerTileId, opponentCurrentPos, root, 1);
        //bestNode: the best move's node
        int[] bestNode = chooseMinMaxMove(root, minMaxDepth, true);

        //update path
        path.add(new int[]{bestNode[1], playerTileId + tileIfHeMoves(bestNode[1]), bestNode[0]});

        //return the best move
        return bestNode[1];
    }

    /**
     * create the subtree for every possible move of the player
     * @param opponentCurrentPos the current position of the opponent
     * @param parent the subtree's root
     * @param depth the depth of the node
     */
    void createMySubTree(int currentPos, int opponentCurrentPos, Node parent, int depth)
    {
        //walls: 0 if there is a wall, or 1, 3, 5, 7 otherwise
        int[] walls = new int[]{1, 3, 5, 7};

        //replace the element with 0 where a wall exists
        if(tiles[currentPos].getUp()) walls[0] = 0;
        if(tiles[currentPos].getDown()) walls[2] = 0;
        if(tiles[currentPos].getLeft()) walls[3] = 0;
        if(tiles[currentPos].getRight()) walls[1] = 0;

        //create the node for every possible move
        for(int theMove: walls)
        {
            //if there is a wall, don't add the node
            if(theMove == 0) continue;

            //placeHolder: the tile the player will get to if he moves at dice
            int placeHolder = currentPos + tileIfHeMoves(theMove);
            //child: the child to create
            Node child = new Node(parent, depth, theMove);

            //update the correct variables
            child.setNodeEvaluation(evaluate(placeHolder, theMove, opponentCurrentPos));
            parent.addChild(child);

            //create the opponent's subtree for this node
            createOpponentSubTree(placeHolder, opponentCurrentPos, child, depth + 1);
        }
    }

    /**
     * create the subtree for every possible move of the opponent
     * @param currentPos the current position of the player
     * @param opponentCurrentPos the current position of the opponent
     * @param parent the subtree's root
     * @param depth the rank of the node
     */
    void createOpponentSubTree(int currentPos, int opponentCurrentPos, Node parent, int depth)
    {
        //walls: 0 if there is a wall, or 1, 3, 5, 7 otherwise
        int[] walls = new int[]{1, 3, 5, 7};

        //remove the elements where a wall exists
        if(tiles[opponentCurrentPos].getUp()) walls[0] = 0;
        if(tiles[opponentCurrentPos].getDown()) walls[2] = 0;
        if(tiles[opponentCurrentPos].getLeft()) walls[3] = 0;
        if(tiles[opponentCurrentPos].getRight()) walls[1] = 0;

        //create the node for every possible move
        for(int theMove: walls)
        {
            //if there is a wall, don't add the node
            if(theMove == 0) continue;

            //placeHolder: the tile the opponent will get to if he moves at dice
            int placeHolder = opponentCurrentPos + tileIfHeMoves(theMove);
            //child: the child to create
            Node child = new Node(parent, depth, theMove);

            //update the correct variables
            child.setNodeEvaluation(evaluate(currentPos, theMove, placeHolder));
            parent.addChild(child);

            //create the player's subtree for this node
            if(depth < minMaxDepth) createMySubTree(currentPos, placeHolder, child, depth + 1);
        }
    }

    /**
     * calculate the tile in which the player will get, if he moves at dice
     * @param dice the direction to move to
     * @return the tile in which the player will get
     */
    int tileIfHeMoves(int dice)
    {
        return switch(dice)
        {
            case 1 -> board.getN();
            case 3 -> 1;
            case 5 -> - board.getN();
            case 7 -> - 1;
            default -> 0;
        };
    }

    /**
     * move the player around the board
     * @param opponentCurrentPos the current position of the opponent
     */
    public void move(int opponentCurrentPos)
    {
        System.out.println("[" + name + "]");

        int nextMove = getNextMove(opponentCurrentPos);

        playerTileId += tileIfHeMoves(nextMove);

        //move to the chosen direction (update the correct coordinate)
        switch(nextMove)
        {
            //up
            case 1 -> y++;
            //down
            case 5 -> y--;
            //left
            case 7 -> x--;
            //right
            case 3 -> x++;
        }

        System.out.println("x: " + x + ", y: " + y);

        //check if theseus collected a supply
        if(playerId == 0)
        {
            for(int i = 0; i < board.getS(); i++)
            {
                //if he did, print it, remove it and increase score
                if(supplies[i].getX() == x && supplies[i].getY() == y)
                {
                    score++;
                    System.out.println("Theseus collected a supply, with id " + (i + 1) + "!");

                    //x = y = tileId = - 1 is handled in getStringRepresentation()
                    supplies[i].setX(- 1);
                    supplies[i].setY(- 1);
                    supplies[i].setSupplyTileId(- 1);
                    break;
                }
            }
        }

        statistics(opponentCurrentPos, false);
    }

    /**
     * calculate the distance from the closest supply
     * @param dice the dice to calculate
     * @param currentPos the current position of the player
     * @return the distance calculated
     */
    int calculateSupplyDistance(int dice, int currentPos)
    {
        int distance = 100;

        for(int i = 0; i <= 3; i++)
        {
            //up
            if(dice == 1)
            {
                //for every supply
                for(int j = 0; j < board.getS(); j++)
                {
                    //if there is a wall, stop
                    if(tiles[currentPos + board.getN() * i].getUp())
                    {
                        i = 4;
                        break;
                    }
                    //else, calculate the distance
                    if(currentPos + board.getN() * i == supplies[j].getSupplyTileId()) distance = Math.min(distance, i);
                }
            }

            //down
            else if(dice == 5)
            {
                for(int j = 0; j < board.getS(); j++)
                {
                    //if there is a wall, stop
                    if(tiles[currentPos - board.getN() * i].getDown())
                    {
                        i = 4;
                        break;
                    }
                    //else, calculate the distance
                    if(currentPos - board.getN() * i == supplies[j].getSupplyTileId()) distance = Math.min(distance, i);
                }
            }

            //left
            else if(dice == 7)
            {
                for(int j = 0; j < board.getS(); j++)
                {
                    //if there is a wall, stop
                    if(tiles[currentPos - i].getLeft())
                    {
                        i = 4;
                        break;
                    }
                    //else, calculate the distance
                    if(currentPos - i == supplies[j].getSupplyTileId()) distance = Math.min(distance, i);
                }
            }

            //right
            else if(dice == 3)
            {
                for(int j = 0; j < board.getS(); j++)
                {
                    //if there is a wall, stop
                    if(tiles[currentPos + i].getRight())
                    {
                        i = 4;
                        break;
                    }
                    //else, calculate the distance
                    if(currentPos + i == supplies[j].getSupplyTileId()) distance = Math.min(distance, i);
                }
            }
        }

        return distance;
    }

    /**
     * calculate the distance from the opponent
     * @param dice the dice to calculate
     * @param opponentCurrentPos the current position of the opponent
     * @return the distance calculated
     */
    int calculateOpponentDistance(int dice, int currentPos, int opponentCurrentPos)
    {
        int distance = 100;

        for(int i = 0; i <= 3; i++)
        {
            //up
            if(dice == 1)
            {
                //if there is a wall, stop
                if(tiles[currentPos + board.getN() * i].getUp()) break;
                //else, calculate the distance
                if(currentPos + board.getN() * i == opponentCurrentPos)
                {
                    distance = i;
                    break;
                }
            }
            //down
            else if(dice == 5)
            {
                //if there is a wall, stop
                if(tiles[currentPos - board.getN() * i].getDown()) break;
                //else, calculate the distance
                if(currentPos - board.getN() * i == opponentCurrentPos)
                {
                    distance = i;
                    break;
                }
            }
            //left
            else if(dice == 7)
            {
                //if there is a wall, stop
                if(tiles[currentPos - i].getLeft()) break;
                //else, calculate the distance
                if(currentPos - i == opponentCurrentPos)
                {
                    distance = i;
                    break;
                }
            }
            //right
            else if(dice == 3)
            {
                //if there is a wall, stop
                if(tiles[currentPos + i].getRight()) break;
                //else, calculate the distance
                if(currentPos + i == opponentCurrentPos)
                {
                    distance = i;
                    break;
                }
            }
        }

        return distance;
    }

    /**
     * print information about the player's moves
     * @param opponentCurrentPos the current position of minotaur
     * @param isLastTime if it is the last time this method gets called
     */
    public void statistics(int opponentCurrentPos, boolean isLastTime)
    {
        //print a separator
        System.out.printf("%n%n[STATISTICS]%n");

        //if it is the last time this method gets called, print the correct information
        if(isLastTime)
        {
            int[] moveCount = new int[]{0, 0, 0, 0};

            for(int[] move: path)
            {
                switch(move[0])
                {
                    case 1 -> moveCount[0]++;
                    case 3 -> moveCount[1]++;
                    case 5 -> moveCount[2]++;
                    case 7 -> moveCount[3]++;
                }
            }

            System.out.println("Number of times the player went UP: " + moveCount[0]);
            System.out.println("Number of times the player went DOWN: " + moveCount[2]);
            System.out.println("Number of times the player went LEFT: " + moveCount[3]);
            System.out.println("Number of times the player went RIGHT: " + moveCount[1]);
        }

        //else if it not the last time, print the correct information
        else
        {
            int supplyDistance = 100, minotaurDistance = 100, suppliesCollected = 0;

            for(int i = 0; i < 4; i++) supplyDistance = Math.min(supplyDistance, calculateSupplyDistance(i * 2 + 1, playerTileId));
            for(int i = 0; i < 4; i++) minotaurDistance = Math.min(minotaurDistance, calculateOpponentDistance(i * 2 + 1, playerTileId, opponentCurrentPos));
            for(int i = 0; i < board.getS(); i++)
            {
                if(supplies[i].getX() == - 1 && supplies[i].getY() == - 1) suppliesCollected++;
            }

            System.out.println("Total supplies collected: " + suppliesCollected);
            System.out.println("Distance from closest supply: " + (supplyDistance == 100 ? "more than 3 tiles" : supplyDistance));
            System.out.println("Distance from Minotaur: " + (minotaurDistance == 100 ? "more than 3 tiles" : minotaurDistance));
        }

        //print a separator
        System.out.printf("%n%n");
    }
}