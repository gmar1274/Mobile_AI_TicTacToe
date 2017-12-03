package ai.portfolio.dev.project.app.com.tictactoe.Interfaces;

import ai.portfolio.dev.project.app.com.tictactoe.Objects.AIMove;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.TicTacToe;

/**
 * Created by gabe on 11/30/2017.
 */

public interface IMinmax {
    /**
     *
     * @param board - initial game state
     * @return AI move
     */
    AIMove AIMove(TicTacToe board);

    /**
     * Minimizer function for minmax algorithm
     * @param depth
     * @return
     */
    int min(int depth,int alpha,int beta);

    /**
     * Maximizer function for minmax algorithm
     * @param depth
     * @return
     */
    int max(int depth,int alpha,int beta);

    /**
     *
     * @return terminal state
     */
    int evaluate();

    /**
     *
     * @return gamestate
     */
    int check4Winner();
}
