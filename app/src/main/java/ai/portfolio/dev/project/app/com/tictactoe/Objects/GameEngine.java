package ai.portfolio.dev.project.app.com.tictactoe.Objects;


import java.util.Random;

import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.IDifficulty;
import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.IMinmax;
import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.ITicTacToe;

public class GameEngine implements ITicTacToe,IMinmax, IDifficulty {
    public final int GAME_NOT_OVER = 0;
    private DIFFICULTY mDifficultyLevel;
    private int BEST_SCORE = 400;//absolute value
    private int mGameBoard[][];

    private Player mPlayerOne, mPlayerTwo;
    private Player mCurrentPlayer;
    private int mPlayerOneWins, mPlayerTwoWins, mDraws;
    public GameEngine(){
        this.initGameBoard(mGameBoard);
        this.mPlayerOne = new Player();
        this.mPlayerTwo = new Player();
        this.mDifficultyLevel = DIFFICULTY.HARD;
        this.maxDepth = this.maxDepth(this.mDifficultyLevel);
    }

    @Override
    public void initGameBoard(int[][] board) {
        board = new int[3][3];
        for(int i=0; i <3; ++i){
            for (int j = 0; j<3;++j){
                board[i][j] = EMPTY;
            }
        }
        this.mGameBoard = board;
    }

    @Override
    public boolean makeMove(Move move) {
        int row=move.getRow();
        int col=move.getCol();
        if(mGameBoard[row][col]!=EMPTY)return false;//move not made
        mGameBoard[row][col]=mCurrentPlayer.getCode();
        this.mCurrentPlayer = this.mCurrentPlayer.equals(mPlayerOne)?mPlayerTwo:mPlayerOne;//next player
        return true;
    }

    /**
     * This method will initialize all the components needed to play a game.
     *
     * @param playerOne
     * @param playerTwo
     * @return The which player moves first.
     * @see Player
     */
    @Override
    public Player newGame(Player playerOne, Player playerTwo) {
        mPlayerOne=playerOne;
        mPlayerTwo=playerTwo;
        initGameBoard(mGameBoard);
       return flipCoin(playerOne,playerTwo);
    }

    private int depth;
    private int maxDepth;
    public final int EMPTY = Integer.MIN_VALUE, PLAYER_ONE_WIN = -400, PLAYER_TWO_WIN = 400,TIE=1;


