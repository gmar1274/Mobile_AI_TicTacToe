package ai.portfolio.dev.project.app.com.tictactoe.Objects;

/**
 * This class is a Player object for the TicTacToe Game.
 */
public class Player {
    private String name,symbol;
    private int color;
    private boolean isAI;


    public Player(String name,String symbol){
        this.name=name;
        this.symbol = symbol;
        this.isAI=false;
    }

    public Player() {
        this.name=this.symbol=null;
        this.isAI=false;
    }

    public String toString(){
        return this.name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    /**
     * The game engine uses an 2Dinteger array, int[][], so X:=1 and O:=2.
     * @return
     */
    public int getCode() {
        return symbol.toLowerCase().contains("x")?1:2;
    }

    public int getColor() {
        return color;

    }

    public void setColor(int color) {
        this.color = color;
    }
    public void enableAI(){
        this.isAI = true;
    }
    public void disableAI(){
        this.isAI = false;
    }
    public boolean isAI(){return this.isAI;}
    @Override
    public boolean equals(Object obj){
        return obj instanceof Player && ((Player)obj).symbol.hashCode()==this.symbol.hashCode();
    }

    public boolean isPlayerOne() {return this.getCode()==1;
    }
}
