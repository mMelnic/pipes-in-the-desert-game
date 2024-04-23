package components;

import java.util.UUID;

import interfaces.ILeakage;

public class Pump extends Component implements ILeakage{
    private int connectablePipesNumber;
    private boolean isReservoirFull;
    private long leakStartTime;
    private boolean isLeaking;
    private Pipe incomingPipe;
    private Pipe outgoingPipe;
    private boolean isBroken;

    public Pump(UUID componentID, int connectablePipesNumber) {
        super(componentID);
        this.connectablePipesNumber = connectablePipesNumber;
    }
    public void connectPipe(Pipe newPipe) {
        if (incomingPipe == null) {
            incomingPipe = newPipe;
        } else if (outgoingPipe == null) {
            outgoingPipe = newPipe;
        }
    }
    public void fillReservoir() {
        if (!isReservoirFull) {
            isReservoirFull = true;
        }
    }
    public void startLeaking() {
        if (isReservoirFull) {
            isLeaking = true;
            leakStartTime = System.currentTimeMillis();
        }
    }
    public long stopLeaking() {
        if (isLeaking) {
            isLeaking = false;
            long leakDuration = System.currentTimeMillis() - leakStartTime;
            leakStartTime = 0; // Reset leak start time
            return leakDuration;
        }
        return 0; 
    }
}
