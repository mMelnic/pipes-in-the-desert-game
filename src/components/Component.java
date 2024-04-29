package components;
import java.util.HashMap;
import java.util.UUID;
import java.util.Iterator;
import java.util.Map;

import enumerations.*;
import system.*;
import exceptions.*;
/**
 * Component class represents components that are in the game 
 * i.e. Cistern, Spring, Pump, Pipe.
 */
public class Component {
    //an attribute storing location cell of a component
    protected Cell location;
    //an attribute storing unique id of a component
    protected UUID componentID;
    //an attribute storing connected components of a component
    protected HashMap<Direction, Component> connectedComponents;
    /**
     * constructor of Component class, sets appropriate values to certain attributes of the class */
    public Component(){
        this.location = null;
        this.connectedComponents  = new HashMap<Direction, Component>();
        this.componentID = UUID.randomUUID();
    }
    /**
     * gets the Cell the Component is on.
     * 
     * @return the Cell the component currently on.
     */
    public Cell getLocation(){
        return this.location;
    }
    /**
     * sets location of a Component.
     * 
     * @param cell the Cell the component to be set.
     */
    public void setLocation(Cell cell){
        this.location = cell;
    }
    /**
     * gets ID of a component.
     * @return the Id of a component
     */
    public UUID getComponentID() {
        return componentID;
    }
    /**
     * gets the HashMap that stores connedted components of the component.
     * @return Hashmap storing connected components of the component.
     */
    public HashMap<Direction, Component> getConnectedComponents() {
        return connectedComponents;
    }
    /**
     * a method that adds a component to a components connected component set,
     * i.e. adds a pair of direction and component to the HashMap storing connected components of a 
     * certain component.
     * @param c a component to be added.
     * @param d a direction of the component's(this) neighboring cell where the component (c) is to be added.
     * @return a boolean representing success of the process i.e. true if the component (c) succfully added,
     * false if the component (c) failed to be added.
     * @throws PumpConnectablePipeNumberExceedException when a plumber tried to connect a component to a pump
     * while it is already connected to maximum number of connectable component, e.g. if a pump is 
     * connectable to 2 components and already is connected to 2 component, then a plumber tried to connect
     * 3rd component, in that case the exceprion is thrown.
     * @throws CisternMultipleComponentsConnectedException the way the game is designed is a spring can be 
     * connected to at most 1 component, i.e. a pump or a pipe. the exception is thrown when a plumber tried
     * to connect more that one components to a Spring.
     * @throws SpringMultipleComponensConnectedException the way the game is designed is a cistern can be
     * connected to at most 1 component, i.e. a pump or a pipe. The exception is thrown when i plumber tried
     * to connected more that 1 componetn to a cistern.
     */
    public boolean addConnectedComponent(Component c, Direction d) throws PumpConnectablePipeNumberExceedException, CisternMultipleComponentsConnectedException, SpringMultipleComponensConnectedException{
        //integer storing row of the cell the new component is to be added.
        int newRow = 0;
        //column storing column of the cell the new component is to be added.
        int newColumn = 0;
        try{
            //initialixation of the row an column of the cell the new component is to be added.
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
        //the initialization of the row and column is failed an exception is thrown and returns false,
        //implying the process was unsuccessful.
        catch(Exception e){
            return false;
        }
        try {
            //if the cell is not empty i.e. occupied, an exception is thrown, and returns false
            if(this.location.getMap().getCells(newRow, newColumn).isEmpty() == false){
                throw new CellOccupiedException("the cell is not empty!");
            } 
            else 
            {
                //if the component that is receiving a new component to its neighbouring cell
                //is a pipe we add them
                if(this instanceof Pipe){
                    this.connectedComponents.put(d, c);
                    writeMessageToCMD("component successfully added.");
                } 
                //if the component that is receiving a new component to its neighbouring cell
                //is a pump, prior to add
                //first checks connectable pipe number and currently connected pipe
                else if(this instanceof Pump) {
                    if(this.connectedComponents.size() < ((Pump) this).getConnectablePipesNumber()){
                        this.connectedComponents.put(d, c);
                        writeMessageToCMD("component successfully added.");
                        return true;
                    }
                    else{
                        throw new PumpConnectablePipeNumberExceedException("the pump is connected to max number of pipes already.");
                    }
                }
                //if the component that is receiving a new component to its neighbouring cell is a cistern
                //prior to add, first checks if it is already connected to a component.
                else if(this instanceof Cistern) {
                    if(this.connectedComponents.size() < 1){
                        this.connectedComponents.put(d, c);
                        writeMessageToCMD("component successfully added.");
                        return true;
                    } else {
                        throw new CisternMultipleComponentsConnectedException("the cistern is already connected to a component.");
                    }
                }
                //if the component that is receiving a new component to its neighbouring cell is a spring
                //prior to add, first checks if it is already connected to a component.
                else if(this instanceof Spring){
                    if(this.connectedComponents.size() < 1){
                        this.connectedComponents.put(d ,c);
                        writeMessageToCMD("component successfully added.");
                        return true;
                    } else {
                        throw new SpringMultipleComponensConnectedException("the Spring is already connected to a component.");
                    }
                }
            }
        } catch (CellOccupiedException e) {
            return false;
        }
        return false;
    }
    /**
     * a method that is used to deconnect a certain component from a component, i.e. to remove a pair of
     * direction and a component (c) from a Hashmap storing pairs of a direction and a component representing
     * connected components of a component.
     * @param c a component to be removed from the HashMap storing connected components
     * @return returns true if successfully removed, false is the removing process failed.
     */
    public boolean removeConnectedComponent(Component c){
        Iterator<Map.Entry<Direction, Component>> iterator = connectedComponents.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Direction, Component> entry = iterator.next();
            if (entry.getValue() == c) {
                iterator.remove();
                writeMessageToCMD("component successfully removed.");
                return true;
            }
        }
        return false;
    }

    public void writeMessageToCMD(String string){
        System.out.println(string);
    }

}
