package player;

import interfaces.IScorer;

public class SaboteurScorer implements IScorer{
    private int score;


    /**
     * Retrieves the current score of the saboteurs team.
     *
     * @return The current score of the saboteurs team.
     */
    @Override
    public int getScore() {
        return score;
    }

     /**
     * Updates the score of the saboteurs team based on the duration of a leakage event.
     *
     * @param leakDuration The duration of the leakage event.
     */
    @Override
    public void updateScore(long leakDuration) {
        int scoreIncrease = (int) (leakDuration / 1000); // Convert milliseconds to seconds
         score += scoreIncrease;
    }
}
