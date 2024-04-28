package player;

import java.util.ArrayList;
import java.util.List;

import interfaces.IScorer;

public class Team {
    private List<MovablePlayer> players;
    private IScorer teamScore;

    public Team(IScorer teamScore) {
        this.players = new ArrayList<>();
        this.teamScore = teamScore;
    }

    public void assignPlayer(Player player) {
        if (player != null) {
            players.add(player);
            System.out.println(player.getName() + " has been assigned to the team.");
        } else {
            System.out.println("Invalid player. Cannot assign to the team.");
        }
    }

    // Getter for players
    public List<MovablePlayer> getPlayers() {
        return players;
    }

    // Setter for teamScore
    public void setTeamScore(IScorer teamScore) {
        this.teamScore = teamScore;
    }

    // Getter for teamScore
    public IScorer getTeamScore() {
        return teamScore;
    }
}
