package components;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import enumerations.Direction;
import interfaces.ILeakage;
import player.SaboteurScorer;
import system.Cell;

/**
 * Represents a pump component in the game.
 */
public class Pump extends Component implements ILeakage {
    private int connectablePipesNumber;
    private boolean isReservoirFull;
    private long leakStartTime;
    private boolean isLeaking;
    private Pipe incomingPipe;
    private Pipe outgoingPipe;
    private boolean isBroken;
    private SaboteurScorer saboteursScore;

    /**
     * Constructs a new Pump object with a specified number of connectable pipes.
     * 
     * @param connectablePipesNumber the number of pipes that can be connected to
     *                               the pump.
     */
    public Pump(int connectablePipesNumber, Cell cell) {
        super(cell);
        this.connectablePipesNumber = connectablePipesNumber;
        this.isReservoirFull = false;
        this.leakStartTime = 0;
        this.isLeaking = false;
    }

    /**
     * Constructs a new Pump object with a default number of connectable pipes.
     */
    public Pump(Cell cell) {
        super(cell);
        this.isReservoirFull = false;
        this.leakStartTime = 0;
        this.isLeaking = false;
    }

    /**
     * Connects a pipe to the pump.
     * 
     * @param newPipe the pipe to be connected.
     */
    public void connectPipe(Pipe newPipe) {
        if (incomingPipe == null) {
            incomingPipe = newPipe;
        } else if (outgoingPipe == null) {
            outgoingPipe = newPipe;
        }
    }

    /**
     * Fills the reservoir of the pump.
     */
    public void fillReservoir() {
        if (!isReservoirFull) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (outgoingPipe.isFull() == true) {
                        startLeaking();
                        isReservoirFull = true;

                    } else {
                        isReservoirFull = false;
                    }
                }
            };
            long delay = 8000;
            timer.schedule(task, delay);
        }
    }

    /**
     * Starts the leaking of the pump if the reservoir is full.
     */
    public void startLeaking() {
        if (isReservoirFull) {
            isLeaking = true;
            leakStartTime = System.currentTimeMillis();
            stopFlow();
        }
    }

    /**
     * Stops the water flow from the pump.
     */
    public void stopFlow() {
        if (outgoingPipe != null) {
            stopOrStartFlowRecursive(outgoingPipe, false);
        }
    }

    /**
     * Recursively stops or starts the water flow in connected components.
     * 
     * @param component   the component to stop or start the flow.
     * @param stopOrStart true to stop the flow, false to start it.
     */
    private void stopOrStartFlowRecursive(Component component, boolean stopOrStart) {
        if (component == null || component instanceof Cistern) {
            return;
        }
        if (component instanceof Pipe) {
            ((Pipe) component).isWaterFlowing(stopOrStart);
            for (Direction direction : Direction.values()) {
                stopOrStartFlowRecursive(component.connectedComponents.get(direction), stopOrStart);
            }
        }
    }

    /**
     * Stops the leaking of the pump.
     */
    public void stopLeaking() {
        if (isLeaking) {
            isLeaking = false;
            long leakDuration = System.currentTimeMillis() - leakStartTime;
            leakStartTime = 0;
            saboteursScore.updateScore(leakDuration);
            stopOrStartFlowRecursive(outgoingPipe, true);
        }
    }

    /**
     * Retrieves the number of connectable pipes for the pump.
     * 
     * @return the number of connectable pipes.
     */
    public int getConnectablePipesNumber() {
        return connectablePipesNumber;
    }

    /**
     * Sets the incoming pipe for the pump.
     * 
     * @param newIncomingPipe the new incoming pipe.
     */
    public void setIncomingPipe(Pipe newIncomingPipe) {
        this.incomingPipe = newIncomingPipe;
    }

    /**
     * Sets the outgoing pipe for the pump.
     * 
     * @param newOutgoingPipe the new outgoing pipe.
     */
    public void setOutgoingPipe(Pipe newOutgoingPipe) {
        this.outgoingPipe = newOutgoingPipe;
    }

    /**
     * Checks if the pump is broken.
     * 
     * @return true if the pump is broken, false otherwise.
     */
    public boolean isBroken() {
        return isBroken;
    }

    /**
     * Sets whether the pump is broken.
     * 
     * @param set true to set the pump as broken, false otherwise.
     */
    public void setBroken(boolean set) {
        isBroken = set;
    }

    /**
     * Checks if the pump is leaking.
     * 
     * @return true if the pump is leaking, false otherwise.
     */
    public boolean isLeaking() {
        return isLeaking;
    }

    /**
     * Sets whether the pump is leaking.
     * 
     * @param set true to set the pump as leaking, false otherwise.
     */
    public void setLeaking(boolean set) {
        isLeaking = set;
    }

    /**
     * Checks if the reservoir of the pump is full.
     * 
     * @return true if the reservoir is full, false otherwise.
     */
    public boolean isReservoirFull() {
        return isReservoirFull;
    }

    /**
     * Sets whether the reservoir of the pump is full.
     * 
     * @param set true to set the reservoir as full, false otherwise.
     */
    public void setReservoirFull(boolean set) {
        isReservoirFull = set;
    }

    /**
     * Retrieves the outgoing pipe of the pump.
     * 
     * @return the outgoing pipe.
     */
    public Pipe getOutgoingPipe() {
        return outgoingPipe;
    }

    /**
     * Retrieves the incoming pipe of the pump.
     * 
     * @return the incoming pipe.
     */
    public Pipe getIncomingPipe() {
        return incomingPipe;
    }

}
