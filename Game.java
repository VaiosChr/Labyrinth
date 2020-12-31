//HEADLINE
//Τζιώρτζης Νικόλαος (Tiortzis Nikolaos), ΑΕΜ: 10081, τηλέφωνο: 6947172135, email: nikotzio@ece.auth.gr
//Τσιαλός Αναστάσιος (Tsialos Anastasios), ΑΕΜ:10253, τηλέφωνο: 6980386613 email: tsialosa@ece.auth.gr
//Χριστοδούλου Βάιος (Christodoulou Vaios), ΑΕΜ: 9987, τηλέφωνο: 6975658015 email: vkchrist@ece.auth.gr

package com.company;

/**
 * the main class of the game
 */
public class Game
{
    /**
     * print boardToString at the console
     * @param boardToString the 2D String array
     * @param N the size of the board
     */
    public static void printBoard(String[][] boardToString, int N)
    {
        for(int i = 0; i < 2 * N + 1; i++)
        {
            for(int j = 0; j < N + 1; j++) System.out.print(boardToString[i][j]);
            System.out.printf("%n");
        }
    }

    public static void main(String[] args)
    {
        //N: the size of the board
        //n: the number of times each player plays before draw
        //round: the round of the game
        //winner: the id of the player who wins, - 1 if the game ends in a draw
        int N = 15, S = 4, n = 100, round = 1, winner = - 1;

        //board:the board of the game
        var board = new Board(N, S, N * (N + 2));

        //boardToString: the board represented as a 2D String array
        String[][] boardToString;

        //initialise the board
        board.createTile();
        board.createSupply();

        //initialise the players
        //theseus: theseus' Player object
        var theseus = new MinMaxPlayer(0, 0, 0, 0, 0, "Theseus", board);
        //minotaur: minotaur's Player object
        var minotaur = new Player(1, N / 2 * (N + 1), 0, N / 2, N / 2, "Minotaur", board);

        //print the starting board
        System.out.printf("%nWelcome to the game!%n");
        boardToString = board.getStringRepresentation(theseus.getPlayerTileId(), minotaur.getPlayerTileId());
        printBoard(boardToString, N);

        //move the players
        while(round <= 2 * n)
        {
            //print a separator
            System.out.printf("%n%n%n%n");

            //theseus' move
            System.out.println("<--- Round: " + round + " --->");
            theseus.move(minotaur.getPlayerTileId());

            //update boardToString
            boardToString = board.getStringRepresentation(theseus.getPlayerTileId(), minotaur.getPlayerTileId());
            printBoard(boardToString, N);

            round++;

            //check if the game is over
            if(theseus.getPlayerTileId() == minotaur.getPlayerTileId())
            {
                winner = 1;
                theseus.setPlayerTileId(- 1);
                break;
            }
            if(theseus.getScore() == S)
            {
                winner = 0;
                break;
            }

            //print a separator
            System.out.printf("%n%n%n%n");

            //minotaur's move
            System.out.println("<--- Round: " + round + " --->");
            minotaur.move();

            //update boardToString
            boardToString = board.getStringRepresentation(theseus.getPlayerTileId(), minotaur.getPlayerTileId());
            printBoard(boardToString, N);

            round++;

            //check if the game is over
            if(theseus.getPlayerTileId() == minotaur.getPlayerTileId())
            {
                winner = 1;
                theseus.setPlayerTileId(- 1);
                break;
            }
        }

        //print a separator
        System.out.printf("%n%n%n%n");
        System.out.println("Game over!");

        //print the winner
        if(winner == 0) System.out.println("Theseus won!");
        else if(winner == 1) System.out.println("Minotaur won!");
        else System.out.println("Nobody won... It's a tie.");

        //print statistics
        theseus.statistics(minotaur.getPlayerTileId(), true);

        boardToString = board.getStringRepresentation(theseus.getPlayerTileId(), minotaur.getPlayerTileId());
        printBoard(boardToString, N);
    }
}
