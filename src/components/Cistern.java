package components;
import interfaces.ICisternListener;
import system.Cell;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
/**
 * a class Cistern represents cistern in the game, extends class component
 */
public class Cistern extends Component implements ICisternListener {
    //an attribute indicating if a cistern is full
    private boolean isCisternFull;
    //an ttribute storing manufactured component that is avaible to pick from a cistern
    private Component manufacturedComponent;
    //an attribute storing how much water currently is in a cistern
    private int currentwater;
    //an attribute storing capacity of cistern
    private int capacity;
    //an attribute indicating if a cistern is leaking
    private boolean isLeaking;
    /**
     * a constructor of the class cistern that sets certion attributes
     */
    public Cistern(Cell cell){
        super(cell);
        this.isCisternFull = false;
        this.manufacturedComponent = null;
        this.currentwater = 0;
        this.capacity = 100; //adjustable
        this.isLeaking = false;
    }
    /**
     * a method that returns is a cistern is full
     * @return state of a cistern being full
     */
    public boolean getIsCisternFull(){
        return isCisternFull;
    }
    /**
     * a method that sets attribute of a cistern is full
     * @param bool boolean variable that the isCisternFull attribute is to be get
     */
    public void setIsCisternFull(boolean bool){
        this.isCisternFull = bool;
    }
    /**
     * a method returns the manufactured component that is currently available to pick at the cistern
     * @return
     */
    public Component getManufacturedComponent(){
        return manufacturedComponent;
    }
    /**
     * a method that sets the manufactured component at a cistern
     * @param c
     */
    public void setManufacturedComponent(Component c){
        this.manufacturedComponent = c;
    }
    /**
     * an attribute that returns id a cistern is leaking
     * @return true if the cistern is leaking, false if the cistern is not leaking
     */
    public boolean getIsLeaking(){
        return isLeaking;
    }
    /**
     * an attribute that sets a leaking state of a cistern
     * @param bool a boolean variable that isLeaking attribute of the cistern to be set
     */
    public void setIsLeaking(boolean bool){
        isLeaking = bool;
    }
    /**
     * a metho fills a cistern with water
     * @return returns current time in milliseconds.
     */
    public long fillCistern(){
        writeMessageToCMD("started filling cistern.");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentwater + 1 <= capacity) {
                    currentwater++;
                }
                else{
                    timer.cancel();
                    onCisternFull();
                }

            }
        }, 0, 1000);
        return System.currentTimeMillis();
    }
    /**
     * a method manufactures a pipe at a cistern
     * @return returns a pipe that is newly manufactured at a cistern
     */
    public Pipe manufacturePipe(){
        writeMessageToCMD("pipe successfully manufactured.");
        Pipe newPipe = new Pipe(location);
        this.manufacturedComponent = newPipe;
        return newPipe;
    }
    /**
     * e method that manufactures a pump
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
        this.manufacturedComponent = newPump;
        return newPump;
    }

    /**
     * a method let the cistern leak when it is full
     */
    public void onCisternFull(){
        isLeaking = true;
        while(isLeaking){
            //update saboteur score
        }
    }
    /**
     * a method that manufactures a random component, i.e. a pipe or a pump, at a cistern
     * @return returns newly manufactured component at cistern
     */
    public Component manufactureComponent(){
        if(manufacturedComponent == null){
            int random = new Random().nextInt();
            if(random % 2 == 0){
                return manufacturePipe();
            }
            return manufacturePump();
        }
        return null; 
    }
}
