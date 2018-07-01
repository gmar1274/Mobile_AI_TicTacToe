package ai.portfolio.dev.project.app.com.tictactoe.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ai.portfolio.dev.project.app.com.tictactoe.Custom.CustomFlipAnimation;
import ai.portfolio.dev.project.app.com.tictactoe.Interfaces.OnFragmentInteractionListener;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.AIMove;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.TicTacToe;
import ai.portfolio.dev.project.app.com.tictactoe.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TicTacToe_Fragment extends Fragment implements OnFragmentInteractionListener {
    private static final long SPLASH_TIME_OUT = 4000;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String SPLASH = "SPLASH";
    public static final String FRAGMENT_TAG = "TicTacToe_Fragment";
    private TicTacToe mGameEngine;
    private final String HUMAN = "X", AI = "O";
    private Button[] mButtonGrid;
    private OnFragmentInteractionListener mListener;
    private TextView display_turn;

    public TicTacToe_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        } else {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.mGameEngine = new TicTacToe();
        this.mGameEngine.setPlayerTurn(this.mGameEngine.flipCoin());
        this.display_turn = (TextView) view.findViewById(R.id.player_turn_tv);
        this.mButtonGrid = init(view);
        displayBeginGame(view);
    }

    private void displayBeginGame(View view) {
        this.display_turn.setText(mGameEngine.getCurrentPlayerName());
        this.display_turn.setTextColor(this.getCurrentPlayerColor(mGameEngine.getCurrentPlayer()));
        this.display_turn.setShadowLayer(100, 1, 1, Color.BLACK);
        CustomFlipAnimation flip = new CustomFlipAnimation(this.display_turn, this.display_turn, 1000);
        if (this.display_turn.getVisibility() == View.GONE) {
            flip.reverse();
        } else {
            this.display_turn.startAnimation(flip);
        }
        // new Handler().postDelayed(new Runnable() {
        // @Override
        //public void run() {
        // Code to start new activity and finish this one
        //        getFragmentManager().beginTransaction().add(new GameSplashFragment(SPLASH_TIME_OUT),SPLASH).disallowAddToBackStack().commit();

        //    }
        //}, SPLASH_TIME_OUT);
        if (mGameEngine.isAIMove()) {
            doAiMove();
        }
    }
    /**
     * Calculate ai move from the TicTacToe object, then display it by using
     * makeMove()
     */
    private void doAiMove() {
        AIMove move = mGameEngine.makeAIMove();
        int row = move.getRow();
        int col = move.getCol();
        this.mGameEngine.undo(row,col);
        Log.e("AI MOVE: ","row: "+row+" col: "+col);
        this.makeMove(row, col);
    }
    private Button[] init(View view) {


        Button[] b = new Button[9];
        Button tv1 = (Button) view.findViewById(R.id.tv_00);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0, 0);
            }
        });
        Button tv2 = (Button) view.findViewById(R.id.tv_01);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0, 1);
            }
        });
        Button tv3 = (Button) view.findViewById(R.id.tv_02);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0, 2);
            }
        });
        Button tv4 = (Button) view.findViewById(R.id.tv_10);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1, 0);
            }
        });
        Button tv5 = (Button) view.findViewById(R.id.tv_11);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1, 1);
            }
        });
        Button tv6 = (Button) view.findViewById(R.id.tv_12);
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1, 2);
            }
        });
        Button tv7 = (Button) view.findViewById(R.id.tv_20);
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2, 0);
            }
        });
        Button tv8 = (Button) view.findViewById(R.id.tv_21);
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2, 1);
            }
        });
        Button tv9 = (Button) view.findViewById(R.id.tv_22);
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2, 2);
            }
        });
        b[0] = tv1;
        b[1] = tv2;
        b[2] = tv3;
        b[3] = tv4;
        b[4] = tv5;
        b[5] = tv6;
        b[6] = tv7;
        b[7] = tv8;
        b[8] = tv9;
        return b;
    }

    private void makeMove(int row, int col) {

        if (mGameEngine.isAvailable(row, col)) {
            Toast.makeText(getContext(), "Space taken silly!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int move = (row * 3) + col;
            Button cell = this.mButtonGrid[move];
            mGameEngine.makeMove(row, col, mGameEngine.getCurrentPlayer());
            cell.setText(mGameEngine.getCurrentPlayerSymbol().toUpperCase());
            cell.setTextColor(this.getCurrentPlayerColor(mGameEngine.getCurrentPlayer()));
            if (mGameEngine.isOver()) {
                int winner = mGameEngine.check4Winner();
                if (winner == mGameEngine.TIE) {
                    Toast.makeText(this.getActivity(), "It's a draw.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getActivity(), mGameEngine.getWinner(winner) + " wins!!!", Toast.LENGTH_LONG).show();
                }
            }else {
                nextTurn();
                if(mGameEngine.isAIMove())this.doAiMove();
            }
        } catch (Exception e) {
            Log.e("Error resources: ", e.getMessage().toString() + "\n" + e.getStackTrace().toString());
            e.printStackTrace();
        }
    }

    /**
     * Make next move and display whos turn it is
     */
    private void nextTurn() {
        mGameEngine.nextTurn();
        //Log.e("Next Turn: ",game.getCurrentPlayerName());
        //Log.e("Color resorice: ",this.getCurrentPlayerColor(game.getCurrentPlayer())+"");
        this.display_turn.setText(mGameEngine.getCurrentPlayerName());
        this.display_turn.setTextColor(this.getCurrentPlayerColor(mGameEngine.getCurrentPlayer()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tic_tac_toe_gameboard, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Do animation to flip coin
     * then set current player turn
     *
     * @return
     */
    private TicTacToe.PLAYER flipCoin(View view) {
        Random rand = new Random();
        if (rand.nextBoolean()) {
            return TicTacToe.PLAYER.HUMAN;
        } else {
            return TicTacToe.PLAYER.AI;
        }
    }

    private int getCurrentPlayerColor(TicTacToe.PLAYER player) {
        try {
            if (TicTacToe.PLAYER.HUMAN.equals(player))
                return this.getActivity().getResources().getColor(R.color.colorPlayer);
            else return this.getActivity().getResources().getColor(R.color.colorAI);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
