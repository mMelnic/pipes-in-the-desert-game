package player;

import components.Cistern;
import components.Component;
import components.Pipe;
import components.Pump;
import components.Spring;
import enumerations.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import system.Cell;

/**
 * The Plumber class represents a movable player who can repair pipes, pumps,
 * and install components.
 * It extends the MovablePlayer class and provides additional functionality
 * specific to plumbers.
 */
public class Plumber extends MovablePlayer {

    private Component carriedComponent; // The component currently being carried by the plumber

    /**
     * Constructs a new Plumber object and assigns it to the specified team.
     *
     * @param team The team to which the plumber belongs.
     */
    public Plumber(Team team) {
        super(team);
    }

    /**
     * Attempts to repair a broken pipe in the current cell.
     * If the current cell contains a broken pipe, this method attempts to repair it
     * by setting its broken state to false and stopping any ongoing leaks.
     * If the pipe is successfully repaired, the method returns true; otherwise, it
     * returns false.
     * 
     * @return True if the repair is successful, false otherwise.
     */
    public boolean repairPipe() {
        if (currentCell.getComponent() instanceof Pipe) {
            Pipe pipe = (Pipe) currentCell.getComponent();
            if (pipe.isBroken()) {
                pipe.setBroken(false);
                handleOutput("The pipe is repaired.");
                // Check if the pipe is leaking and stop the leaking
                if (pipe.isLeaking()) {
                    long duration = pipe.stopLeaking();
                    ((PlumberScorer)team.getTeamScore()).updateScore(duration);
                    handleOutput("The pipe is repaired and stopped leaking.");
                }
                return true;
            }
            else if(!pipe.isBroken()) {
                handleOutput("The pipe is not broken.");
            }
        }
        else {
            handleOutput("You are not standing on a pipe.");
        }
        return false;
    }

    /**
     * Attempts to repair a broken pump in the current cell.
     * If the current cell contains a broken pump, this method attempts to repair it
     * by setting its broken state to false, stopping any ongoing leaks, and
     * resetting the reservoir if it's full.
     * If the pump is successfully repaired, the method returns true; otherwise, it
     * returns false.
     * 
     * @return True if the repair is successful, false otherwise.
     */
    public boolean repairPump() {
        if (currentCell.getComponent() instanceof Pump) {
            Pump pump = (Pump) currentCell.getComponent();
            if (pump.isBroken()) {
                pump.setBroken(false);
                handleOutput("The pump is repaired.");
                if (pump.isLeaking()) {
                    long duration = pump.stopLeaking();
                    ((PlumberScorer) team.getTeamScore()).updateScore(duration);
                    handleOutput("The pump is repaired and stopped leaking.");
                    if (pump.isReservoirFull()) {
                        pump.setReservoirFull(false);
                        pump.stopFlow();
                        handleOutput("The reservoir emptied.");
                    }
                }
                return true;
            }
            else {
                handleOutput("The pump is not broken.");
            }
        }
        else {
            handleOutput("You are not standing on a pump.");
        }
        return false;
    }

    /**
     * Installs a component in the specified direction from the plumber's current
     * position.
     * This method attempts to install a component in the specified direction from
     * the plumber's current position.
     * It first determines the target cell based on the given direction, then checks
     * if the plumber is carrying a component
     * and if the target cell is valid for installation. If these conditions are
     * met, it adds the carried component
     * to the target cell and establishes connections between the carried component
     * and the component in the current cell,
     * and vice versa, if applicable. For pipes and pumps, it establishes
     * connections based on their placement and functionality.
     * If the installation is successful, the method returns true; otherwise, it
     * returns false.
     * 
     * @param direction The direction in which to install the component.
     * @return True if the installation is successful, false otherwise.
     * 
     * @throws IllegalArgumentException if an invalid direction is specified.
     */

    public boolean installComponent(Direction direction) { // TODO what if the component in the current cell is a pump,
                                                           // how to set the outgoing/ incoming pipes?
        Cell targetCell = calculateTargetCell(direction);

        if (carriedComponent != null && targetCell != null) {
            Component componentInCurrentCell = currentCell.getComponent();
            Component componentInTargetCell = targetCell.getComponent();

            if (componentInTargetCell != null && !(carriedComponent instanceof Pump)) {
                handleOutput("The cell is not empty, you cannot install here.");
                return false;
            }

            if (carriedComponent instanceof Pipe && componentInTargetCell == null) {
                return installPipe(targetCell, componentInCurrentCell, direction);
            } else if (carriedComponent instanceof Pump
                    && (componentInTargetCell == null || componentInTargetCell instanceof Pipe)) {
                return installPump(targetCell, componentInCurrentCell, direction);
            }
        } else if (carriedComponent == null) {
            handleOutput("You have no component carried.");
        }

        return false;
    }

