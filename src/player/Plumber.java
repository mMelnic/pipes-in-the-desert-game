package player;

import java.util.Map;
import components.Pipe;
import components.Pump;
import enumerations.Direction;
import system.Cell;

public class Plumber extends MovablePlayer {

    private Component carriedComponent;

    public Plumber(Team team) {
        super(team);
    }

    public boolean repairPipe() {
        if (currentCell.getComponent() instanceof Pipe) {
            Pipe pipe = (Pipe) currentCell.getComponent();
            if (pipe.isBroken()) {
                pipe.setBroken(false);
                // Check if the pipe is leaking and stop the leaking
                if (pipe.isLeaking()) {
                    pipe.stopLeaking();
                }
                return true;
            }
        }
        return false;
    }
    // TODO do something if the pipe has a free end so that it leaks if water is
    // flowing

    public boolean repairPump() {
        if (currentCell.getComponent() instanceof Pump) {
            Pump pump = (Pump) currentCell.getComponent();
            if (pump.isBroken()) {
                pump.setBroken(false);
                if (pump.isLeaking()) {
                    pump.stopLeaking();
                    if (pump.isReservoirFull()) {
                        pump.setReservoirFull(false);
                        pump.stopFlow();
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean installComponent(Direction direction) {
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

            try {
                componentInCurrentCell.addConnectedComponent(carriedComponent, direction);
                // TODO add a getOppositeDirection method to Direction
                carriedComponent.addConnectedComponent(componentInCurrentCell, direction.getOppositeDirection());
            } catch (Exception e) {
                System.out.println("Could not connect the components.");
                return false;
            }

            if (carriedComponent instanceof Pipe && componentInTargetCell == null) {
                Pipe pipe = (Pipe) carriedComponent;
                pipe.changeShape();

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
                    // Theoretically there should be no scenario when there is no incoming and outgoing pipe
                }

                currentCell.getMap().checkForFreeEnds(); // TODO add method in the Map class
                targetCell.placeComponent(pipe);
                carriedComponent = null;

                return true;
            } else if (carriedComponent instanceof Pump
                    && (componentInTargetCell == null || componentInTargetCell instanceof Pipe)) {
                Pump pump = (Pump) carriedComponent;

                if (componentInCurrentCell instanceof Pipe) {
                    ((Pipe) componentInCurrentCell).changeShape();
                    pump.setIncomingPipe(componentInCurrentCell);
                }
                targetCell.placeComponent(pump);
                carriedComponent = null;

                return true;
                // TODO what if the component in the current cell is a pump, how to set the outgoing/ incoming pipes?
            }
        }

        return false;
    }

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
                ((Spring) component).startWaterSupply();
            }
        } catch (Exception e) {
            System.out.println("Could not connect the components");
        }
    }

    // TODO: check the case when the pipe was reattached to a spring and now is
    // connected to a spring from two sides
    public boolean detachPipe(Component newComponent, Component oldComponent) {
        if (newComponent instanceof Pipe || oldComponent instanceof Pipe) {
            return false;
        }

        if (currentCell.getComponent() instanceof Pipe) {
            Pipe pipe = (Pipe) currentCell.getComponent();

            try {
                if (pipe.getConnectedComponents().contains(oldComponent) &&
                        currentCell.getMap().isNeighbouringCell(newComponent.getLocation(), pipe.getLocation())) {

                    // TODO: Add a new method in the Map class
                    // TODO: Add method to the Cell class
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

                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not detach the pipe");
                return false;
            }
        }
        return false;
    }

    public void pickComponent() {
        // Check all connected components on the neighbouring cells to find a cistern
        for (Map.Entry<Direction, Component> entry : currentCell.getComponent().getConnectedComponents().entrySet()) {
            if (entry.getValue() instanceof Cistern) {
                Cistern cistern = (Cistern) entry.getValue();
                if (cistern.getManufacturedComponent() != null) {
                    carriedComponent = cistern.getManufacturedComponent();
                    cistern.setManufacturedComponent(null); // Reset the manufacturedComponent
                    return; // Exit the method once a component is picked
                }
            }
        }
        // Handle case when no cistern with available component is found
        System.out.println("No cistern with available component found in neighbouring cells.");
    }

    public Component getCarriedComponent() {
        return carriedComponent;
    }

    public void setCarriedComponent(Component carriedComponent) {
        this.carriedComponent = carriedComponent;
    }

}
