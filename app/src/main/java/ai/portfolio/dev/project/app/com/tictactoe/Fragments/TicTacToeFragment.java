package ai.portfolio.dev.project.app.com.tictactoe.Fragments;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import ai.portfolio.dev.project.app.com.tictactoe.BuildConfig;
import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.ITicTacToeFragment;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.GameEngine;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.Move;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.Player;
import ai.portfolio.dev.project.app.com.tictactoe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TicTacToeFragment extends Fragment implements ITicTacToeFragment {

    public static final String FRAGMENT_TAG = "TTT";
    private static final String GOOGLE_ARG = "Google";
    private static final String PLAYER_TWO_TAG ="PlayerTwo" ;

    private String mName;


    private Player mPlayerOne, mPlayerTwo;
    private TableLayout tableLayout;
    private TextView mWinsTV;
    private TextView mLosesTV;
    private TextView mDrawsTV;
    private GameEngine mGameEngine;
    private Button[][] mButtons;
    private TextView mPlayerOneTV;
    private TextView mPlayerTwoTV;


    public TicTacToeFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param account
     * @return A new instance of fragment TicTacToeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicTacToeFragment newInstance(GoogleSignInAccount account) {
        TicTacToeFragment fragment = new TicTacToeFragment();
        Bundle args = new Bundle();
        args.putParcelable(GOOGLE_ARG, account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String p_one_name = "PLAYER ONE";
        String p_two_name = "ROVER";
        if(BuildConfig.DEBUG)p_one_name="Gabriel";
        if(savedInstanceState != null){
            GoogleSignInAccount account = savedInstanceState.getParcelable(GOOGLE_ARG);
            p_one_name = account.getDisplayName();

            if(savedInstanceState.getString(PLAYER_TWO_TAG)!= null)p_two_name=savedInstanceState.getString(PLAYER_TWO_TAG);
        }else{
            Log.e("BUNDLE....","NULLLLLLLLLLLLL");
        }

        View view = inflater.inflate(R.layout.fragment_tic_tac_toe,
                container, false);

        mWinsTV = (TextView)view.findViewById(R.id.score_wins_tv);
        mLosesTV = (TextView)view.findViewById(R.id.score_losses_tv);
        mDrawsTV = (TextView)view.findViewById(R.id.score_ties_tv);


        mPlayerOne = new Player(p_one_name,"X");
        mPlayerOne.setColor(getActivity().getResources().getColor(R.color.colorPlayerOne));
        mPlayerTwo = new Player("Rover","O");
        mPlayerTwo.enableAI();
        mPlayerTwo.setColor(this.getActivity().getResources().getColor(R.color.colorPlayerTwo));

        mPlayerOneTV = (TextView) view.findViewById(R.id.player_one_tv);
        mPlayerOneTV.setText(p_one_name);
        
         mPlayerTwoTV = (TextView) view.findViewById(R.id.player_two_tv);
        mPlayerTwoTV.setText(p_two_name);

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
            tableRow.setWeightSum(1.0f);
            final float scale = getContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (150 * scale + 0.5f);
            tableRow.setMinimumHeight(pixels);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int col = 0; col < 3; ++col) {
                final Move move = new Move(row, col);
                final Button button = new Button(this.getActivity());
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
                button.setText("");
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 200);
                button.setTextAppearance(this.getActivity(),R.style.gameFontButton);
                mButtons[row][col] = button;
                mButtons[row][col].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.setText(mGameEngine.getmCurrentPlayer().getSymbol());
                        button.setTextColor(mGameEngine.getmCurrentPlayer().getColor());
                        button.setEnabled(false);
                        mGameEngine.makeMove(move);
                        if(BuildConfig.DEBUG) Log.e("BOARD: ","\n"+mGameEngine.toString());

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
                               if(BuildConfig.DEBUG) Log.e("Predicted Move:: ",move.toString()+" :: "+mGameEngine);
                                mButtons[move.getRow()][move.getCol()].performClick();
                            }
                        }
                    }


                });
                tableRow.addView(button);
            }
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
        return mButtons;
    }
    private AlertDialog displayMessage(String title, String msg){
        return new AlertDialog.Builder(this.getActivity()).setTitle(title).setMessage(msg)
                .setNeutralButton(android.R.string.ok, null).create();
    }

    /**
     * Highlight player textview
     * @param player
     */
    private void updateGui(Player player) {
        mPlayerOneTV.animate().cancel();
        mPlayerTwoTV.animate().cancel();

       if(player.isPlayerOne()){
           animate(mPlayerOneTV);
       }else{
           animate(mPlayerTwoTV);
       }
    }
    private void animate(final TextView view){
        view.animate().rotation(360).setInterpolator(new LinearInterpolator()).setDuration(1000).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(final android.animation.Animator animation) {
                view.animate().setListener(this); //It listens for animation's ending and we are passing this to start onAniationEnd method when animation ends, So it works in loop
                view.animate().rotation(360).setInterpolator(new LinearInterpolator()).setDuration(1000).setListener(this).start();
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
    }
    private void updateScore(GameEngine ge){
       mWinsTV.setText(String.valueOf(ge.getmPlayerOneWins()));
       mDrawsTV.setText(String.valueOf(ge.getmDraws()));
       mLosesTV.setText(String.valueOf(ge.getmPlayerTwoWins()));
    }
}
