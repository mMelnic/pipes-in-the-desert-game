package components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import enumerations.Direction;
import interfaces.ILeakage;
import interfaces.IScorer;
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
    private long fillStartTime;
    private long fillElapsedTime = 0;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fillingTask;
    private List<IScorer> scorers = new ArrayList<>();

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
        isBroken = false;
    }

    /**
     * Constructs a new Pump object with a default number of connectable pipes.
     */
    public Pump(Cell cell) {
        super(cell);
        connectablePipesNumber = 4;
        this.isReservoirFull = false;
        this.leakStartTime = 0;
        this.isLeaking = false;
        isBroken = false;
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

    public void removePipe(Pipe pipeToRemove) {
        if (incomingPipe == pipeToRemove) {
            incomingPipe = null;
        }
        if (outgoingPipe == pipeToRemove) {
            outgoingPipe = null;
        }
    }

    /**
     * Fills the reservoir of the pump.
     */
    public void fillReservoir() {
        if (!isReservoirFull) {
            fillStartTime = System.currentTimeMillis();
            fillingTask = scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (incomingPipe.isWaterFlowing() || outgoingPipe.isWaterFlowing()) {
                        long currentTime = System.currentTimeMillis();
                        fillElapsedTime += currentTime - fillStartTime;
                        fillStartTime = currentTime;

                        if (fillElapsedTime >= 30000) {
                            isReservoirFull = true;
                            startLeaking();
                            stopFillingTask();
                        }
                    } else {
                        fillStartTime = System.currentTimeMillis(); // Reset the start time when the water flow stops
                    }
                }
            }, 0, 100, TimeUnit.MILLISECONDS); // Check every 100 milliseconds
        }
    }

    public void stopFillingTask() {
        if (fillingTask != null && !fillingTask.isCancelled()) {
            fillingTask.cancel(true);
            fillElapsedTime = 0;
            //location.getMap().updateWaterFlow();
        }
    }

    /**
     * Starts the leaking of the pump if the reservoir is full.
     */
    public void startLeaking() {
        if (isReservoirFull && !isLeaking) {
            isBroken = true;
            isLeaking = true;
            leakStartTime = System.currentTimeMillis();
            location.getMap().updateWaterFlow();
        }
    }

    /**
     * Stops the leaking of the pump.
     */
    public long stopLeaking() {
        if (isLeaking) {
            isLeaking = false;
            long leakDuration = System.currentTimeMillis() - leakStartTime;
            leakStartTime = 0;
            // saboteursScore.updateScore(leakDuration);
            location.getMap().updateWaterFlow();
            System.out.println("Leaking duration: " + leakDuration + " milliseconds");
            notifyScorers(leakDuration);
            return leakDuration;
        }
        return 0;
    }

    public void undoFullPipes() {
        Pipe fullPipe = findFullPipe();
        if (fullPipe != null) {
            // Start the recursive traversal from the full pipe
            undoFullPipesRecursive(fullPipe, new HashSet<>());
        }
    }

    private void undoFullPipesRecursive(Component currentComponent, Set<Component> visited) {
        // Base case: If the currentComponent is a Pipe
        if (currentComponent instanceof Pipe) {
            Pipe pipe = (Pipe) currentComponent;
            // Reset attributes
            pipe.setFull(false);
            pipe.setWaterFlowing(false);
        }

        // Mark the current component as visited
        visited.add(currentComponent);

        // Traverse the connected components recursively
        for (Component connectedComponent : currentComponent.getConnectedComponents().values()) {
            // Check if the connected component has not been visited before
            if (!visited.contains(connectedComponent)) {
                // Recursive call to traverse the connected components of the connected component
                undoFullPipesRecursive(connectedComponent, visited);
            }
        }
    }

    private Pipe findFullPipe() {
        if (incomingPipe != null && incomingPipe.isFull()) {
            return incomingPipe;
        }
        if (outgoingPipe != null && outgoingPipe.isFull()) {
            return outgoingPipe;
        }
        return null; // Return null if no full pipe is found
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

    public boolean isFilling() {
        return fillingTask != null && !fillingTask.isDone();
    }

}
