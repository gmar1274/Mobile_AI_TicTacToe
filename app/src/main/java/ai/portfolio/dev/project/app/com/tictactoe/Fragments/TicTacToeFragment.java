package ai.portfolio.dev.project.app.com.tictactoe.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import ai.portfolio.dev.project.app.com.tictactoe.Activities.MainActivity;
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
    private String ai_difficulty;


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

        mMediaPlayer = MediaPlayer.create(this.getActivity(),R.raw.game_play);// begin game play music
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());//get persistent preferences from FragmentPreferences

        String p_one_name =  spref.getString(getString(R.string.pref_player_name_one_key),getString(R.string.pref_default_display_name));//get saved name otherwise default
        String p_two_name = spref.getString(getString(R.string.pref_player_name_two_key),getString(R.string.AiName));//get saved named otherwise default

         ai_difficulty = spref.getString(getString(R.string.pref_difficulty_key),null);

        boolean isAiEnabled = spref.getBoolean(getString(R.string.pref_single_player_mode_key),true);//AI is enabled by default otherwise users option

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
        Player  mPlayerTwo = new Player(p_two_name,"O");
        mPlayerTwo.setSinglePlayerMode(isAiEnabled);//true if AI is enabled
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
         if(mGameEngine.isAIEnabled()){mGameEngine.getDifficultyFromString(ai_difficulty);}
         mButtons = attachButtonListeners();
         resetBG(mPlayerOneTV);
         resetBG(mPlayerTwoTV);
         updateGui(mGameEngine.getmCurrentPlayer());
         updateScore(mGameEngine);
        if(mGameEngine.isAIMove()){
            Move move = mGameEngine.predictAIMove();
            mButtons[move.getRow()][move.getCol()].performClick();
        }
    }


    private void resetBG(View view) {
        view.setBackgroundColor(this.getActivity().getResources().getColor(R.color.transparent));
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

    /**This method dynamically creates buttons for mTableLayout.
     * Dynamically create Table Rows with Buttons that will contain game logic on click.
     *
     * @return dynamically create buttons with listeners to detect game moves.
     */
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
                button.setBackgroundColor(TicTacToeFragment.this.getContext().getResources().getColor(R.color.buttonBG));
                mButtons[row][col] = button;
                mButtons[row][col].setOnClickListener(new View.OnClickListener() {//attach game logic on button click
                    @Override
                    public void onClick(View v) {
                        playSoundMove();
                        button.setText(mGameEngine.getmCurrentPlayer().getSymbol());//display current players move
                        button.setTextColor(mGameEngine.getmCurrentPlayer().getColor());//display the color of current player
                        button.setEnabled(false);//disable button
                        mGameEngine.makeMove(move);//update move on game board
                        if(BuildConfig.DEBUG) Log.e("BOARD: ","\n"+mGameEngine.toString());

                        if (mGameEngine.isOver()) {//check if game's over
                                gameOver();
                        } else {
                            updateGui(mGameEngine.getmCurrentPlayer());
                            if(mGameEngine.isAIMove()){
                                Move move = mGameEngine.predictAIMove();
                               //if(BuildConfig.DEBUG) Log.e("Predicted Move:: ",move.toString()+" :: "+mGameEngine);
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



    private void gameOver() {
        final Player winner = mGameEngine.getWinner();// get winning player. Null if tie.
        String msg = "";
        if(winner==null){//it's a draw
            msg= "It's a draw.";
            playSoundLoss();
        }else{
            if(winner.equals(mGameEngine.getmPlayerOne())){
                msg=winner.getName()+" wins!!!";
                playSoundWin();
            }else {
                playSoundLoss();
            }
        }
        displayDialog("Game Over",msg,winner);//display winner by dialog and add a listener to button
    }
    /**
     *Prepare, play, release, nullify upon usage.
     */
    private void playSoundMove() {
        MediaPlayer mMediaPlayerMove = MediaPlayer.create(this.getActivity(), R.raw.move);
        mMediaPlayerMove.start();
        mMediaPlayerMove.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    /**
     * Prepare, start, release, nullify ,repeat
     */
    private void playSoundWin() {
        MediaPlayer mMediaPlayerGameOverWin = MediaPlayer.create(this.getActivity(), R.raw.win);
        mMediaPlayerGameOverWin.start();
        mMediaPlayerGameOverWin.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    /**
     * Prepare, start, release, nullify ,repeat
     */
    private void playSoundLoss() {
        MediaPlayer mMediaPlayerGameOverLoss = MediaPlayer.create(this.getActivity(), R.raw.loss);
        mMediaPlayerGameOverLoss.start();
        mMediaPlayerGameOverLoss.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    @Override
    public void backButtonPressed() {
        this.getActivity().getSupportFragmentManager().beginTransaction().remove(this);
        Intent i = new Intent(this.getContext(),MainActivity.class);
        startActivity(i);
    }

    private AlertDialog.Builder displayDialog(String title, String msg, final Player winner){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(),R.style.CustomDialog);
        builder.setCancelable(false).setTitle(title).setMessage(msg).setNeutralButton(android.R.string.ok,null);
        final AlertDialog alrt = builder.create();
        alrt.show();
        Button btn = alrt.getButton(DialogInterface.BUTTON_NEUTRAL);
        btn.setTextAppearance(this.getContext(),R.style.dialogBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameEngine.updateScore(winner);
                updateScore(mGameEngine);
                beginGame(mGameEngine.getmPlayerOne(),mGameEngine.getmPlayerTwo());
                alrt.cancel();
            }
        });
         return builder;
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
        resetBG(mPlayerOneTV);
        resetBG(mPlayerTwoTV);
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
                            view.getContext().getResources().getColor(R.color.transparent), // start color defined in resources as #ff333333
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
    @Override
    public void onDestroy(){
        super.onDestroy();

    }
    private void cleanUp(){
        mMediaPlayer.release();
        mMediaPlayer=null;
        
       /* mMediaPlayerGameOverLoss.release();
        mMediaPlayerGameOverWin.release();
        mMediaPlayerMove.release();
       
        mMediaPlayerGameOverWin=null;
        mMediaPlayerGameOverLoss=null;
        mMediaPlayerMove=null;*/
    }

}
