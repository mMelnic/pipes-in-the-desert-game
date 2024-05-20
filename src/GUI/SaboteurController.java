package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import enumerations.Direction;
import player.Saboteur;

/**
 * The SaboteurController class handles input and controls for two saboteur players.
 * It alternates control between the two players every 15 seconds.
 */
public class SaboteurController extends KeyAdapter {
    private Saboteur saboteurPlayer1;
    private Saboteur saboteurPlayer2;
    private SaboteurView saboteurView1;
    private SaboteurView saboteurView2;
    private boolean isPlayer1Active;

    /**
     * Constructs a new SaboteurController for the specified saboteur players.
     *
     * @param saboteurPlayer1 the first saboteur player
     * @param saboteurPlayer2 the second saboteur player
     */
    public SaboteurController(Saboteur saboteurPlayer1, Saboteur saboteurPlayer2) {
        this.saboteurPlayer1 = saboteurPlayer1;
        this.saboteurPlayer2 = saboteurPlayer2;
        this.saboteurView1 = new SaboteurView(saboteurPlayer1, "yellow");
        this.saboteurView2 = new SaboteurView(saboteurPlayer2, "blue");
        this.isPlayer1Active = true; // Start with player 1 active

        startSwitchTimer();
    }

    /**
     * Starts a timer to switch the active player every 15 seconds.
     */
    private void startSwitchTimer() {
        Timer switchTimer = new Timer(true); // Run timer as a daemon thread
        switchTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                switchActivePlayer();
            }
        }, 0, 15000); // Switch every 15 seconds
    }

    /**
     * Switches the active player between the two saboteurs.
     */
    private void switchActivePlayer() {
        isPlayer1Active = !isPlayer1Active;
        System.out.println("Switched active player: " + (isPlayer1Active ? "Player 1" : "Player 2"));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Saboteur activePlayer = isPlayer1Active ? saboteurPlayer1 : saboteurPlayer2;
        handleKeyPress(e.getKeyCode(), activePlayer);
    }

    /**
     * Handles key presses to control the active player.
     *
     * @param keyCode the code of the key that was pressed
     * @param player the active saboteur player
     */
    private void handleKeyPress(int keyCode, Saboteur player) {
        switch (keyCode) {
            case KeyEvent.VK_W -> player.move(Direction.UP);
            case KeyEvent.VK_S -> player.move(Direction.DOWN);
            case KeyEvent.VK_A -> player.move(Direction.LEFT);
            case KeyEvent.VK_D -> player.move(Direction.RIGHT);
            case KeyEvent.VK_Q -> player.puncturePipe();
        }
    }

    /**
     * Gets the view for the first saboteur player.
     *
     * @return the view for the first saboteur player
     */
    public SaboteurView getSaboteurView() {
        return saboteurView1;
    }
    
    /**
     * Gets the view for the second saboteur player.
     *
     * @return the view for the second saboteur player
     */
    public SaboteurView getSaboteurView2() {
        return saboteurView2;
    }
}
