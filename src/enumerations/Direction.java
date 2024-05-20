package enumerations;

/**
 * The Direction enum represents different directions.
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    /**
     * Get the opposite direction of the current one.
     * @return The opposite direction.
     */
    public Direction getOppositeDirection() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new IllegalArgumentException("Unsupported direction: " + this);
        }
    }
}
