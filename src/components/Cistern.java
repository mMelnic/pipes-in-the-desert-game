package components;
import interfaces.ICisternListener;
import interfaces.IScorer;
import interfaces.IWaterFlowListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import system.Cell;
/**
 * a class Cistern represents cistern in the game, extends class component
 */
public class Cistern extends Component implements IWaterFlowListener {
    //an attribute indicating if a cistern is full
    private boolean isCisternFull;
    //an ttribute storing manufactured component that is avaible to pick from a cistern
    private Component manufacturedComponent;
    //an attribute storing how much water currently is in a cistern
    private boolean isFilling;
    private long startTime;
    private long elapsedTime;
    private Thread fillingThread;
    private List<IScorer> scorers = new ArrayList<>();
    private List<ICisternListener> listeners = new ArrayList<>();

    /**
     * a constructor of the class cistern that sets certion attributes
     */
    public Cistern(Cell cell){
        super(cell);
        this.isCisternFull = false;
        this.manufacturedComponent = null;
        this.isFilling = false;
        this.startTime = 0;
        this.elapsedTime = 0;
        this.fillingThread = null;
    }

    public void addScorer(IScorer scorer) {
        scorers.add(scorer);
    }

    public void removeScorer(IScorer scorer) {
        scorers.remove(scorer);
    }

    private void notifyScorers(long fillingDuration) {
        for (IScorer scorer : scorers) {
            scorer.updateScore(fillingDuration);
        }
    }

    public void addCisternFullListener(ICisternListener listener) {
        listeners.add(listener);
    }

