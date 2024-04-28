package components;

import java.util.Timer;
import java.util.TimerTask;

public class Cistern extends Component {
    private boolean isCisternFull;
    private Component manufacturedComponent;
    private int currentwater;
    private int capacity;

    public Cistern(){
        super();
        this.isCisternFull = false;
        this.manufacturedComponent = null;
        this.currentwater = 0;
        this.capacity = 100; //adjustable
    }

    public boolean getIsCisternFull(){
        return isCisternFull;
    }
    public void setIsCisternFull(boolean bool){
        this.isCisternFull = bool;
    }
    public Component getManufacturedComponent(){
        return manufacturedComponent;
    }
    public void setManufacturedComponent(Component c){
        this.manufacturedComponent = c;
    }

    public long fillCistern(){
        writeMessageToCMD("started filling cistern.");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentwater++;

                if (currentwater >= capacity) {
                    timer.cancel();
                    onCisternFull();
                }
            }
        }, 0, 1000);
        return 0;
    }

    public Pipe manufacturePipe(){
        writeMessageToCMD("pipe successfully manufactured.");
        Pipe newPipe = new Pipe();
        this.manufacturedComponent = newPipe;
        return newPipe;
    }

    public Pump manufacturePump(){
        writeMessageToCMD("pump successfully manufactured.");
        Pump newPump = new Pump();
        this.manufacturedComponent = newPump;
        return newPump;
    }

    public void onCisternFull(){
       
    }
}
