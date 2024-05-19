package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
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

public class PlumberController extends KeyAdapter {
    private Plumber plumberPlayer1;
    private Plumber plumberPlayer2;
    private PlumberView plumberView1;
    private PlumberView plumberView2;
    private Plumber activePlumber;

    private Pipe selectedPipe = null;
    private static final int CELL_SIZE = 80;
    private Set<Integer> pressedKeys = new HashSet<>();

    private enum State {
        MOVEMENT, ACTION
    }

    private State currentState = State.MOVEMENT;

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

        // // Schedule the task to switch players every 15 seconds
        // ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // scheduler.scheduleAtFixedRate(this::switchPlayers, 15, 15, TimeUnit.SECONDS);
    }

    private void switchPlayers() {
        if (activePlumber == plumberPlayer1) {
            activePlumber = plumberPlayer2;
        } else {
            activePlumber = plumberPlayer1;
        }
    }

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

    private void handleActionKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_SHIFT) {
            currentState = State.MOVEMENT;
            return; // Exit early if Shift key is pressed to switch to movement state
        }

        // Handle simultaneous key presses specific to the action state
        handleSimultaneousKeyPresses();
    }

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

    // Helper method to get the order of key presses
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

    // Helper method to map key codes to directions
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

    private void checkAndDetach(Direction dir1, Direction dir2) {
        Cell cell1 = getCellInDirection(activePlumber.getCurrentCell(), dir1);
        Cell cell2 = getCellInDirection(activePlumber.getCurrentCell(), dir2);

        if (cell1 != null && cell2 != null) {
            Component component1 = cell1.getComponent();
            Component component2 = cell2.getComponent();
            activePlumber.detachPipe(component1, component2);
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

    public PlumberView getPlumberView() {
        return plumberView1;
    }

    public PlumberView getPlumberView2() {
        return plumberView2;
    }
}