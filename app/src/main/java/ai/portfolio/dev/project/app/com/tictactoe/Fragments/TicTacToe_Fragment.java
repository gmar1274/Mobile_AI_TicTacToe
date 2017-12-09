package ai.portfolio.dev.project.app.com.tictactoe.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link TicTacToe_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicTacToe_Fragment extends Fragment implements OnFragmentInteractionListener {
    private static final long SPLASH_TIME_OUT = 4000;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String SPLASH = "SPLASH";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String FRAGMENT_TAG = "TicTacToe_Fragment";
    private TicTacToe game;
    private final String HUMAN = "X", AI = "O";
    private TextView[] cells;
    private OnFragmentInteractionListener mListener;
    private TextView display_turn;
    private Typeface TYPEFACE;

    public TicTacToe_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tic_tac_toe_gameboard.
     */
    // TODO: Rename and change types and number of parameters
    public static TicTacToe_Fragment newInstance(String param1, String param2) {
        TicTacToe_Fragment fragment = new TicTacToe_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        this.TYPEFACE = getGameTypeface();
        this.game = new TicTacToe();
        this.game.setPlayerTurn(this.game.flipCoin());
        this.display_turn = (TextView) view.findViewById(R.id.player_turn_tv);
        this.cells = init(view);
        displayBeginGame(view);
    }

    private void displayBeginGame(View view) {
        this.display_turn.setText(game.getCurrentPlayerName());
        this.display_turn.setTypeface(this.TYPEFACE);
        this.display_turn.setTextColor(this.getCurrentPlayerColor(game.getCurrentPlayer()));
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
        if (game.isAIMove()) {
            doAiMove();
        }
    }
    /**
     * Calculate ai move from the TicTacToe object, then display it by using
     * makeMove()
     */
    private void doAiMove() {
        AIMove move = game.makeAIMove();
        int row = move.getRow();
        int col = move.getCol();
        this.game.undo(row,col);
        Log.e("AI MOVE: ","row: "+row+" col: "+col);
        this.makeMove(row, col);
    }
    private TextView[] init(View view) {
        TextView[] b = new TextView[9];
        TextView tv1 = (TextView) view.findViewById(R.id.tv_00);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0, 0);
            }
        });
        TextView tv2 = (TextView) view.findViewById(R.id.tv_01);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0, 1);
            }
        });
        TextView tv3 = (TextView) view.findViewById(R.id.tv_02);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0, 2);
            }
        });
        TextView tv4 = (TextView) view.findViewById(R.id.tv_10);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1, 0);
            }
        });
        TextView tv5 = (TextView) view.findViewById(R.id.tv_11);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1, 1);
            }
        });
        TextView tv6 = (TextView) view.findViewById(R.id.tv_12);
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1, 2);
            }
        });
        TextView tv7 = (TextView) view.findViewById(R.id.tv_20);
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2, 0);
            }
        });
        TextView tv8 = (TextView) view.findViewById(R.id.tv_21);
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2, 1);
            }
        });
        TextView tv9 = (TextView) view.findViewById(R.id.tv_22);
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

        if (game.isAvailable(row, col)) {
            Toast.makeText(getContext(), "Space taken silly!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int move = (row * 3) + col;
            TextView cell = this.cells[move];
            game.makeMove(row, col, game.getCurrentPlayer());
            cell.setText(game.getCurrentPlayerSymbol().toUpperCase());
            cell.setTypeface(TYPEFACE);
            cell.setTextColor(this.getCurrentPlayerColor(game.getCurrentPlayer()));
            if (game.isOver()) {
                int winner = game.check4Winner();
                if (winner == game.TIE) {
                    Toast.makeText(this.getActivity(), "It's a draw.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getActivity(), game.getWinner(winner) + " wins!!!", Toast.LENGTH_LONG).show();
                }
            }else {
                nextTurn();
                if(game.isAIMove())this.doAiMove();
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
        game.nextTurn();
        //Log.e("Next Turn: ",game.getCurrentPlayerName());
        //Log.e("Color resorice: ",this.getCurrentPlayerColor(game.getCurrentPlayer())+"");
        this.display_turn.setText(game.getCurrentPlayerName());
        this.display_turn.setTextColor(this.getCurrentPlayerColor(game.getCurrentPlayer()));
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

    public Typeface getGameTypeface() {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(this.getActivity().getAssets(), ("fonts/gameFont.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tf;
    }
}
