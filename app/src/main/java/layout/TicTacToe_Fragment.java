package layout;

import android.content.Context;
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

import ai.portfolio.dev.project.app.com.tictactoe.Objects.TicTacToe;
import ai.portfolio.dev.project.app.com.tictactoe.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicTacToe_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicTacToe_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicTacToe_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String FRAGMENT_TAG ="TicTacToe_Fragment" ;
    private TicTacToe game;
    private final String HUMAN="X", AI="O";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView[] buttons;

    private OnFragmentInteractionListener mListener;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }else{


        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.game = new TicTacToe();
       this.game.setPlayerTurn(this.game.flipCoin());
        Toast.makeText(getContext(),this.game.getCurrentPlayerSymbol()+" goes first!",Toast.LENGTH_LONG).show();

        this.buttons = init(view);
    }
    private TextView[] init(View view) {
        TextView[] b = new TextView[9];
        TextView tv1 = (TextView) view.findViewById(R.id.tv_00);
        tv1.setText("G");
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0,0);
            }
        });
        TextView tv2 = (TextView) view.findViewById(R.id.tv_01);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0,1);
            }
        });
        TextView tv3 = (TextView) view.findViewById(R.id.tv_02);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(0,2);
            }
        });
        TextView tv4 = (TextView) view.findViewById(R.id.tv_10);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1,0);
            }
        });
        TextView tv5 = (TextView) view.findViewById(R.id.tv_11);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1,1);
            }
        });
        TextView tv6 = (TextView) view.findViewById(R.id.tv_12);
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(1,2);
            }
        });
        TextView tv7 = (TextView) view.findViewById(R.id.tv_20);
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2,0);
            }
        });
        TextView tv8 = (TextView) view.findViewById(R.id.tv_21);
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2,1);
            }
        });
        TextView tv9 = (TextView) view.findViewById(R.id.tv_22);
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMove(2,2);
            }
        });
        b[0]=tv1;
        b[1]=tv2;
        b[2]=tv3;
        b[3]=tv4;
        b[4]=tv5;
        b[5]=tv6;
        b[6]=tv7;
        b[7]=tv8;
        b[8]=tv9;
        return b;
    }

    private void makeMove(int row, int col) {
        int move = (row * 3 )+ col;
        TextView t =this.buttons[move] ;
       Log.e("MOVE: ",this.game.toString());
        if(game.isAvailable(row,col)){ Toast.makeText(getContext(),"Space taken silly!", Toast.LENGTH_SHORT).show(); return;}
        t.setText(game.getCurrentPlayerSymbol());
        game.makeMove(row,col, game.getCurrentPlayer());
        game.nextTurn();
        Log.e("MOVE:",this.game.toString());
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /**
     * Do animation to flip coin
     *  then set current player turn
     * @return
     */
    private TicTacToe.PLAYER flipCoin(View view) {
        Random rand = new Random();
        if(rand.nextBoolean()){
            return TicTacToe.PLAYER.HUMAN;
        }else{
            return TicTacToe.PLAYER.AI;
        }
    }

}
