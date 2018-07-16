package ai.portfolio.dev.project.app.com.tictactoe.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
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


    private TableLayout tableLayout;
    private TextView mWinsTV;
    private TextView mLosesTV;
    private TextView mDrawsTV;
    private GameEngine mGameEngine;
    private Button[][] mButtons;
    private TextView mPlayerOneTV;
    private TextView mPlayerTwoTV;
    private ObjectAnimator objectAnimator;
    private ObjectAnimator objectAnimator2;
    private MediaPlayer mMediaPlayer;


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
        mMediaPlayer = MediaPlayer.create(this.getActivity(),R.raw.game_play);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        String p_one_name = "PLAYER ONE";
        String p_two_name = "ROVER";
        if(BuildConfig.DEBUG)p_one_name="Gabriel";
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.getActivity());
        if(account!=null) {
            p_one_name = account.getGivenName()+" "+account.getFamilyName();
        }

        View view = inflater.inflate(R.layout.fragment_tic_tac_toe,
                container, false);

        mWinsTV = (TextView)view.findViewById(R.id.score_wins_tv);
        mLosesTV = (TextView)view.findViewById(R.id.score_losses_tv);
        mDrawsTV = (TextView)view.findViewById(R.id.score_ties_tv);


        Player mPlayerOne = new Player(p_one_name,"X");
        mPlayerOne.setColor(getActivity().getResources().getColor(R.color.colorPlayerOne));
        Player  mPlayerTwo = new Player("Rover","O");
        mPlayerTwo.enableAI();
        mPlayerTwo.setColor(this.getActivity().getResources().getColor(R.color.colorPlayerTwo));

        mPlayerOneTV = (TextView) view.findViewById(R.id.player_one_tv);
        mPlayerOneTV.setText(p_one_name);
        
         mPlayerTwoTV = (TextView) view.findViewById(R.id.player_two_tv);
        mPlayerTwoTV.setText(p_two_name);

        tableLayout = (TableLayout) view.findViewById(R.id.gameBoardTL);
        beginGame(mPlayerOne,mPlayerTwo);

        return view;
    }

    private void beginGame(Player mPlayerOne,Player mPlayerTwo) {
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
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        int pad = 10;
        params.setMargins(pad,pad,pad,pad);
        for (int row = 0; row < 3; ++row) {//create rows
            TableRow tableRow = new TableRow(this.getActivity());
            for (int col = 0; col < 3; ++col) {//create columns
                final Move move = new Move(row, col);//associate a location for a move for the button pressed
                final Button button = new Button(this.getActivity());//create button for each tictactoe gameboard dynamically
                //button.setPadding(pad,pad,pad,pad);
                button.setLayoutParams(params);//set layout
                button.setTextAppearance(this.getActivity(),R.style.gameFontButton);//set custom font style
                button.setBackgroundColor(TicTacToeFragment.this.getContext().getColor(R.color.buttonBG));
                mButtons[row][col] = button;
                mButtons[row][col].setOnClickListener(new View.OnClickListener() {//attach game logic on button click
                    @Override
                    public void onClick(View v) {
                        MediaPlayer mp = MediaPlayer.create(TicTacToeFragment.this.getActivity(), R.raw.coin);
                        mp.start();
                        button.setText(mGameEngine.getmCurrentPlayer().getSymbol());//display current players move
                        button.setTextColor(mGameEngine.getmCurrentPlayer().getColor());//display the color of current player
                        button.setEnabled(false);//disable button
                        mGameEngine.makeMove(move);//update move on game board
                        if(BuildConfig.DEBUG) Log.e("BOARD: ","\n"+mGameEngine.toString());

                        if (mGameEngine.isOver()) {//check if game's over
                            final Player winner = mGameEngine.getWinner();// get winning player. Null if tie.
                            String msg = "";
                            if(winner==null){//it's a draw
                                msg= "It's a draw.";
                                mp = MediaPlayer.create(TicTacToeFragment.this.getActivity(), R.raw.loss);
                            }else{
                                if(winner.equals(mGameEngine.getmPlayerOne()))mp = MediaPlayer.create(TicTacToeFragment.this.getActivity(), R.raw.won);
                                else mp = MediaPlayer.create(TicTacToeFragment.this.getActivity(), R.raw.loss);
                                msg=winner.getName()+" wins!!!";
                            }
                            mp.start();
                            displayMessage("Game Over",msg)//display winner by dialog and add a listener to button
                                    .setCancelable(false).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//update and restart for a new game
                                            mGameEngine.updateScore(winner);
                                            updateScore(mGameEngine);
                                            beginGame(mGameEngine.getmPlayerOne(),mGameEngine.getmPlayerTwo());
                                        }
                                    }).create().show();



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
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT,1));
        }
        return mButtons;
    }
    private AlertDialog.Builder displayMessage(String title, String msg){
        return new AlertDialog.Builder(this.getActivity()).setTitle(title).setMessage(msg);
    }

    /**
     * Highlight player textview.
     * @see Player
     * Activate ObjectAnimator depending on player
     * @param player
     */
    private void updateGui(Player player) {

        if(objectAnimator==null)objectAnimator=animate(mPlayerOneTV);
        if(objectAnimator2==null)objectAnimator2= animate(mPlayerTwoTV);
        if(player.isPlayerOne()){
           objectAnimator.start();
           objectAnimator2.cancel();
       }else{
          objectAnimator2.start();
          objectAnimator.cancel();
       }

    }



    private ObjectAnimator animate( View view){
        int GLOW_ANIM_DURATION = 4000;

             ObjectAnimator objAnim =
                    ObjectAnimator.ofObject(view,
                            "backgroundColor", // we want to modify the backgroundColor
                            new ArgbEvaluator(), // this can be used to interpolate between two color values
                            view.getContext().getResources().getColor(R.color.white), // start color defined in resources as #ff333333
                            view.getContext().getResources().getColor(R.color.endColor) // end color defined in resources as #ff3355dd
                    );
            objAnim.setDuration(GLOW_ANIM_DURATION / 2);
            objAnim.setRepeatMode(ValueAnimator.REVERSE); // start reverse animation after the "growing" phase
            objAnim.setRepeatCount(ValueAnimator.INFINITE);
            return objAnim;
    }
    private void setShadowLayer (View view ,float radius, float dx, float dy, int color){

    }
    private void updateScore(GameEngine ge){
       mWinsTV.setText(String.valueOf(ge.getmPlayerOneWins()));
       mDrawsTV.setText(String.valueOf(ge.getmDraws()));
       mLosesTV.setText(String.valueOf(ge.getmPlayerTwoWins()));
    }

    @Override
    public void onResume(){
        super.onResume();
        mMediaPlayer.start();
    }
    @Override public void onPause(){
        super.onPause();
        mMediaPlayer.pause();
    }
}
