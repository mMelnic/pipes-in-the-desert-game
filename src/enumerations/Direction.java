package enumerations;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

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
