package player;

import interfaces.IScorer;

/**
 * The {@code PlumberScorer} class implements the {@code IScorer} interface and
 * represents a scorer for plumbers in the game.
 * It keeps track of the score and provides methods to retrieve and update the
 * score.
 */
public class PlumberScorer implements IScorer {
    private int score;

    /**
     * Constructs a new {@code PlumberScorer} with an initial score of 0.
     */
    public PlumberScorer() {
        this.score = 0;
    }

    /**
     * Retrieves the current score of the plumber.
     * 
     * @return The current score of the plumber.
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * Updates the score based on the duration of filling.
     * The score is incremented by the number of seconds it took to fill a
     * component.
     * 
     * @param fillingDuration The duration of filling in milliseconds.
     */
    @Override
    public void updateScore(long fillingDuration) {
        int scoreIncrement = (int) (fillingDuration / 1000);
        score += scoreIncrement;
    }
}
