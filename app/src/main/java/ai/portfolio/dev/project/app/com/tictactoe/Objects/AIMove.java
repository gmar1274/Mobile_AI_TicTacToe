package ai.portfolio.dev.project.app.com.tictactoe.Objects;

/**
 * Created by gabe on 12/2/2017.
 * Helper class for return a AI alpha beta move
 */

public class AIMove {
    private long elapsed_time;//in nano
    private int row,col;
    public AIMove(int row,int col, long et){
        this.row = row;
        this.col = col;
        this.elapsed_time = et;
    }

    public long getElapsed_time() {
        return elapsed_time;
    }

    public void setElapsed_time(long elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
