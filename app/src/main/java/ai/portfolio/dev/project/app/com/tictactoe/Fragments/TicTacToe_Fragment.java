package ai.portfolio.dev.project.app.com.tictactoe.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.ITicTacToeFragment;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.GameEngine;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.Move;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.Player;
import ai.portfolio.dev.project.app.com.tictactoe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TicTacToe_Fragment extends Fragment implements ITicTacToeFragment {

    public static final String FRAGMENT_TAG = "TTT";
    //private GameEngine mGameEngine;
    private String mName;
    private TextView mCurrentMoveTV;


    private Player mPlayerOne, mPlayerTwo;
    private TableLayout tableLayout;
    private TextView mWinsTV;
    private TextView mLosesTV;
    private TextView mDrawsTV;
    private GameEngine mGameEngine;
    private Button[][] mButtons;

    public TicTacToe_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tic_tac_toe_gameboard,
                container, false);

        mWinsTV = (TextView)view.findViewById(R.id.wins_tv);
        mLosesTV = (TextView)view.findViewById(R.id.loses_tv);
        mDrawsTV = (TextView)view.findViewById(R.id.draws_tv);


        mPlayerOne = new Player("Gabriel","X");
        mPlayerOne.setColor(getActivity().getResources().getColor(R.color.colorPlayerOne));
        mPlayerTwo = new Player("Rover","O");
        mPlayerTwo.enableAI();
        mPlayerTwo.setColor(this.getActivity().getResources().getColor(R.color.colorPlayerTwo));

        mCurrentMoveTV = (TextView) view.findViewById(R.id.player_turn_tv);
        tableLayout = (TableLayout) view.findViewById(R.id.gameBoardTL);
        beginGame();

        return view;
    }

    private void beginGame() {
         mGameEngine =  newGame(this.getActivity(), mPlayerOne, mPlayerTwo);
         mButtons = attachButtonListeners();
         updateGui(mGameEngine.getmCurrentPlayer());
         updateScore(mGameEngine);
        if(mGameEngine.isAIMove()){
            Move move = mGameEngine.predictAIMove();
            mButtons[move.getRow()][move.getCol()].performClick();
        }
    }

    @Override
    public GameEngine newGame(Context context , Player pOne, Player pTwo) {
        tableLayout.removeAllViews();
        if(mGameEngine==null){
            mGameEngine= new GameEngine();
        }
        mGameEngine.newGame(pOne,pTwo);
        return mGameEngine;
    }

    @Override
    public Button[][] attachButtonListeners() {
         mButtons= new Button[3][3];
        for (int row = 0; row < 3; ++row) {
            TableRow tableRow = new TableRow(this.getActivity());
            for (int col = 0; col < 3; ++col) {
                final Move move = new Move(row, col);
                final Button button = new Button(this.getActivity());
                button.setText("");
                button.setTextAppearance(this.getActivity(),R.style.gameFontButton);
                mButtons[row][col] = button;
                mButtons[row][col].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.setText(mGameEngine.getmCurrentPlayer().getSymbol());
                        button.setTextColor(mGameEngine.getmCurrentPlayer().getColor());
                        button.setEnabled(false);
                        mGameEngine.makeMove(move);
                        Log.e("BOARD: ","\n"+mGameEngine.toString());

                        if (mGameEngine.isOver()) {
                            Player winner = mGameEngine.getWinner();
                            if(winner==null){
                                displayMessage("Game Over","It's a draw.").show();
                            }else{
                                displayMessage("Game Over",winner.getName()+" wins!!!").show();
                            }
                            mGameEngine.updateScore(winner);
                            updateScore(mGameEngine);
                            beginGame();

                        } else {
                            updateGui(mGameEngine.getmCurrentPlayer());
                            if(mGameEngine.isAIMove()){
                                Move move = mGameEngine.predictAIMove();
                                Log.e("Predicted Move:: ",move.toString()+" :: "+mGameEngine);
                                mButtons[move.getRow()][move.getCol()].performClick();
                            }
                        }
                    }


                });
                tableRow.addView(button);
            }
            tableLayout.addView(tableRow);
        }
        return mButtons;
    }
    private AlertDialog displayMessage(String title, String msg){
        return new AlertDialog.Builder(this.getActivity()).setTitle(title).setMessage(msg).create();
    }
    private void updateGui(Player player) {
        mCurrentMoveTV.setText(player.getName());
        mCurrentMoveTV.setTextColor(player.getColor());
    }
    private void updateScore(GameEngine ge){
        mWinsTV.setText("WINS: "+String.valueOf(ge.getmPlayerOneWins()));
        mLosesTV.setText("LOSES: "+String.valueOf(ge.getmPlayerTwoWins()));
        mDrawsTV.setText("DRAWS: "+String.valueOf(ge.getmDraws()));
    }
}
