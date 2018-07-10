package ai.portfolio.dev.project.app.com.tictactoe.Interfaces;

import android.content.Context;
import android.widget.Button;

import ai.portfolio.dev.project.app.com.tictactoe.Objects.GameEngine;
import ai.portfolio.dev.project.app.com.tictactoe.Objects.Player;

public interface ITicTacToeFragment {
GameEngine newGame(Context context, Player pOne, Player pTwo);
Button[][] attachButtonListeners();
}
