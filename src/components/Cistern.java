package components;

public class Cistern extends Component {
    private boolean isCisternFull;
    private Component manufacturedComponent;

    public Cistern(){
        super();
        this.isCisternFull = false;
        this.manufacturedComponent = null;
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
        return 0;
    }

    public Pipe manufacturePipe(){
        Pipe newPipe = new Pipe();
        this.manufacturedComponent = newPipe;
        return newPipe;
    }

    public Pump manufacturePump(){
        Pump newPump = new Pump();
        this.manufacturedComponent = newPump;
        return newPump;
    }

    public void onCisternFull(){
        
    }
}
