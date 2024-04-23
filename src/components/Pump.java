package components;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import enumerations.Direction;
import interfaces.ILeakage;
import player.SaboteurScorer;

public class Pump extends Component implements ILeakage {
    private int connectablePipesNumber;
    private boolean isReservoirFull;
    private long leakStartTime;
    private boolean isLeaking;
    private Pipe incomingPipe;
    private Pipe outgoingPipe;
    private boolean isBroken;
    private SaboteurScorer saboteursScore;

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

    public void startLeaking() {
        if (isReservoirFull) {
            isLeaking = true;
            leakStartTime = System.currentTimeMillis();
            stopFlow();
        }
    }
    public void stopFlow() {
        if (outgoingPipe != null) {
            stopFlowRecursive(outgoingPipe);
        }
    }

    private void stopFlowRecursive(Component component) {
        if (component == null || component instanceof Cistern) {
            return;
        }
        if (component instanceof Pipe) {
            ((Pipe) component).isWaterFlowing(false); 
            for (Direction direction : Direction.values()) {
                stopFlowRecursive(component.connectedComponents.get(direction));
            }
        }
    }


    public void stopLeaking() {
        if (isLeaking) {
            isLeaking = false;
            long leakDuration = System.currentTimeMillis() - leakStartTime;
            leakStartTime = 0; 
            saboteursScore.updateScore(leakDuration); 
        }
    }
}
