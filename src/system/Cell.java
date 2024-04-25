package system;

import components.Component;
import components.Pipe;
import components.Pump;

public class Cell {
    /**
     * Stores the component occupying the cell, allowing for easy retrieval and manipulation.
     */
    private Component component;
    /**
     * Indicates whether the cell is currently unoccupied. [boolean, private, false]
     */
    private boolean isEmpty;
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

}
