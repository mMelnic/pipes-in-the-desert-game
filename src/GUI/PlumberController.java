package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import javax.swing.*;
import java.awt.*;


import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import system.Map;

import components.Cistern;
import components.Component;
import components.Pipe;
import components.Pump;
import components.Spring;
import enumerations.Direction;
import player.Plumber;
import system.Cell;

/**
 * Controls the plumber player actions and interactions in the game.
 * Handles key and mouse events for player movement, pipe connections,
 * and other in-game actions.
 */
public class PlumberController extends KeyAdapter {
    private Plumber plumberPlayer1;
    private Plumber plumberPlayer2;
    private PlumberView plumberView1;
    private PlumberView plumberView2;
    private Plumber activePlumber;
    private JPanel mapPanel;

    private Pipe selectedPipe = null;
    private static final int CELL_SIZE = 80;
    private Set<Integer> pressedKeys = new HashSet<>();

    private enum State {
        MOVEMENT, ACTION
    }

    private State currentState = State.MOVEMENT;

    /**
     * Constructs a new PlumberController with the specified plumbers and map panel.
     *
     * @param plumberPlayer1 the first plumber player
     * @param plumberPlayer2 the second plumber player
     * @param mapPanel the map panel where the game is displayed
     */
    public PlumberController(Plumber plumberPlayer1, Plumber plumberPlayer2, JPanel mapPanel) {
        this.plumberPlayer1 = plumberPlayer1;
        this.plumberPlayer2 = plumberPlayer2;
        this.activePlumber = plumberPlayer2; // Start with plumberPlayer1
        this.plumberView1 = new PlumberView(plumberPlayer1, "green");
        this.plumberView2 = new PlumberView(plumberPlayer2, "red");

        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
        });

        // Schedule the task to switch players every 15 seconds
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::switchPlayers, 15, 15, TimeUnit.SECONDS);

    }

     /**
     * Switches the active player between plumberPlayer1 and plumberPlayer2.
     */
    private void switchPlayers() {
        if (activePlumber == plumberPlayer1) {
            activePlumber = plumberPlayer2;
        } else {
            activePlumber = plumberPlayer1;
        }
        showMessage("Switch!");

    }
    
    private void showMessage(String message) {
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 660, 755);
        frame.setLocationRelativeTo(null);
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent background
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
        frame.add(label);
        frame.setSize(300, 50);
        frame.setUndecorated(true); // Remove window decorations
    
        // Position the window at the bottom-left corner
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = 10;
        int y = screenSize.height - frame.getHeight() - 40;
        frame.setLocation(x, y);
    
        frame.setAlwaysOnTop(true);
        frame.setBackground(new Color(0, 0, 0, 0)); // Set the background to be transparent
        frame.setVisible(true);
    
        // Set a timer to close the window after 2 seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                frame.dispose();
            }
        }, 2000);
    }
    

    /**
     * Handles mouse press events for selecting and connecting pipes.
     *
     * @param e the MouseEvent triggered by a mouse press
     */
    private void handleMousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int row = mouseY / CELL_SIZE;
        int col = mouseX / CELL_SIZE;
        Map map = plumberPlayer1.getCurrentCell().getMap();
        Cell clickedCell = map.getCells(row, col);
        if (clickedCell != null) {
            Pipe clickedPipe = clickedCell.getComponent() instanceof Pipe ? (Pipe) clickedCell.getComponent() : null;
            Component clickedComponent = clickedCell.getComponent();
            if (clickedPipe != null || clickedComponent instanceof Spring || clickedComponent instanceof Cistern) {
                if (selectedPipe == null) {
                    // First click
                    if (clickedPipe != null) {
                        selectedPipe = clickedPipe;
                    }
                } else {
                    // Second click
                    if (clickedComponent instanceof Spring || clickedComponent instanceof Cistern) {
                        plumberPlayer1.connectPipeWithComponent(selectedPipe, clickedComponent);
                        selectedPipe = null;
                    } else {
                        selectedPipe = null;
                    }
                }
            } else {
                selectedPipe = null;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.add(keyCode);
        switch (currentState) {
            case MOVEMENT:
                handleMovementKeyPress(keyCode);
                break;
            case ACTION:
                handleActionKeyPress(keyCode);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    /**
     * Handles key press events for movement actions.
     *
     * @param keyCode the code of the key that was pressed
     */
    private void handleMovementKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                activePlumber.move(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                activePlumber.move(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                activePlumber.move(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                activePlumber.move(Direction.RIGHT);
                break;
            case KeyEvent.VK_ENTER:
                attemptRepairAction();
                break;
            case KeyEvent.VK_PERIOD:
                activePlumber.pickComponent();
                break;
            case KeyEvent.VK_SLASH:
                activePlumber.installComponent(activePlumber.getFacingDirection());
                break;
            case KeyEvent.VK_COMMA:
                activePlumber.dropComponent();
                break;
            case KeyEvent.VK_SHIFT:
                currentState = State.ACTION;
                break;
        }
    }
    

    /**
     * Handles key press events for action-related actions.
     *
     * @param keyCode the code of the key that was pressed
     */
    private void handleActionKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_SHIFT) {
            currentState = State.MOVEMENT;
            return; // Exit early if Shift key is pressed to switch to movement state
        }

        // Handle simultaneous key presses specific to the action state
        handleSimultaneousKeyPresses();
    }

    /**
     * Handles simultaneous key presses in action state for special actions like redirecting or detaching pipes.
     */
    private void handleSimultaneousKeyPresses() {
        // List of all pairs of keys
        int[] keys = { KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT };

        for (int key1 : keys) {
            for (int key2 : keys) {
                if (key1 != key2) {
                    if (pressedKeys.contains(key1) && pressedKeys.contains(key2)) {
                        // Check the order of key presses
                        if (pressedKeysOrder(key1) < pressedKeysOrder(key2)) {
                            Cell currentCell = activePlumber.getCurrentCell();
                            Component currentComponent = currentCell.getComponent();

                            if (currentComponent instanceof Pump) {
                                // Check and redirect for pump
                                checkAndRedirect(getDirection(key1), getDirection(key2));
                            } else if (currentComponent instanceof Pipe) {
                                // Check and detach for pipe
                                checkAndDetach(getDirection(key1), getDirection(key2));
                            }
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

    /**
     * Helper method to map key codes to directions.
     *
     * @param keyCode the key code
     * @return the direction corresponding to the key code
     */
    private Direction getDirection(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                return Direction.UP;
            case KeyEvent.VK_DOWN:
                return Direction.DOWN;
            case KeyEvent.VK_LEFT:
                return Direction.LEFT;
            case KeyEvent.VK_RIGHT:
                return Direction.RIGHT;
            default:
                return null; // Handle invalid key codes
        }
    }

    /**
     * Checks and redirects water flow between two pipes in specified directions.
     *
     * @param dir1 the first direction
     * @param dir2 the second direction
     */
    private void checkAndRedirect(Direction dir1, Direction dir2) {
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

    /**
     * Checks and detaches pipes in specified directions.
     *
     * @param dir1 the first direction
     * @param dir2 the second direction
     */
    private void checkAndDetach(Direction dir1, Direction dir2) {
        Cell cell1 = getCellInDirection(activePlumber.getCurrentCell(), dir1);
        Cell cell2 = getCellInDirection(activePlumber.getCurrentCell(), dir2);

        if (cell1 != null && cell2 != null) {
            Component component1 = cell1.getComponent();
            Component component2 = cell2.getComponent();
            activePlumber.detachPipe(component1, component2);
        }
    }

    /**
     * Gets the cell in a specified direction from the current cell.
     *
     * @param currentCell the current cell
     * @param direction the direction to get the cell
     * @return the cell in the specified direction
     */
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
     * Attempts to repair the component on the current cell if it is a pump or pipe.
     */
    private void attemptRepairAction() {
        // Get the current cell the player is standing on
        Cell currentCell = activePlumber.getCurrentCell();
        // Check if the cell contains a component and attempt repair
        if (currentCell.getComponent() != null) {
            if (currentCell.getComponent() instanceof Pump) {
                activePlumber.repairPump();
            } else if (currentCell.getComponent() instanceof Pipe) {
                activePlumber.repairPipe();
            }
        }
    }

    /**
     * Gets the view for plumberPlayer1.
     *zsq
     * @return the plumber view for plumberPlayer1
     */
    public PlumberView getPlumberView() {
        return plumberView1;
    }

    /**
     * Gets the view for plumberPlayer2.
     *
     * @return the plumber view for plumberPlayer2
     */
    public PlumberView getPlumberView2() {
        return plumberView2;
    }
}