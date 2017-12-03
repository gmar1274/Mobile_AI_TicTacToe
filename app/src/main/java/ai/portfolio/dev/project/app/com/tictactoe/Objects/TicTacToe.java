package ai.portfolio.dev.project.app.com.tictactoe.Objects;

import java.util.Random;

import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.IGamePlay;
import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.IMinmax;

/**
 * Created by gabe on 11/30/2017.
 * Game Board class.
 */

public class TicTacToe implements IMinmax, IGamePlay {


    private static final String AI_SYMBOL_GUI = "O";
    private static final String HUMAN_SYMBOL_GUI = "X";
    private final int GAME_NOT_OVER = Integer.MIN_VALUE;



    public enum DIFFICULTY {EASY, INTERMEDIATE, HARD}

    private int[][] board;

    public enum PLAYER {HUMAN, AI}


    /**
     * @param player
     * @return symbol for game board
     */
    private int getPlayerSymbol(PLAYER player) {
        return PLAYER.HUMAN.equals(player) ? HUMAN_SYMBOL : AI_SYMBOL;
    }

    private int depth;
    private PLAYER currentTurn;
    private DIFFICULTY difficulty;
    private int maxDepth;
    private int alpha, beta;
    private final int HUMAN_SYMBOL = 1, AI_SYMBOL = 2, EMPTY = Integer.MIN_VALUE, HUMAN_WIN = -5000, AI_WIN = 5000, TIE = 0;
    private int HUMAN_SCORE, AI_SCORE;

    /**
     * initial setup
     */
    public TicTacToe() {
        this.currentTurn = null;
        this.difficulty = null;
        this.maxDepth = this.maxDepth(this.difficulty);
        this.board = this.init();
        this.alpha = Integer.MIN_VALUE;
        this.beta = Integer.MAX_VALUE;
        this.HUMAN_SCORE = 0;
        this.AI_SCORE = 0;
    }

    public TicTacToe(DIFFICULTY diff, PLAYER player) {
        super();
    }

