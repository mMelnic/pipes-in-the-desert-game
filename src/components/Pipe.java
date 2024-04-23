package components;

import enumerations.Direction;
import enumerations.Shapes;
import interfaces.ILeakage;
import system.Cell;

public class Pipe extends Component implements ILeakage {
    private Shapes shape;
    private boolean isWaterFlowing;
    private long leakStartTime;
    private boolean isLeaking;
    private boolean isFull;
    private boolean isBroken;
    private boolean isPlayerOn;

    public Pipe(Cell location) {
        super(location);
        this.shape = Shapes.HORIZONTAL;
        this.isWaterFlowing = false;
        this.leakStartTime = 0;
        this.isLeaking = false;
        this.isBroken = false;
        this.isPlayerOn = false;
    }

    @Override
    public void startLeaking() {
        if (!isLeaking) {
            isLeaking = true;
            leakStartTime = System.currentTimeMillis();
            stopFlow();
        }
    }


    @Override
    public long stopLeaking() {
        if (isLeaking) {
            isLeaking = false;
            long duration = System.currentTimeMillis() - leakStartTime;
            undoStopFlow();
            return duration;
        }
        return 0;
    }

    public boolean isFull() {

        return isFull;
    }
    public void isWaterFlowing(boolean set){
        isWaterFlowing = set;
    }

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

    public void stopFlow() {
        isWaterFlowing = false;
        for (Direction direction : Direction.values()) {
            stopFlowRecursive(connectedComponents.get(direction));
        }
    }

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

    public void changeShape() {

    }

}