    public void removeCisternFullListener(ICisternListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ICisternListener listener : listeners) {
            listener.onCisternFullCheck();
        }
    }

    @Override
    public void onWaterFlowChanged(Pipe pipe) {
        if (isCisternFull) {
            System.out.println("Cistern is already full and cannot be filled again.");
            onCisternFull(this);
            return; // If the cistern is full, do nothing
        }
        if (connectedComponents.size() < 1) {
            stopFilling();
        }
        if (pipe.isWaterFlowing()) {
            fillCistern();
        } else {
            stopFilling();
        }
    }

    /**
     * a method manufactures a pipe at a cistern
     * 
     * @return returns a pipe that is newly manufactured at a cistern
     */
    public Pipe manufacturePipe() {
        writeMessageToCMD("pipe successfully manufactured.");
        Pipe newPipe = new Pipe(location);
        this.manufacturedComponent = newPipe;
        newPipe.addScorer(location.getMap().getSaboteurScorer());
        return newPipe;
    }

    /**
     * e method that manufactures a pump
     * 
     * @return returns a pipe that is newly manufactured at a cistern
     */
    public Pump manufacturePump() {
        writeMessageToCMD("Pump successfully manufactured.");
        int probability = new Random().nextInt(100) + 1; // Generates a random number between 1 and 10
        int parameter;
        if (probability <= 50) {
            parameter = 4; // Probability of 4: 60%
        } else if (probability <= 85) {
            parameter = 3; // Probability of 3: 30%
        } else {
            parameter = 2; // Probability of 2: 10%
        }
        Pump newPump = new Pump(parameter, location);
        newPump.addScorer(location.getMap().getSaboteurScorer());
        this.manufacturedComponent = newPump;
        return newPump;
    }

    /**
     * a method that manufactures a random component, i.e. a pipe or a pump, at a
     * cistern
     * 
     * @return returns newly manufactured component at cistern
     */
    public Component manufactureComponent() {
        if (manufacturedComponent == null) {
            Random random = new Random();
            int randomValue = random.nextInt(100); // Generate a random number between 0 and 99

            if (randomValue < 80) {
                return manufacturePipe();
            } else {
                return manufacturePump();
            }
        }
        return null;
    }

    /**
     * a metho fills a cistern with water
     * @return returns current time in milliseconds.
     */
    public synchronized void fillCistern() {
        if (isCisternFull) {
            return;
        }
        if (isFilling) {
            System.out.println("Cistern is already being filled.");
            return;
        }
        isFilling = true;
        startTime = System.currentTimeMillis();

        if (fillingThread == null || !fillingThread.isAlive()) {
            fillingThread = new Thread(() -> {
                try {
                    while (isFilling && elapsedTime < 180000) {
                        Thread.sleep(100); // Check every 100ms
                        long currentTime = System.currentTimeMillis();
                        long elapsedUpdate = currentTime - startTime;
                        elapsedTime += elapsedUpdate; // Accumulate elapsed time
                        startTime = currentTime; // Update startTime to current time
                        notifyScorers(elapsedUpdate); // Notify listeners of the update
                    }

                    if (elapsedTime >= 180000) {
                        System.out.println("Cistern filled.");
                        isCisternFull = true; // Set the flag to true when the cistern is filled
                        isFilling = false;
                        elapsedTime = 0;
                        notifyListeners();
                        onCisternFull(this);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    elapsedTime += System.currentTimeMillis() - startTime;
                }
            });
            fillingThread.start();
        }
    }

    public synchronized long stopFilling() {
        if (isCisternFull) {
            System.out.println("Cistern is full and cannot be stopped.");
            return elapsedTime;
        }
        System.out.println("Stopping the filling of the cistern due to no water flow.");
        isFilling = false;
        System.out.println(elapsedTime);
        return elapsedTime;
    }


    /**
     * a method let the cistern leak when it is full
     */
    public void onCisternFull(Component startComponent) {
        Set<Component> visited = new HashSet<>();
        onCisternFullRecursive(startComponent, visited);
    }
    
    private void onCisternFullRecursive(Component startComponent, Set<Component> visited) {
        // Base case: If the startComponent is a Pump, call its fillReservoir method
        if (startComponent instanceof Pump) {
            Pump pump = (Pump) startComponent;
            if (!pump.isReservoirFull()) {
                pump.fillReservoir();
            }
            else if (pump.isReservoirFull() && !pump.isLeaking()) {
                pump.startLeaking();
            }
            return;
        }
    
        // Mark the current component as visited
        visited.add(startComponent);
    
        // Traverse the connected components recursively
        for (Component connectedComponent : startComponent.getConnectedComponents().values()) {
            // Check if the connected component has not been visited before
            if (!visited.contains(connectedComponent) && connectedComponent instanceof Pipe) {
                Pipe pipe = (Pipe) connectedComponent;
                // Check if water is flowing in the pipe
                if (pipe.isWaterFlowing()) {
                    // Set the isFull attribute to true for the Pipe
                    pipe.setFull(true);
                    // Recursive call to traverse the connected components of the connected component
                    onCisternFullRecursive(connectedComponent, visited);
                } else {
                    // If water is not flowing, stop the method
                    return;
                }
            } else if (connectedComponent instanceof Pump) {
                // Recursive call to handle the Pump
                onCisternFullRecursive(connectedComponent, visited);
            }
        }
    }


    /**
     * a method that returns is a cistern is full
     * 
     * @return state of a cistern being full
     */
    public boolean getIsCisternFull() {
        return isCisternFull;
    }

    public boolean getIsCisternFilling() {
        return isFilling;
    }

    public void setIsCisternFilling(boolean isFillingState) {
        isFilling = isFillingState;
    }

    /**
     * a method that sets attribute of a cistern is full
     * 
     * @param bool boolean variable that the isCisternFull attribute is to be get
     */
    public void setIsCisternFull(boolean bool) {
        this.isCisternFull = bool;
    }

    /**
     * a method returns the manufactured component that is currently available to
     * pick at the cistern
     * 
     * @return
     */
    public Component getManufacturedComponent() {
        return manufacturedComponent;
    }

    /**
     * a method that sets the manufactured component at a cistern
     * 
     * @param c
     */
    public void setManufacturedComponent(Component c) {
        this.manufacturedComponent = c;
    }
}
