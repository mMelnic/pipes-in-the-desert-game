package player;

import interfaces.IScorer;
import java.util.concurrent.atomic.AtomicInteger;

public class SaboteurScorer implements IScorer{
    private static AtomicInteger score = new AtomicInteger(0);

    public SaboteurScorer(){}

    /**
     * Retrieves the current score of the saboteurs team.
     *
     * @return The current score of the saboteurs team.
     */
    @Override
    public int getScore() {
        return score.get();
    }

     /**
     * Updates the score of the saboteurs team based on the duration of a leakage event.
     *
     * @param leakDuration The duration of the leakage event.
     */
    @Override
    public void updateScore(long leakDuration) {
        int scoreIncrement = (int) (leakDuration / 100); // Convert milliseconds to seconds
        score.addAndGet(scoreIncrement);
    }
}
