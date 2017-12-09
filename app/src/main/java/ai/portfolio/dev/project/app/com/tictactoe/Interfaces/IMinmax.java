package ai.portfolio.dev.project.app.com.tictactoe.Interfaces;

import ai.portfolio.dev.project.app.com.tictactoe.Objects.AIMove;

/**
 * Created by gabe on 11/30/2017.
 */

public interface IMinmax {
    /**
     * @return AI move
     */
    AIMove makeAIMove();
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
     * @return terminal state
     */
    int evaluate();
    /**
     * @return gamestate
     */
    int check4Winner();
}
