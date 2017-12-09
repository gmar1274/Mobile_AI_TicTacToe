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
    public final int GAME_NOT_OVER = Integer.MIN_VALUE;
    private  int[][] board;
    private String HUMAN_NAME,AI_NAME;


    public enum DIFFICULTY {EASY, INTERMEDIATE, HARD}
    private int BEST_SCORE = 400;//absolute value
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
    public final int HUMAN_SYMBOL = 1, AI_SYMBOL = 2, EMPTY = Integer.MIN_VALUE, HUMAN_WIN = -400, AI_WIN = 400,TIE=1;
    private int HUMAN_SCORE, AI_SCORE;

    /**
     * initial setup
     */
    public TicTacToe() {
        this.currentTurn = null;
        this.difficulty = DIFFICULTY.HARD;
        this.maxDepth = this.maxDepth(this.difficulty);
        this.board = this.init();
        this.HUMAN_SCORE = 0;
        this.AI_SCORE = 0;
        this.HUMAN_NAME="Player";
        this.AI_NAME="Rover";
    }
    public TicTacToe(DIFFICULTY diff, PLAYER player) {
        super();
    }
    public AIMove makeAIMove() {
        long start = System.nanoTime();
        int depth = this.maxDepth(this.difficulty), score, ai_row = 0, ai_col = 0;
        int alpha = -this.BEST_SCORE, beta = this.BEST_SCORE;// assume alpha is worst move similarly beta best move(*Remember trying to maximize alpha and minimize beta)
        int best = -this.BEST_SCORE;//AI wants positive
        for (int row = 0; row < 3; row++) {// naively start at beginning spot
            for (int col = 0; col < 3; col++) {// and look for an available next move
                if (board[row][col] == EMPTY) {// if spot available *Recall (-infinity<==>empty) otherwise spot is taken.
                    board[row][col] = AI_SYMBOL; // make move on board and generate child node
                    score = min(depth - 1, alpha, beta);// recursively (Depth first search). Traverse the tree for best movea
                    if (score > best) {
                        ai_row = row;
                        ai_col = col;
                        best = score;
                    }
                    board[row][col] = EMPTY; // undo move
                    if (alpha >= beta) {//prune condition stop exploring child nodes
                        board[ai_row][ai_col] = AI_SYMBOL;
                        long elapsed_time = System.nanoTime() - start;
                        return new AIMove(ai_row,ai_col,elapsed_time);
                    }
                }
            }
        }
        board[ai_row][ai_col] = AI_SYMBOL;
        long elapsed_time = System.nanoTime() - start;
        return new AIMove(ai_row,ai_col, elapsed_time);
    }
    /**
     * Minimizer function using alpha-beta pruning
     *
     * @return score - which is the heuristic value
     */
    public int min(int depth, int alpha, int beta) {
        int best = 20000;//assume worst case
        int score;
        int utility_value = check4Winner();
        if (utility_value != GAME_NOT_OVER) return utility_value;
        if (depth == 0) return 0;//heuristic value
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == EMPTY) {
                    board[row][col] = HUMAN_SYMBOL; // make move
                    score = max(depth - 1, alpha, beta);
                    if (score < best) {//update best
                        best = score;
                        beta = best;//update beta
                    }
                    board[row][col] = EMPTY; // undo move
                    if (beta <= alpha) { return best; }
                }
            }
        }
        return best;
    }

    /**
     * Maximizer function using alpha beta pruning.
     *
     * @return score - heuristic value
     */
    public int max(int depth, int alpha, int beta) {
        int best = -20000;//assumed to be worst
        int score;
        int utility_value = check4Winner();
        if (utility_value != GAME_NOT_OVER) return utility_value;
        if (depth == 0) return this.evaluate();//heuristic value
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == EMPTY) {//if empty space
                    board[row][col] = AI_SYMBOL; // make move on (new game state aka child node)
                    score = min(depth - 1, alpha, beta);
                    if (score > best) {//update best
                        best = score;
                        alpha = best;//update alpha
                    }
                    board[row][col] = EMPTY; // undo move
                    if (alpha >= beta) { return best; } //prune
                }
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
                b[r][c] = EMPTY;
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


    public String getCurrentPlayerName() {
        return (this.currentTurn.equals(PLAYER.HUMAN)?HUMAN_NAME:AI_NAME);
    }

    public boolean isAIMove() {
        return this.currentTurn.equals(PLAYER.AI);
    }

    /**
     *
     * @param winner score
     * @return Readable displayble name
     */
    public String getWinner(int winner) {
        if(winner==HUMAN_WIN)return this.HUMAN_NAME;
        else if(winner==AI_WIN)return this.AI_NAME;
        else return  "TIE";
    }
    public boolean isOver() {
        return this.check4Winner()!= GAME_NOT_OVER;
    }

    /**
     * Lazy hack... ai move sets the position then i need to update the gui but gui checks if space is empty first
     * so i have to und to redo move...
     * @param row
     * @param col
     */
    public void undo(int row, int col) {
        this.board[row][col] = EMPTY;
    }
}