    /**
     * MinMax algorithm with alpha-beta pruning
     * @param board - initial game state
     * @return Game Board
     */
    @Override
    public AIMove AIMove(TicTacToe board) {
        int best = -20000;
        int score;
        int alpha = this.alpha;
        int beta = this.beta;
        int ai_row = 0, ai_col = 0;
        this.maxDepth = this.maxDepth(this.difficulty);
        long start_time = System.nanoTime();
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.board[row][col] = AI_SYMBOL;// min max on computer .. max score
                score = min(maxDepth - 1, alpha, beta);
                if (score > best) {
                    best = score;
                    ai_row = row;
                    ai_col = col;
                }
                this.board[row][col] = EMPTY;//undo move
                if (alpha >= beta) {
                    return new AIMove(ai_row, ai_col, System.nanoTime() - start_time);
                }
            }
        }
        return new AIMove(ai_row, ai_col, System.nanoTime() - start_time);
    }

    /**
     * Minimizer function for minmax algorithm
     *
     * @param depth
     * @return
     */
    @Override
    public int min(int depth, int alpha, int beta) {
        int best = 20000;
        int score;
        int utility = this.check4Winner();
        if (utility != GAME_NOT_OVER) return utility;
        if (depth == 0) return evaluate();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == EMPTY) {
                    this.board[i][j] = HUMAN_SYMBOL;//make human move
                }
                score = max(depth - 1, alpha, beta);
                if (score < best) {
                    beta = score;
                    best = score;
                }
                this.board[i][j] = EMPTY;//undoa
                if (alpha >= beta) return best;
            }
        }

        return best;

    }

    /**
     * Maximizer function for minmax algorithm
     *
     * @param depth
     * @return
     */
    @Override
    public int max(int depth, int alpha, int beta) {

        int best = -20000;
        int score;
        int utility = this.check4Winner();
        if (utility != GAME_NOT_OVER) return utility;
        if (depth == 0) return evaluate();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.board[i][j] == EMPTY) {
                    this.board[i][j] = AI_SYMBOL;
                }
                score = min(depth - 1, alpha, beta);
                if (score > best) {
                    alpha = score;
                    best = score;
                }
                this.board[i][j] = EMPTY;//undo
                if (alpha >= beta) return best;
            }
        }
        return best;

    }

    /**
     * @return terminal state
     */
    @Override
    public int evaluate() {
        return 0;
    }

    /**
     * @return gamestate
     */
    @Override
    public int check4Winner() {
        if (this.board[0][0] == HUMAN_SYMBOL && this.board[0][1] == HUMAN_SYMBOL && this.board[0][2] == HUMAN_SYMBOL ||
                this.board[1][0] == HUMAN_SYMBOL && this.board[1][1] == HUMAN_SYMBOL && this.board[1][2] == HUMAN_SYMBOL ||
                this.board[2][0] == HUMAN_SYMBOL && this.board[2][1] == HUMAN_SYMBOL && this.board[2][2] == HUMAN_SYMBOL ||
                this.board[0][0] == HUMAN_SYMBOL && this.board[1][1] == HUMAN_SYMBOL && this.board[2][2] == HUMAN_SYMBOL ||
                this.board[2][2] == HUMAN_SYMBOL && this.board[1][1] == HUMAN_SYMBOL && this.board[2][0] == HUMAN_SYMBOL
                ) {
            return HUMAN_WIN;
        } else if (this.board[0][0] == AI_SYMBOL && this.board[0][1] == AI_SYMBOL && this.board[0][2] == AI_SYMBOL ||
                this.board[1][0] == AI_SYMBOL && this.board[1][1] == AI_SYMBOL && this.board[1][2] == AI_SYMBOL ||
                this.board[2][0] == AI_SYMBOL && this.board[2][1] == AI_SYMBOL && this.board[2][2] == AI_SYMBOL ||
                this.board[0][0] == AI_SYMBOL && this.board[1][1] == AI_SYMBOL && this.board[2][2] == AI_SYMBOL ||
                this.board[2][2] == AI_SYMBOL && this.board[1][1] == AI_SYMBOL && this.board[2][0] == AI_SYMBOL
                ) {
            return AI_WIN;
        }
        for (int i = 0; i < 3; ++i) {//loop game board check if there is still spaces to check
            for (int j = 0; j < 3; ++j) {
                if (this.board[i][j] == EMPTY) return GAME_NOT_OVER;
            }
        }
        return TIE;//if spots all taken and no winner then tie.

    }

    int maxDepth(DIFFICULTY diff) {
        if(diff==null)return 9;
        switch (diff) {
            case EASY:
                return 1;
            case INTERMEDIATE:
                return 3;
            case HARD:
                return 9;
        }
        return 9;
    }

    /**
     * initialize game board
     *
     * @return
     */
    @Override
    public int[][] init() {
        int[][] b = new int[3][3];
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 3; ++c) {
                b[r][c] = Integer.MIN_VALUE;
            }
        }
        return b;
    }

    /**
     * reset game board
     */
    @Override
    public TicTacToe restart(PLAYER winner) {
        this.board = null;
        this.board = this.init();
        return this;
    }

    /**
     * randomly pick player to start game first
     *No animation
     * @return
     */
    @Override
    public PLAYER flipCoin() {
        Random rand = new Random();
        if(rand.nextBoolean()){
            return TicTacToe.PLAYER.HUMAN;
        }else{
            return TicTacToe.PLAYER.AI;
        }
    }

    public void setDifficulty(DIFFICULTY diff) {
        this.difficulty = diff;
        this.maxDepth = this.maxDepth(this.difficulty);
    }

    private PLAYER determinePlayer(PLAYER player) {
        switch (player) {
            case HUMAN:
                return PLAYER.HUMAN;
            case AI:
                return PLAYER.AI;
        }
        return null;
    }

    public void setPlayerTurn(PLAYER player) {
        this.currentTurn = player;
    }

    public PLAYER getCurrentPlayer() {
        return this.currentTurn;
    }

    public String getCurrentPlayerSymbol() {
        return this.getCurrentPlayer().equals(PLAYER.HUMAN) ? this.HUMAN_SYMBOL_GUI : this.AI_SYMBOL_GUI;
    }

    /**
     * Checks if spot is available
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isAvailable(int row, int col) {
        return this.board[row][col] != EMPTY;
    }

    /**
     * places a move
     *
     * @param row
     * @param col
     * @param player
     */
    public void makeMove(int row, int col, PLAYER player) {
        int move = this.getPlayerSymbol(player);
        this.board[row][col] = move;
    }
    public void nextTurn(){
        this.currentTurn = currentTurn.equals(PLAYER.HUMAN)?PLAYER.AI:PLAYER.HUMAN;
    }
    public String toString(){
        String s="";
        for (int i=0;i<3;++i){
            s+="[";
            for(int j=0;j<3;++j){
                s += this.board[i][j] ==EMPTY?"-":this.board[i][j];
            }
            s+="]";
        }
        return s;
    }
}
