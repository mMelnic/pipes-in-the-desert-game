package system;

import components.Component;
import components.Pipe;
import components.Pump;
import enumerations.Direction;

public class Cell {
    Component component;
    boolean isEmpty;
    boolean isPlayerOn;
    int row;
    int column;
    Map map;

    public Component getComponent() {
        return component;
    }

    public void placeComponent(Component newElement){
        component = newElement;
    }

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
