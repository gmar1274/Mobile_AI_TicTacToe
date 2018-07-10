package ai.portfolio.dev.project.app.com.tictactoe.Objects;

/**
 * This class is a Move object for TicTacToe game.
 */
public class Move {
    private int row,col;

    public Move(int row,int col){
        this.row = row;
        this.col=col;
    }
    public Move getMove(){
        return this;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    @Override
    public String toString() {
        return "["+row+","+col+"]";
    }
}
