package player;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import components.Pipe;
import components.Pump;
import enumerations.Direction;
import system.Cell;
import system.Map;

public abstract class MovablePlayer {
    protected UUID playerID;
    protected Team team;
    protected Cell currentCell;
    protected Direction facingDirection = Direction.DOWN;

    protected MovablePlayer(Team team) {
        this.playerID = UUID.randomUUID();
        this.team = team;
    }

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
                }
            } else if (targetCell.getComponent() instanceof Pump) {
                currentCell = targetCell;
            }
        }
    }

    public void redirectWaterFlow(Pipe newIncomingPipe, Pipe newOutgoingPipe) {
        if (currentCell.getComponent() instanceof Pump) {
            if (newIncomingPipe.equals(newOutgoingPipe)) {
                throw new IllegalArgumentException("Incoming and outgoing pipes should be different.");
            }

            Pump pump = (Pump) currentCell.getComponent();
            pump.setIncomingPipe(newIncomingPipe);
            pump.setOutgoingPipe(newOutgoingPipe);
            currentCell.getMap().updateWaterFlow();
        } else {
            throw new IllegalStateException("Current cell does not contain a pump"); // TODO return instead of exception?
        }
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    public UUID getPlayerID() {
        return playerID;
    }
}
