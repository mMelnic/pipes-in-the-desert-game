package components;

import enumerations.Direction;
import enumerations.Shapes;
import interfaces.ILeakage;
import player.PlumberScorer;
import system.Cell;

/**
 * Represents a pipe component in the game.
 */

public class Pipe extends Component implements ILeakage {
    private Shapes shape;
    private boolean isWaterFlowing;
    private long leakStartTime;
    private boolean isLeaking;
    private boolean isFull;
    private boolean isBroken;
    private boolean isPlayerOn;
    private PlumberScorer plumbersScore;


    // public Pipe(Cell location) {
    //     super(location);
    //     this.shape = Shapes.HORIZONTAL;
    //     this.isWaterFlowing = false;
    //     this.leakStartTime = 0;
    //     this.isLeaking = false;
    //     this.isBroken = false;
    //     this.isPlayerOn = false;
    // }

     /**
     * Constructs a new Pipe object with default values.
     */
    public Pipe(Cell cell) {
        super(cell);
        this.shape = Shapes.HORIZONTAL;
        this.isWaterFlowing = false;
        this.leakStartTime = 0;
        this.isLeaking = false;
    }

    /**
     * Starts the leaking of the pipe.
     */
    @Override
    public void startLeaking() {
        if (!isLeaking) {
            isLeaking = true;
            leakStartTime = System.currentTimeMillis();
            //stopFlow();
        }
    }


    /**
     * Stops the leaking of the pipe.
     */
    @Override
    public long stopLeaking() {
        if (isLeaking) {
            isLeaking = false;
            long duration = System.currentTimeMillis() - leakStartTime;
            undoStopFlow();
            // plumbersScore.updateScore(duration);
            return duration;
        }
        return 0;
        
    }

    /**
     * Checks if the pipe is full.
     * @return true if the pipe is full, false otherwise.
     */
    public boolean isFull() {

        return isFull;
    }
     /**
     * Sets whether water is flowing through the pipe.
     * @param set true to set water flowing, false otherwise.
     */
    public void isWaterFlowing(boolean set){
        isWaterFlowing = set;
    }

    /**
     * Undoes the stoppage of water flow.
     */
    private void undoStopFlow() {
        Component currentPipe = this;
        for (Direction direction : Direction.values()) {
            Component connectedComponent = currentPipe.connectedComponents.get(direction);
            while (connectedComponent != null && !(connectedComponent instanceof Cistern)) {
                if (connectedComponent instanceof Pipe) {
                    ((Pipe) connectedComponent).isWaterFlowing = true;
                }
                currentPipe = connectedComponent;
                connectedComponent = currentPipe.connectedComponents.get(direction);
            }
        }
    }

    /**
     * Stops the water flow in the pipe.
     */
    public void stopFlow() {
        isWaterFlowing = false;
        for (Direction direction : Direction.values()) {
            stopFlowRecursive(connectedComponents.get(direction));
        }
    }

    /**
     * Recursively stops the water flow in connected components.
     * @param component the component to stop the flow.
     */
    private void stopFlowRecursive(Component component) {
        if (component == null || component instanceof Cistern) {
            return;
        }
        if (component instanceof Pipe) {
            ((Pipe) component).isWaterFlowing = false;
            for (Direction direction : Direction.values()) {
                stopFlowRecursive(component.connectedComponents.get(direction));
            }
        }
    }

  
    /**
     * Changes the shape of the pipe based on connected components.
     */
    public void changeShape() {
        // Check if there is only one connected component
        int connectedCount = 0;
        Direction singleDirection = null;
        for (Direction direction : Direction.values()) {
            if (connectedComponents.get(direction) != null) {
                connectedCount++;
                singleDirection = direction;
            }
        }
        if (connectedCount == 1) {
            if (singleDirection == Direction.UP || singleDirection == Direction.DOWN ) {
                shape = Shapes.VERTICAL;
            } else if (singleDirection == Direction.LEFT || singleDirection == Direction.RIGHT ){
                shape = Shapes.HORIZONTAL;
            }
        } else {
            boolean upConnected = connectedComponents.get(Direction.UP) != null;
            boolean downConnected = connectedComponents.get(Direction.DOWN) != null;
            boolean leftConnected = connectedComponents.get(Direction.LEFT) != null;
            boolean rightConnected = connectedComponents.get(Direction.RIGHT) != null;
    
            if (upConnected && downConnected) {
                shape = Shapes.VERTICAL;
            } else if (upConnected && rightConnected) {
                shape = Shapes.CORNER_LEFT_DOWN;
            } else if (rightConnected && downConnected) {
                shape = Shapes.CORNER_LEFT_UP;
            } else if (downConnected && leftConnected) {
                shape = Shapes.CORNER_RIGHT_UP;
            } else if (leftConnected && upConnected) {
                shape = Shapes.CORNER_RIGHT_DOWN;
            } else {
                shape = Shapes.HORIZONTAL;
            }
        }
    }
    /**
     * Checks if the pipe is broken.
     * @return true if the pipe is broken, false otherwise.
     */
    public boolean isBroken(){
        return isBroken;
    }
     /**
     * Sets whether the pipe is broken.
     * @param set true to set the pipe as broken, false otherwise.
     */
    public void setBroken(boolean set){
        isBroken = set;
    }
      /**
     * Checks if the pipe is leaking.
     * @return true if the pipe is leaking, false otherwise.
     */
    public boolean isLeaking(){
        return isLeaking;
    }
     /**
     * Sets whether the pipe is leaking.
     * @param set true to set the pipe as leaking, false otherwise.
     */
    public void setLeaking(boolean set){
        isLeaking = set;
    }
     /**
     * Checks if water is flowing through the pipe.
     * @return true if water is flowing, false otherwise.
     */
    public boolean isWaterFlowing(){
        return isWaterFlowing;
    }
    /**
     * Sets whether water is flowing through the pipe.
     * @param set true to set water flowing, false otherwise.
     */
    public void setWaterFlowing(boolean set){
        isWaterFlowing = set;
    }

    
}
