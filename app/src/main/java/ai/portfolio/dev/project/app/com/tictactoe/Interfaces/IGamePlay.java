package ai.portfolio.dev.project.app.com.tictactoe.Interfaces;

import ai.portfolio.dev.project.app.com.tictactoe.Objects.TicTacToe;

/**
 * Created by gabe on 12/1/2017.
 */

public interface IGamePlay {
    /**
     * initialize game board
     * @return
     */
    int[][] init();

    /**
     * reset game board
     */
    TicTacToe restart(TicTacToe.PLAYER winner);

    /**
     * randomly pick player to start game first
     * @return
     */
    TicTacToe.PLAYER flipCoin();

    /**
     * Alter difficulty based on user input
     * @param diff
     */
    void setDifficulty(TicTacToe.DIFFICULTY diff);
}
