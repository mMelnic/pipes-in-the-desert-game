package player;

import java.util.Map;
import components.Cistern;
import components.Component;
import components.Pipe;
import components.Pump;
import components.Spring;
import enumerations.Direction;
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
    public boolean repairPipe(String filePath) {
        if (currentCell.getComponent() instanceof Pipe) {
            Pipe pipe = (Pipe) currentCell.getComponent();
            if (pipe.isBroken()) {
                pipe.setBroken(false);
                handleOutput("The pipe is repaired.", filePath);
                // Check if the pipe is leaking and stop the leaking
                if (pipe.isLeaking()) {
                    pipe.stopLeaking();
                    handleOutput("The pipe is repaired and stopped leaking.", filePath);
                }
                return true;
            }
            else if(!pipe.isBroken()) {
                handleOutput("The pipe is not broken.", filePath);
            }
        }
        else {
            handleOutput("You are not standing on a pipe.", filePath);
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
    public boolean repairPump(String filePath) {
        if (currentCell.getComponent() instanceof Pump) {
            Pump pump = (Pump) currentCell.getComponent();
            if (pump.isBroken()) {
                pump.setBroken(false);
                handleOutput("The pump is repaired.", filePath);
                if (pump.isLeaking()) {
                    pump.stopLeaking();
                    handleOutput("The pump is repaired and stopped leaking.", filePath);
                    if (pump.isReservoirFull()) {
                        pump.setReservoirFull(false);
                        pump.stopFlow();
                        handleOutput("The reservoir emptied.", filePath);
                    }
                }
                return true;
            }
            else {
                handleOutput("The pump is not broken.", filePath);
            }
        }
        else {
            handleOutput("You are not standing on a pump.", filePath);
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

    public boolean installComponent(Direction direction, String filePath) {
        Cell targetCell;

        switch (direction) {
            case UP:
                targetCell = currentCell.getMap().getUpwardCell(currentCell);
                break;
            case DOWN:
                targetCell = currentCell.getMap().getDownwardCell(currentCell);
                break;
            case LEFT:
                targetCell = currentCell.getMap().getLeftwardCell(currentCell);
                break;
            case RIGHT:
                targetCell = currentCell.getMap().getRightwardCell(currentCell);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }

        if (carriedComponent != null && targetCell != null) {
            Component componentInCurrentCell = currentCell.getComponent();
            Component componentInTargetCell = targetCell.getComponent();
            if (componentInTargetCell != null) {
                handleOutput("The cell is not empty, you cannot install here.", filePath);
            }

            if (carriedComponent instanceof Pipe && componentInTargetCell == null) {
                Pipe pipe = (Pipe) carriedComponent;
                pipe.changeShape();

                try {
                    componentInCurrentCell.addConnectedComponent(carriedComponent, direction);
                    // TODO add a getOppositeDirection method to Direction
                    carriedComponent.addConnectedComponent(componentInCurrentCell, direction.getOppositeDirection());
                } catch (Exception e) {
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

                    // Set outgoing pipe if there is no outgoing pipe
                    if (currentCellPump.getOutgoingPipe() == null) {
                        currentCellPump.setOutgoingPipe(pipe);
                    }

                    // Set incoming pipe if there is no incoming pipe
                    if (currentCellPump.getIncomingPipe() == null) {
                        currentCellPump.setIncomingPipe(pipe);
                    }
                    // Theoretically there should be no scenario when there is no incoming and no outgoing pipe
                }

                // currentCell.getMap().checkForFreeEnds(); // If free ends are found start leaking
                targetCell.placeComponent(pipe);
                carriedComponent = null;
                handleOutput("You have successfully installed a Pipe.", filePath);

                return true;
            } else if (carriedComponent instanceof Pump
                    && (componentInTargetCell == null || componentInTargetCell instanceof Pipe)) {
                Pump pump = (Pump) carriedComponent;

                try {
                    componentInCurrentCell.addConnectedComponent(carriedComponent, direction);
                    carriedComponent.addConnectedComponent(componentInCurrentCell, direction.getOppositeDirection());
                } catch (Exception e) {
                    System.out.println("Could not connect the components.");
                    return false;
                }

                if (componentInCurrentCell instanceof Pipe) {
                    ((Pipe) componentInCurrentCell).changeShape();
                    pump.setIncomingPipe((Pipe)componentInCurrentCell);
                }
                targetCell.placeComponent(pump);
                carriedComponent = null;
                handleOutput("You have successfully installed a Pump.", filePath);

                return true;
                // TODO what if the component in the current cell is a pump, how to set the outgoing/ incoming pipes?
            }
        } else if (carriedComponent == null) {
            handleOutput("You have no component carried.", null);
        }

        return false;
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
        if (component instanceof Pipe || component instanceof Pump) {
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
            } else if (component instanceof Spring) {
                ((Spring) component).startWaterSupply(true);
            }
        } catch (Exception e) {
            System.out.println("Could not connect the components");
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

    public boolean detachPipe(Component newComponent, Component oldComponent, String filePath) {
        if (oldComponent instanceof Pipe) {
            handleOutput("The pipe is not connected to any active component.", filePath);
        } else if (newComponent instanceof Pipe) {
            handleOutput("The component that you have tried to connect is not an active component.", filePath);
        }
        if (newComponent instanceof Pipe || oldComponent instanceof Pipe) {
            return false;
        }

        if (currentCell.getComponent() instanceof Pipe) {
            Pipe pipe = (Pipe) currentCell.getComponent();
            if (!(pipe.getConnectedComponents().containsValue(oldComponent))) {
                handleOutput("The pipe is not connected to the given active component.", filePath);
            } else if (!(currentCell.getMap().isNeighbouringCell(newComponent.getLocation(), pipe.getLocation()))) {
                handleOutput("The component that you have tried to connect is not in an adjacent cell.", filePath);
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

                    // Handle Pump logic
                    if (newComponent instanceof Pump) {
                        Pump newPump = (Pump) newComponent;

                        // If the pump has no incoming pipe, set the pipe as incoming
                        if (newPump.getIncomingPipe() == null) {
                            newPump.setIncomingPipe(pipe);
                        }

                        // If the pump has no outgoing pipe, set it as outgoing
                        if (newPump.getOutgoingPipe() == null) {
                            newPump.setOutgoingPipe(pipe);
                        }

                        // If no outgoing or incoming, set it as incoming
                        if (newPump.getIncomingPipe() == null && newPump.getOutgoingPipe() == null) {
                            newPump.setIncomingPipe(pipe);
                        }
                    }

                    // Handle Pump removal logic
                    if (oldComponent instanceof Pump) {
                        Pump oldPump = (Pump) oldComponent;

                        // If the pipe was outgoing or incoming, set the outgoing or incoming pipe to
                        // null
                        if (oldPump.getOutgoingPipe() == pipe) {
                            oldPump.setOutgoingPipe(null);
                        }

                        if (oldPump.getIncomingPipe() == pipe) {
                            oldPump.setIncomingPipe(null);
                        }
                    }

                    handleOutput("You successfully redirected an end of a pipe.", filePath);
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not detach the pipe");
                return false;
            }
        } else {
            handleOutput("You are not standing on a pipe.", filePath);
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
    public void pickComponent(String filePath) {
        if (carriedComponent != null) {
            handleOutput("You already have a component carried.", filePath);
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
                    handleOutput("You have picked a " + carriedComponent.getClass().getSimpleName(), filePath);
                    return; // Exit the method once a component is picked
                }
                else {
                    handleOutput("There is no component available at the cistern.", filePath);
                }
            }
        }
        if (!cisternFound) {
            handleOutput("You are not close enough to any cistern.", filePath);
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
