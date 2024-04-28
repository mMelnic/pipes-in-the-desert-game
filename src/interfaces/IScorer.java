package interfaces;

public interface IScorer {
     /**
     * Retrieves the current score maintained by the implementing class.
     *
     * @return The current score of the team.
     */
    int getScore();

    /**
     * Updates the score maintained by the implementing class based on the specified duration.
     *
     * @param duration The duration associated with the game event.
     */
    void updateScore(long duration);

}
