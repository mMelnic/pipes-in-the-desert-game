package components;
import java.util.HashMap;
import java.util.UUID;
import java.util.Iterator;
import java.util.Map;

import enumerations.*;
import interfaces.*;
import system.*;
import exceptions.*;

import player.*;

public class Component {
    protected Cell location;
    protected UUID componentID;
    protected HashMap<Direction, Component> connectedComponents;
    //protected string componentType;

    public Component(){
        this.location = null;
        //this.componentType = null;
        this.connectedComponents  = new HashMap<Direction, Component>();
        this.componentID = UUID.randomUUID();
    }

    public Cell getLocation(){
        return this.location;
    }
    public void setLocation(Cell cell){
        this.location = cell;
    }

    public UUID getComponentID() {
        return componentID;
    }

    public HashMap<Direction, Component> getConnectedComponents() {
        return connectedComponents;
    }

    public boolean addConnectedComponent(Component c, Direction d) throws PumpConnectablePipeNumberExceedException, CisternMultipleComponentsConnectedException, SpringMultipleComponensConnectedException{
        int newRow = 0;
        int newColumn = 0;
        try{
            
            switch(d){
                case UP:
                    newRow = this.location.getRow() - 1;
                    newColumn = this.location.getColumn();
                case DOWN:
                    newRow = this.location.getRow() + 1;
                    newColumn = this.location.getColumn();
                case LEFT:
                    newRow = this.location.getRow();
                    newColumn = this.location.getColumn() - 1;
                case RIGHT:
                    newRow = this.location.getRow();
                    newColumn = this.location.getColumn() + 1;
            }
        }
        catch(Exception e){
            return false;
        }
        if(this.location.getMap().getCells(newRow, newColumn).getIsEmpty() == false){
            throw new CellOccupiedException("the cell is not null!");
            return false;
        } else {
            if(this instanceof Pipe){
                this.connectedComponents.put(d, c);
            } 
            else if(this instanceof Pump) {
                if(this.connectedComponents.size() < this.getConnectablePipesNumber()){
                    this.connectedComponents.put(d, c);
                    return true;
                }
                else{
                    throw new PumpConnectablePipeNumberExceedException("the pump is connected to max number of pipes already.");
                    return false;
                }
            }
            else if(this instanceof Cistern) {
                if(this.connectedComponents.size() < 1){
                    this.connectedComponents.put(d, c);
                    return true;
                } else {
                    throw new CisternMultipleComponentsConnectedException("the cistern is already connected to a component.");
                    return false;
                }
            }
            else if(this instanceof Spring){
                if(this.connectedComponents.size() < 1){
                    this.connectedComponents.put(d ,c);
                    return true;
                } else {
                    throw new SpringMultipleComponensConnectedException("the Spring is already connected to a component.");
                    return false;
                }
            }
        }
        return false;
    }

    public boolean removeConnectedComponent(Component c){
        Iterator<Map.Entry<Direction, Component>> iterator = connectedComponents.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Direction, Component> entry = iterator.next();
            if (entry.getValue() == c) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }


}
