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
}
