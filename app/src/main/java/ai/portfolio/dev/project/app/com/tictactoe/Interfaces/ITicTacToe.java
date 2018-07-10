package ai.portfolio.dev.project.app.com.tictactoe.Interfaces;

import ai.portfolio.dev.project.app.com.tictactoe.Objects.Move;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.Player;

public interface ITicTacToe {
    /**
     * Intialize game board for new game
     * @param board
     */
    void initGameBoard(int[][] board);

    /**
     * @see Move
     * @param move
     * @return true if move was successfully made. False otherwise.
     */
    boolean makeMove(Move move);

    /**
     * This method will initialize all the components needed to play a game.
     * @param playerOne
     * @param playerTwo
     * @see Player
     * @return The which player moves first.
     */
    Player newGame(Player playerOne,Player playerTwo);

    /**
     * Randomly choose a player to move first
     * @param pOne
     * @param pTwo
     * @see Player
     * @return a random player to make first move
     */
    Player flipCoin(Player pOne, Player pTwo);

     boolean isOver();
     Player getWinner();

    /**
     * Updates the game engine and keeps track of the winning player.
     * @see Player
     * @param player if null means its a draw. Otherwise update player wins
     */
     void updateScore(Player player);
}
