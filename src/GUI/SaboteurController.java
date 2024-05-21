package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import components.Component;
import components.Pipe;
import components.Pump;
import enumerations.Direction;
import player.Saboteur;
import system.Cell;

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
    private Set<Integer> pressedKeys = new HashSet<>();

    private enum State {
        MOVEMENT, ACTION
    }

    private State currentState = State.MOVEMENT;

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
            case KeyEvent.VK_SHIFT-> currentState = State.ACTION;
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        Saboteur activePlayer = isPlayer1Active ? saboteurPlayer1 : saboteurPlayer2;
        int keyCode = e.getKeyCode();
        pressedKeys.add(keyCode);
        switch (currentState) {
            case MOVEMENT:
                handleKeyPress(keyCode, activePlayer);
                break;
            case ACTION:
                handleActionKeyPress(keyCode, activePlayer);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    /**
     * Handles key press events for action-related actions.
     *
     * @param keyCode the code of the key that was pressed
     */
    private void handleActionKeyPress(int keyCode, Saboteur player) {
        if (keyCode == KeyEvent.VK_SHIFT) {
            currentState = State.MOVEMENT;
            return; // Exit early if Shift key is pressed to switch to movement state
        }

        // Handle simultaneous key presses specific to the action state
        handleSimultaneousKeyPresses(player);
    }

    /**
     * Handles simultaneous key presses in action state for special actions like redirecting or detaching pipes.
     */
    private void handleSimultaneousKeyPresses(Saboteur player) {
        // List of all pairs of keys
        int[] keys = { KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D };

        for (int key1 : keys) {
            for (int key2 : keys) {
                if (key1 != key2) {
                    if (pressedKeysOrder(key1) < pressedKeysOrder(key2)) {
                        Cell currentCell = player.getCurrentCell();
                        Component currentComponent = currentCell.getComponent();

                        if (currentComponent instanceof Pump) {
                            // Check and redirect for pump
                            checkAndRedirect(getDirection(key1), getDirection(key2), player);
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper method to get the order of key presses.
     *
     * @param keyCode the key code
     * @return the order of the key press
     */
    private int pressedKeysOrder(int keyCode) {
        int order = 0;
        for (int key : pressedKeys) {
            if (key == keyCode) {
                return order;
            }
            order++;
        }
        return -1; // Key not found in pressedKeys set
    }

    private void checkAndRedirect(Direction dir1, Direction dir2, Saboteur activePlumber) {
        Cell cell1 = getCellInDirection(activePlumber.getCurrentCell(), dir1);
        Cell cell2 = getCellInDirection(activePlumber.getCurrentCell(), dir2);

        if (cell1 != null && cell2 != null) {
            Component component1 = cell1.getComponent();
            Component component2 = cell2.getComponent();
            if (component1 instanceof Pipe && component2 instanceof Pipe) {
                activePlumber.redirectWaterFlow((Pipe) component1, (Pipe) component2);
            }
        }
    }
    private Cell getCellInDirection(Cell currentCell, Direction direction) {
        switch (direction) {
            case UP:
                return currentCell.getMap().getUpwardCell(currentCell);
            case DOWN:
                return currentCell.getMap().getDownwardCell(currentCell);
            case LEFT:
                return currentCell.getMap().getLeftwardCell(currentCell);
            case RIGHT:
                return currentCell.getMap().getRightwardCell(currentCell);
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }
    /**
     * Helper method to map key codes to directions.
     *
     * @param keyCode the key code
     * @return the direction corresponding to the key code
     */
    private Direction getDirection(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                return Direction.UP;
            case KeyEvent.VK_S:
                return Direction.DOWN;
            case KeyEvent.VK_A:
                return Direction.LEFT;
            case KeyEvent.VK_D:
                return Direction.RIGHT;
            default:
                return null; // Handle invalid key codes
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