    private Cell calculateTargetCell(Direction direction) {
        switch (direction) {
            case UP:
                return currentCell.getMap().getUpwardCell(currentCell);
            case DOWN:
                return currentCell.getMap().getDownwardCell(currentCell);
            case LEFT:
                return currentCell.getMap().getLeftwardCell(currentCell);
            case RIGHT:
                return currentCell.getMap().getRightwardCell(currentCell);
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }

    private boolean installPipe(Cell targetCell, Component componentInCurrentCell, Direction direction) {
        Pipe pipe = (Pipe) carriedComponent;
        pipe.changeShape();

        try {
            componentInCurrentCell.addConnectedComponent(carriedComponent, direction);
            carriedComponent.addConnectedComponent(componentInCurrentCell, direction.getOppositeDirection());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not connect the components.");
            return false;
        }

        if (componentInCurrentCell instanceof Pipe) {
            Pipe currentCellPipe = ((Pipe) componentInCurrentCell);
            currentCellPipe.changeShape();
            if (currentCellPipe.isWaterFlowing()) {
                pipe.setWaterFlowing(true);
            }
        } else if (componentInCurrentCell instanceof Pump) {
            Pump currentCellPump = (Pump) componentInCurrentCell;
            if ((currentCellPump.getOutgoingPipe() != null && currentCellPump.getOutgoingPipe().isWaterFlowing()) ||
                    (currentCellPump.getIncomingPipe() != null && currentCellPump.getIncomingPipe().isWaterFlowing())) {
                pipe.setWaterFlowing(true);
            }
        }

        targetCell.placeComponent(pipe);
        carriedComponent = null;
        handleOutput("You have successfully installed a Pipe.");

        return true;
    }

    private boolean installPump(Cell targetCell, Component componentInCurrentCell, Direction direction) {
        Pump pump = (Pump) carriedComponent;
        // Check if targetCell's component is a Pipe and reconnect its connected components to the Pump
        if (targetCell.getComponent() instanceof Pipe) {
            Pipe pipeToBeReplaced = (Pipe) targetCell.getComponent();
            HashMap<Direction, Component> connectedComponents = pipeToBeReplaced.getConnectedComponents();
            for (Map.Entry<Direction, Component> entry : connectedComponents.entrySet()) {
                Direction connectedDirection = entry.getKey();
                Component connectedComponent = entry.getValue();
                try {
                    connectedComponent.removeConnectedComponent(pipeToBeReplaced);
                    pump.addConnectedComponent(connectedComponent, connectedDirection);
                    connectedComponent.addConnectedComponent(pump, connectedDirection.getOppositeDirection());
                } catch (Exception e) {
                    System.out.println("Could not reconnect the components.");
                    try {
                        connectedComponent.addConnectedComponent(pipeToBeReplaced, connectedDirection);
                    } catch (Exception f) {

                    }
                    return false;
                }
            }
            targetCell.placeComponent(pump);
            carriedComponent = null;
            handleOutput("You have successfully installed a Pump.");

            return true;
        }

        try {
            componentInCurrentCell.addConnectedComponent(carriedComponent, direction);
            carriedComponent.addConnectedComponent(componentInCurrentCell, direction.getOppositeDirection());
        } catch (Exception e) {
            System.out.println("Could not connect the components.");
            return false;
        }
        targetCell.placeComponent(pump);
        carriedComponent = null;
        handleOutput("You have successfully installed a Pump.");

        return true;
    }

    /**
     * Connects a pipe with a non-pipe and non-pump component, establishing
     * bidirectional connections
     * between them. If the component is a cistern and the pipe has flowing water,
     * it triggers the
     * fillCistern() method of the cistern. If the component is a spring, it calls
     * the startWaterSupply()
     * method of the spring.
     * 
     * @param pipe      The pipe component to connect.
     * @param component The non-pipe and non-pump component to connect the pipe
     *                  with.
     */

    public void connectPipeWithComponent(Pipe pipe, Component component) {
        if (component instanceof Pipe || component instanceof Pump || 
        !(pipe.getLocation().getMap().isNeighbouringCell(pipe.getLocation(), component.getLocation()))) {
            return;
        }

        try {
            // Add pipe to the connected components of the component
            component.addConnectedComponent(pipe, pipe.getLocation().getRelativeDirection(component.getLocation()));

            // Add component to the connected components of the pipe
            pipe.addConnectedComponent(component, component.getLocation().getRelativeDirection(pipe.getLocation()));

            // Change shape of the pipe
            pipe.changeShape();

            // Check the type of the component and take appropriate action
            if (component instanceof Cistern && pipe.isWaterFlowing()) {
                ((Cistern) component).fillCistern();
                handleOutput("Pipe connected to cistern.");
            } else if (component instanceof Spring) {
                ((Spring) component).startWaterSupply();
                handleOutput("Pipe connected to spring.");
            }
        } catch (Exception e) {
            handleOutput("Could not connect the components");
        }
    }

    // TODO: check the case when the pipe was reattached to a spring and now is
    // connected to a spring from two sides

    /**
     * Detaches a pipe from its current connection and connects it to a new
     * component.
     * If successful, it updates the connections, including setting incoming and
     * outgoing pipes for pumps
     * if necessary. If the pipe was connected to a pump, it adjusts the pump's
     * incoming and outgoing pipes
     * accordingly. Returns true if the detachment and reattachment are successful,
     * false otherwise.
     * 
     * @param newComponent The new component to attach the pipe to.
     * @param oldComponent The component from which to detach the pipe.
     * @return True if the detachment and reattachment are successful, false
     *         otherwise.
     */

    public boolean detachPipe(Component newComponent, Component oldComponent) {
        if (oldComponent instanceof Pipe) {
            handleOutput("The pipe is not connected to any active component.");
        } else if (newComponent instanceof Pipe) {
            handleOutput("The component that you have tried to connect is not an active component.");
        }
        if (newComponent instanceof Pipe || oldComponent instanceof Pipe) {
            return false;
        }

        if (currentCell.getComponent() instanceof Pipe) {
            Pipe pipe = (Pipe) currentCell.getComponent();
            if (!(pipe.getConnectedComponents().containsValue(oldComponent))) {
                handleOutput("The pipe is not connected to the given active component.");
            } else if (!(currentCell.getMap().isNeighbouringCell(newComponent.getLocation(), pipe.getLocation()))) {
                handleOutput("The component that you have tried to connect is not in an adjacent cell.");
            }

            try {
                if (pipe.getConnectedComponents().containsValue(oldComponent) &&
                        currentCell.getMap().isNeighbouringCell(newComponent.getLocation(), pipe.getLocation())) {
                    Direction pipeRelativeToNewComponent = pipe.getLocation()
                            .getRelativeDirection(newComponent.getLocation());
                    Direction newComponentRelativeToPipe = newComponent.getLocation()
                            .getRelativeDirection(pipe.getLocation());

                    // Check if the pipe has water flowing
                    if (pipe.isWaterFlowing()) {
                        currentCell.getMap().updateWaterFlow(); // TODO add a method to the Map class
                    }

                    // Update connected components
                    pipe.removeConnectedComponent(oldComponent);
                    oldComponent.removeConnectedComponent(pipe);
                    pipe.addConnectedComponent(newComponent, newComponentRelativeToPipe);
                    newComponent.addConnectedComponent(pipe, pipeRelativeToNewComponent);
                    pipe.changeShape();
                    handleOutput("You successfully redirected an end of a pipe.");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not detach the pipe");
                return false;
            }
        } else {
            handleOutput("You are not standing on a pipe.");
        }
        return false;
    }

    /**
     * Picks up a component from a neighboring cistern if available. If a cistern
     * has a manufactured
     * component, it picks it up, sets it as the carried component, and resets the
     * cistern's manufactured
     * component. If no cistern with an available component is found, it prints a
     * message indicating so.
     */
    public void pickComponent() {
        if (carriedComponent != null) {
            handleOutput("You already have a component carried.");
            return;
        }
        boolean cisternFound = false;
        // Check all connected components on the neighbouring cells to find a cistern
        for (Map.Entry<Direction, Component> entry : currentCell.getComponent().getConnectedComponents().entrySet()) {
            if (entry.getValue() instanceof Cistern) {
                cisternFound = true;
                Cistern cistern = (Cistern) entry.getValue();
                if (cistern.getManufacturedComponent() != null) {
                    carriedComponent = cistern.getManufacturedComponent();
                    cistern.setManufacturedComponent(null); // Reset the manufacturedComponent
                    handleOutput("You have picked a " + carriedComponent.getClass().getSimpleName());
                    return; // Exit the method once a component is picked
                }
                else {
                    handleOutput("There is no component available at the cistern.");
                }
            }
        }
        if (!cisternFound) {
            handleOutput("You are not close enough to any cistern.");
        }
        // Handle case when no cistern with available component is found
        System.out.println("No cistern with available component found in neighbouring cells.");
    }

    /**
     * Gets the currently carried component by the plumber.
     * 
     * @return The currently carried component.
     */
    public Component getCarriedComponent() {
        return carriedComponent;
    }

    /**
     * Sets the currently carried component by the plumber.
     * 
     * @param carriedComponent The component to be carried.
     */
    public void setCarriedComponent(Component carriedComponent) {
        this.carriedComponent = carriedComponent;
    }

}
