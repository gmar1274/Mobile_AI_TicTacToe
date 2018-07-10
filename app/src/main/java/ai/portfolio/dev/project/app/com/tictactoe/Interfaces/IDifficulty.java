package ai.portfolio.dev.project.app.com.tictactoe.Interfaces;

public interface IDifficulty {
    enum DIFFICULTY{EASY,INTERMEDIATE,HARD}
    void setDifficulty(DIFFICULTY diffLevel);
}
