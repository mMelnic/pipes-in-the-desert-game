package system;

import components.Component;
import components.Pipe;
import components.Pump;
import enumerations.Direction;

public class Cell {
    /**
     * Stores the component occupying the cell, allowing for easy retrieval and manipulation.
     */
    private Component component;
    /**
     * Indicates whether the cell is currently unoccupied. [boolean, private, false]
     */
    public boolean isEmpty = true;
    /**
     * racks whether a player is currently occupying the cell. [boolean, private, false]
     */
    private boolean isPlayerOn;
    /**
     * Represents the row coordinate of the cell within the map grid. [int, private]
     */
    public int row;
    /**
     * Represents the column coordinate of the cell within the map grid. [int, private]
     */
    public int column;
    Map map;

    /**
     * Retrieves the component currently occupying the cell for further interaction. [Component]
     * @return Component
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Places a new component onto the cell, updating its status accordingly.
     * @param newElement
     */
    public void placeComponent(Component newElement){
        component = newElement;
        isEmpty = false;
    }

    /**
     * Checks if the cell contains a pump or a pipe, returning a boolean array to indicate their presence.
     * @boolean
     */
    public boolean[] checkPumpOrPipe(){
        if (component instanceof Pipe) {
            return new boolean[]{true, false};
        } else if (component instanceof Pump) {
            return new boolean[]{false, true};
        } else {
            return new boolean[]{false, false};
        }
    }

    public Direction getRelativeDirection(Cell otherCell) {
        if (otherCell.equals(this)) {
            return null; // Cells are the same, so no direction
        }

        if (map == null || otherCell.getMap() == null || !map.equals(otherCell.getMap())) {
            return null; // Cells belong to different maps, so no direction
        }

        if (map.getDownwardCell(this) == otherCell) {
            return Direction.DOWN;
        } else if (map.getLeftwardCell(this) == otherCell) {
            return Direction.LEFT;
        } else if (map.getUpwardCell(this) == otherCell) {
            return Direction.UP;
        } else if (map.getRightwardCell(this) == otherCell) {
            return Direction.RIGHT;
        }

        return null; // No relative direction found
    }

    public Map getMap() {
        return map;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isPlayerOn() {
        return isPlayerOn;
    }

    public void setPlayerOn(boolean isPlayerOn) {
        this.isPlayerOn = isPlayerOn;
    }
}
