package player;

import components.Pipe;
import components.Pump;
import enumerations.Direction;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import system.Cell;
import system.Map;

/**
 * Abstract class representing a movable player in a game.
 */
public abstract class MovablePlayer {
    protected UUID playerID;
    protected Team team;
    protected Cell currentCell;
    protected Direction facingDirection = Direction.DOWN;

    /**
     * Constructs a new MovablePlayer with a unique player ID and assigned to a
     * team.
     * 
     * @param team The team to which the player belongs.
     */
    protected MovablePlayer(Team team) {
        this.playerID = UUID.randomUUID();
        this.team = team;
    }

    /**
     * Allows the player to choose a map from a list of available maps based on
     * size.
     * 
     * @param maps A list of available maps to choose from.
     * @return The chosen map.
     * @throws IllegalArgumentException If no maps are available or if an invalid
     *                                  map size choice is made.
     */
    public Map chooseMap(List<Map> maps) {
        if (maps == null || maps.isEmpty()) {
            throw new IllegalArgumentException("No maps available");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a map size: small, medium, or large");
        String sizeChoice = scanner.nextLine().toLowerCase();

        for (Map map : maps) {
            if (map.getSize().equalsIgnoreCase(sizeChoice)) {
                return map;
            }
        }

        throw new IllegalArgumentException("Invalid map size choice");
    }

    /**
     * Moves the player in the specified direction.
     * This method calculates the target cell based on the current cell and the
     * specified direction.
     * It checks if the target cell contains a Pipe or a Pump. If it's a Pipe and
     * not occupied by another player,
     * the player moves to that cell and updates their current cell and facing
     * direction.
     * If the target cell contains a Pump, the player moves to that cell without any
     * checks.
     *
     * @param direction The direction in which to move the player.
     * @throws IllegalArgumentException If an invalid direction is specified.
     */

    public void move(Direction direction) {
        Cell targetCell;

        switch (direction) {
            case UP:
                targetCell = currentCell.getMap().getUpwardCell(currentCell);
                facingDirection = Direction.UP;
                break;
            case DOWN:
                targetCell = currentCell.getMap().getDownwardCell(currentCell) ;
                facingDirection = Direction.DOWN;
                break;
            case LEFT:
                targetCell = currentCell.getMap().getLeftwardCell(currentCell);
                facingDirection = Direction.LEFT;
                break;
            case RIGHT:
                targetCell = currentCell.getMap().getRightwardCell(currentCell);
                facingDirection = Direction.RIGHT;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }

        if (targetCell != null) {
            if (targetCell.getComponent() instanceof Pipe) {
                if (!targetCell.isPlayerOn()) {
                    targetCell.setPlayerOn(true);
                    if (currentCell.getComponent() instanceof Pipe) {
                        currentCell.setPlayerOn(false);
                    }
                    currentCell = targetCell;
                    String message = "Moved " + direction + " to row " + targetCell.getRow() + " and column " + targetCell.getColumn();
                    handleOutput(message);
                } else {
                    handleOutput("You cannot move there.");
                }
            } else if (targetCell.getComponent() instanceof Pump) {
                if (currentCell.getComponent() instanceof Pipe) {
                    currentCell.setPlayerOn(false);
                }
                currentCell = targetCell;
                String message = "Moved " + direction + " to row " + targetCell.getRow() + " and column " + targetCell.getColumn();
                handleOutput(message);
            } else {
                handleOutput("You cannot move there.");
            }
        }
    }

    /**
     * Redirects the water flow of a pump to different incoming and outgoing pipes.
     * This method updates the water flow configuration of a pump by assigning new
     * incoming and outgoing pipes.
     * If the pump is connected to the same pipe for both incoming and outgoing
     * flow, an IllegalArgumentException is thrown.
     * If the current cell does not contain a pump, an IllegalStateException is
     * thrown.
     *
     * @param newIncomingPipe The new incoming pipe.
     * @param newOutgoingPipe The new outgoing pipe.
     * @throws IllegalArgumentException If the incoming and outgoing pipes are the
     *                                  same.
     * @throws IllegalStateException    If the current cell does not contain a pump.
     */

    public void redirectWaterFlow(Pipe newIncomingPipe, Pipe newOutgoingPipe) {
        if (newIncomingPipe == null || newOutgoingPipe == null) {
            return;
        }
        
        if (currentCell.getComponent() instanceof Pump) {
            if (newIncomingPipe.equals(newOutgoingPipe)) {
                handleOutput("Incoming and outgoing pipes should be different.");
                return;
            }

            Pump pump = (Pump) currentCell.getComponent();
            pump.setIncomingPipe(newIncomingPipe);
            pump.setOutgoingPipe(newOutgoingPipe);
            currentCell.getMap().updateWaterFlow();
            handleOutput("The direction changed.");
        } else {
            handleOutput("You are not standing on a pump.");
        }
    }

    /**
     * Prints the message to the system output and appends it to a file.
     *
     * @param message  The message to be printed and appended.
     * @param filePath The path to the file where the message will be appended.
     */
    protected void handleOutput(String message) {
        System.out.println(message);
        try {
            FileWriter writer = new FileWriter("output.txt", true);
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
        }
    }

    /**
     * Gets the current cell of the player.
     * 
     * @return The current cell.
     */
    public Cell getCurrentCell() {
        return currentCell;
    }

    /**
     * Sets the current cell of the player.
     * 
     * @param currentCell The new current cell.
     */
    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    /**
     * Gets the facing direction of the player.
     * 
     * @return The facing direction.
     */
    public Direction getFacingDirection() {
        return facingDirection;
    }

    /**
     * Sets the facing direction of the player.
     * 
     * @param facingDirection The new facing direction.
     */
    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    /**
     * Gets the ID of the player.
     * 
     * @return The player ID.
     */
    public UUID getPlayerID() {
        return playerID;
    }
    public abstract boolean isSaboteur();

    public abstract boolean isPlumber();
}