    /**
     * MINMAX with AlphaBeta pruning algorithm
     * @see Move
     * @return The predicted best move.
     */
    /**
     * AI move using minmax with alpha beta pruning. Updates game board in GUI class. From given game state: (Maximizer on AI) - generate all possible moves (child) (branching
     * factor) - apply minmax on child node
     * - intial game state. {@value} location of move as int[]: array[0]=row, array[1]=column.
     * @return AIMOVE - int[]location and elapsed_time in nano seconds
     *
     */
    public Move predictAIMove() {
        int depth = getMaxDepth(mDifficultyLevel), score, ai_row = 0, ai_col = 0;
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;// assume alpha is worst move similarly beta best move(*Remember trying to maximize alpha and minimize beta)
        int best = -2000;//Integer.MIN_VALUE;//AI wants positive
        for (int row = 0; row < 3; row++) {// naively start at beginning spot
            for (int col = 0; col < 3; col++) {// and look for an available next move
                if (mGameBoard[row][col] == EMPTY) {// if spot available *Recall (-infinity<==>empty) otherwise spot is taken.
                    mGameBoard[row][col] = mPlayerTwo.getCode(); // make move on board and generate child node
                    score = min(depth - 1, alpha, beta);// recursively (Depth first search). Traverse the tree for best movea
                    if (score > best) {
                        ai_row = row;
                        ai_col = col;
                        best = score;
                    }
                    mGameBoard[row][col] =EMPTY; // undo move
                    if (alpha >= beta) {//prune condition stop exploring child nodes
                        //game_board[ai_row][ai_col] = COMPUTER_MOVE_SYMBOL;
                        return new Move(ai_row,ai_col);
                    }
                }
            }
        }
        //game_board[ai_row][ai_col] = COMPUTER_MOVE_SYMBOL;
        return new Move(ai_row,ai_col);
    }
    /**
     * Minimizer function using alpha-beta pruning
     *
     * @return score - which is the heuristic value
     */
    public int min(int depth, int alpha, int beta) {
        int best = 2000;//Integer.MAX_VALUE;//assume worst case
        int score;
        int utility_value = check4Winner();
        if (utility_value != GAME_NOT_OVER) return utility_value;
        if (depth == 0) return evaluate();//heuristic value
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (mGameBoard[row][col] == EMPTY) {
                    mGameBoard[row][col] = mPlayerOne.getCode(); // make move
                    score = max(depth - 1, alpha, beta);
                    if (score < best) {//update best
                        best = score;
                        beta = best;//update beta
                    }
                    mGameBoard[row][col] = EMPTY; // undo move
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
        int best = -2000;//Integer.MIN_VALUE;//assumed to be worst
        int score;
        int utility_value = check4Winner();
        if (utility_value != GAME_NOT_OVER) return utility_value;
        if (depth == 0) return this.evaluate();//heuristic value
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (mGameBoard[row][col] == EMPTY) {//if empty space
                    mGameBoard[row][col] = mPlayerTwo.getCode(); // make move on (new game state aka child node)
                    score = min(depth - 1, alpha, beta);
                    if (score > best) {//update best
                        best = score;
                        alpha = best;//update alpha
                    }
                    mGameBoard[row][col] = EMPTY; // undo move
                    if (alpha >= beta) { return best; } //prune
                }
            }
        }
        return best;
    }

    /**
     * The heuristic evaluation function for the current board
     *
     * @Return +200, +20, +2 for EACH 3-, 2-, 1-in-a-line for computer. -200, -20, -2 for EACH 3-, 2-, 1-in-a-line for opponent. 1 otherwise
     */
    public int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2); // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2); // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2); // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0); // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1); // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2); // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2); // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0); // alternate diagonal
        return score;
    }

    /**
     * The heuristic evaluation function for the given line of 3 mGameBoard
     *
     * @Return +200, +20, +2 for 3-, 2-, 1-in-a-line for computer. -200, -20, -2 for 3-, 2-, 1-in-a-line for opponent. 1 otherwise
     */
    private int evaluateLine(int cell1_row, int cell1_col, int cell2_row, int cell2_col, int cell3_row, int cell3_col) {
        int score = 0;
        // First cell
        if (mGameBoard[cell1_row][cell1_col] == 2) {
            score = 2;
        } else if (mGameBoard[cell1_row][cell1_col] == 1) {
            score = -2;
        } else {
            score = 1;
        }
        // Second cell
        if (mGameBoard[cell2_row][cell2_col] == 2) {
            if (score == 2) {
                score = 20;
            } else if (score == -2) {
                return 1;
            } else {
                score = 2;
            }
        } else if (mGameBoard[cell2_row][cell2_col] == 1) {
            if (score == -2) {//prev was player
                score = -20;
            } else if (score == 2) {
                return 1;
            } else {
                return -2;
            }
        }
        // Third cell
        if (mGameBoard[cell3_row][cell3_col] == 2) {
            if (score > 2) { // cell1 and/or cell2 is AI
                score *= 20;
            } else if (score < 1) { // cell1 and/or cell2 is player
                return 1;
            } else { // cell1 and cell2 are empty
                score = 2;
            }
        } else if (mGameBoard[cell3_row][cell3_col] == 1) {
            if (score < 1) { // cell and/or cell2 is player
                score *= 20;
            } else if (score > 2) { // cell1 and/or cell2 is AI
                return 1;
            } else { // cell1 and cell2 are empty
                score = -2;
            }
        }
        return score;
    }
    /**
     * @return gamestate
     */
    @Override
    public int check4Winner() {
        if((mGameBoard[0][0]==mPlayerOne.getCode() && mGameBoard[0][1]==mPlayerOne.getCode() && mGameBoard[0][2]==mPlayerOne.getCode())
                ||(mGameBoard[1][0]==mPlayerOne.getCode() &&mGameBoard[1][1]==mPlayerOne.getCode() &&mGameBoard[1][2]==mPlayerOne.getCode())
                ||(mGameBoard[2][0]==mPlayerOne.getCode() &&mGameBoard[2][1]==mPlayerOne.getCode() &&mGameBoard[2][2]==mPlayerOne.getCode())
                || (mGameBoard[0][0]==mPlayerOne.getCode() &&mGameBoard[1][1]==mPlayerOne.getCode() &&mGameBoard[2][2]==mPlayerOne.getCode())
                || (mGameBoard[0][2]==mPlayerOne.getCode() &&mGameBoard[1][1]==mPlayerOne.getCode() &&mGameBoard[2][0]==mPlayerOne.getCode())
                || (mGameBoard[0][0]==mPlayerOne.getCode() && mGameBoard[1][0]==mPlayerOne.getCode()&& mGameBoard[2][0]==mPlayerOne.getCode())
               || (mGameBoard[0][1]==mPlayerOne.getCode() && mGameBoard[1][1]==mPlayerOne.getCode()&& mGameBoard[2][1]==mPlayerOne.getCode())
                || (mGameBoard[0][2]==mPlayerOne.getCode() && mGameBoard[1][2]==mPlayerOne.getCode()&& mGameBoard[2][2]==mPlayerOne.getCode())
                ){ return PLAYER_ONE_WIN;}

          if((mGameBoard[0][0]==mPlayerTwo.getCode() &&mGameBoard[0][1]==mPlayerTwo.getCode() &&mGameBoard[0][2]==mPlayerTwo.getCode())
                ||(mGameBoard[1][0]==mPlayerTwo.getCode() &&mGameBoard[1][1]==mPlayerTwo.getCode() &&mGameBoard[1][2]==mPlayerTwo.getCode())
                ||(mGameBoard[2][0]==mPlayerTwo.getCode() &&mGameBoard[2][1]==mPlayerTwo.getCode() &&mGameBoard[2][2]==mPlayerTwo.getCode())
                || (mGameBoard[0][0]==mPlayerTwo.getCode() &&mGameBoard[1][1]==mPlayerTwo.getCode() &&mGameBoard[2][2]==mPlayerTwo.getCode())
                || (mGameBoard[0][2]==mPlayerTwo.getCode() &&mGameBoard[1][1]==mPlayerTwo.getCode() &&mGameBoard[2][0]==mPlayerTwo.getCode())
                  || (mGameBoard[0][0]==mPlayerTwo.getCode() && mGameBoard[1][0]==mPlayerTwo.getCode()&& mGameBoard[2][0]==mPlayerTwo.getCode())
        || (mGameBoard[0][1]==mPlayerTwo.getCode() && mGameBoard[1][1]==mPlayerTwo.getCode()&& mGameBoard[2][1]==mPlayerTwo.getCode())
        || (mGameBoard[0][2]==mPlayerTwo.getCode() && mGameBoard[1][2]==mPlayerTwo.getCode()&& mGameBoard[2][2]==mPlayerTwo.getCode())
                ){ return PLAYER_TWO_WIN;}

            for (int i=0;i<3;++i){
                for(int j=0;j<3;++j){
                    if(mGameBoard[i][j]==EMPTY)return GAME_NOT_OVER;
                }
            }
        return TIE;
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
     * Print console 3x3 mGameBoard output
     * @return
     */
    public String toString(){
        String s="";
        for (int i=0;i<3;++i){
            s+="[";
            for(int j=0;j<3;++j){
                s += this.mGameBoard[i][j] ==EMPTY?"-":this.mGameBoard[i][j];
            }
            s+="]\n";
        }
        return s;
    }

    @Override
    public void setDifficulty(DIFFICULTY diffLevel) {
        this.mDifficultyLevel=diffLevel;
    }

    @Override
    public Player flipCoin(Player pOne, Player pTwo) {
        this.mPlayerOne = pOne;
        this.mPlayerTwo = pTwo;
        Random rand = new Random();
        mCurrentPlayer = rand.nextBoolean()? mPlayerOne:mPlayerTwo;
        return mCurrentPlayer;
    }

    @Override
    public boolean isOver() {
        return check4Winner()!=GAME_NOT_OVER ;
    }

    @Override
    public Player getWinner() {
        int winner = check4Winner();
        if(winner==TIE)return null;
        return winner== PLAYER_ONE_WIN ?mPlayerOne:mPlayerTwo;
    }

    public Player getmCurrentPlayer() {
        return mCurrentPlayer;
    }

    public Player getmPlayerOne() {
        return mPlayerOne;
    }

    public Player getmPlayerTwo() {
        return mPlayerTwo;
    }

    public boolean isPlayerTwoMove() {
        return mCurrentPlayer.equals(mPlayerTwo);
    }

    public boolean isAvailable(Move move) {
        return mGameBoard[move.getRow()][move.getCol()]==EMPTY;
    }

    public boolean isAIMove() {
        return mCurrentPlayer.isAI();
    }
    public int getmPlayerOneWins() {
        return mPlayerOneWins;
    }

    public int getmPlayerTwoWins() {
        return mPlayerTwoWins;
    }

    public int getmDraws() {
        return mDraws;
    }

    public void setAI(Player mPlayerTwo) {
        this.mPlayerTwo=mPlayerTwo;
        this.mPlayerTwo.enableAI();
    }

    @Override
    public void updateScore(Player winner) {
        if(winner==null){
            mDraws += 1;
        }else if(winner.equals(mPlayerOne)){
            mPlayerOneWins += 1 ;
        }else {
            mPlayerTwoWins += 1;
        }
    }
    private int getMaxDepth(DIFFICULTY diff) {
        if(diff.equals(DIFFICULTY.EASY)){
            return 1;
        }else if(diff.equals(DIFFICULTY.INTERMEDIATE)){
            return 2;
        }else{
            return 9;
        }
    }
}
