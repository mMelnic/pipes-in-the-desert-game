package components;

import enumerations.Direction;
import enumerations.Shapes;
import interfaces.ILeakage;
import interfaces.IScorer;
import interfaces.IWaterFlowListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import system.Cell;

/**
 * Represents a pipe component in the game.
 */

public class Pipe extends Component implements ILeakage {
    private Shapes shape;
    private boolean isWaterFlowing;
    private long leakStartTime;
    private long freeEndLeakingStartTime;
    private boolean isLeaking;
    private boolean isFull;
    private boolean isBroken;
    private boolean freeEndLeaking;
    private Set<IWaterFlowListener> listeners = new HashSet<>();
    private List<IScorer> scorers = new ArrayList<>();

     /**
     * Constructs a new Pipe object with default values.
     */
    public Pipe(Cell cell) {
        super(cell);
        shape = Shapes.HORIZONTAL;
        isWaterFlowing = false;
        leakStartTime = 0;
        isLeaking = false;
        isBroken = false;
        isFull = false;
        freeEndLeaking = false;
        freeEndLeakingStartTime = 0;
    }

    public void addWaterFlowListener(IWaterFlowListener listener) {
        listeners.add(listener);
    }

    public void removeWaterFlowListener(IWaterFlowListener listener) {
        listeners.remove(listener);
    }

    public void notifyWaterFlowListeners() {
        for (IWaterFlowListener listener : listeners) {
            listener.onWaterFlowChanged(this);
        }
    }

    public void addScorer(IScorer scorer) {
        scorers.add(scorer);
    }

    public void removeScorer(IScorer scorer) {
        scorers.remove(scorer);
    }

    private void notifyScorers(long leakTime) {
        for (IScorer scorer : scorers) {
            scorer.updateScore(leakTime);
        }
        System.out.println("Leaking duration: " + leakTime + " milliseconds");

    }

    /**
     * Starts the leaking of the pipe.
     */
    @Override
    public void startLeaking() {
        if (!isLeaking) {
            isLeaking = true;
            leakStartTime = System.currentTimeMillis();
            location.getMap().updateWaterFlow();
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
            System.out.println("Leaking duration: " + duration + " milliseconds");
            notifyScorers(duration);
            location.getMap().updateWaterFlow();
            return duration;
        }
        return 0;
        
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
            } else if (downConnected && leftConnected) {
                shape = Shapes.CORNER_LEFT_DOWN;
            } else if (leftConnected && upConnected) {
                shape = Shapes.CORNER_LEFT_UP;
            } else if (upConnected && rightConnected) {
                shape = Shapes.CORNER_RIGHT_UP;
            } else if (rightConnected && downConnected) {
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
     * Checks if the pipe is full.
     * 
     * @return true if the pipe is full, false otherwise.
     */
    public boolean isFull() {
        return isFull;
    }

    /**
     * Sets whether water is flowing through the pipe.
     * 
     * @param set true to set water flowing, false otherwise.
     */
    public void isWaterFlowing(boolean set) {
        isWaterFlowing = set;
    }
    public Shapes getShape() {
        return shape;
    }

    /**
     * Sets whether water is flowing through the pipe.
     * @param set true to set water flowing, false otherwise.
     */
    public void setWaterFlowing(boolean isWaterFlowing) {
        if (this.isWaterFlowing != isWaterFlowing) {
            this.isWaterFlowing = isWaterFlowing;
            notifyWaterFlowListeners();
        }
    }

    public void setFull(boolean isFull) {
        this.isFull = isFull;
    }

    public boolean isFreeEndLeaking() {
        return freeEndLeaking;
    }
    
    public void setFreeEndLeaking(boolean value) {
        // If the value is changing to true
        if (value && !freeEndLeaking) {
            freeEndLeaking = true;
            freeEndLeakingStartTime = System.currentTimeMillis(); // Record the start time
        } else if (!value && freeEndLeaking) { // If the value is changing to false
            freeEndLeaking = false;
            long duration = System.currentTimeMillis() - freeEndLeakingStartTime; // Calculate the duration
            notifyScorers(duration);
        }
    }
}
