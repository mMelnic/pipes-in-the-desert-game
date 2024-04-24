package system;

import components.Component;
import components.Pipe;
import components.Pump;

public class Cell {
    Component component;
    boolean isEmpty;
    boolean isPlayerOn;
    int rows;
    int column;
    Map map;

    public Component getComponent() {
        return component;
    }

    public void placeComponent(Component newElement){
        component = newElement;
    }

    public boolean[] checkPumpOrPipe(){
        if (component instanceof Pipe) {
            return new boolean[]{true, false};
        } else if (component instanceof Pump) {
            return new boolean[]{false, true};
        } else {
            return new boolean[]{false, false};
        }
    }

    public Map getMap() {
        return map;
    }
}
