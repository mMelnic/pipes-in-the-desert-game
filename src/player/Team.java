package player;

import java.util.ArrayList;
import java.util.List;

import interfaces.IScorer;

/**
 * The Team class represents a group of movable players within the game.
 * It manages the assignment of players to the team and provides access to the
 * team's score.
 */
public class Team {
    private List<MovablePlayer> players; // The list of players in the team
    private IScorer teamScore; // The scoring mechanism for the team

    /**
     * Constructs a new Team object with the specified scoring mechanism.
     *
     * @param teamScore The scoring mechanism for the team.
     */
    public Team(IScorer teamScore) {
        this.players = new ArrayList<>();
        this.teamScore = teamScore;
    }

    /**
     * Assigns a player to the team.
     * Adds the specified player to the team's roster.
     *
     * @param player The player to be assigned to the team.
     */
    public void assignPlayer(MovablePlayer player) {
        if (player != null) {
            players.add(player);
            System.out.println(player.getPlayerID() + " has been assigned to the team.");
        } else {
            System.out.println("Invalid player. Cannot assign to the team.");
        }
    }

    /**
     * Retrieves the list of players in the team.
     *
     * @return The list of players in the team.
     */
    public List<MovablePlayer> getPlayers() {
        return players;
    }

    /**
     * Sets the scoring mechanism for the team.
     *
     * @param teamScore The scoring mechanism to be set for the team.
     */
    public void setTeamScore(IScorer teamScore) {
        this.teamScore = teamScore;
    }

    /**
     * Retrieves the scoring mechanism for the team.
     *
     * @return The scoring mechanism for the team.
     */
    public IScorer getTeamScore() {
        return teamScore;
    }
}
