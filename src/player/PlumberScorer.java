package player;

import interfaces.IScorer;

public class PlumberScorer implements IScorer {
    private int score;

    public PlumberScorer() {
        this.score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void updateScore(long fillingDuration) {
        int scoreIncrement = (int) (fillingDuration / 1000); 
        score += scoreIncrement;
    }
}
