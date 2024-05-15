package components;

import java.util.HashSet;
import java.util.Set;

import system.Cell;

/**
 * a Spring class represents springs in tha game
 */
public class Spring extends Component 
{
	//an attribute indicating if there is water flow from the spring
	private boolean isWaterFlowing;
	/**
	 * constructor of the class
	 */
	public Spring(Cell cell) 
	{
		super(cell);
		isWaterFlowing = false;
	}
	/**
	 * a method that starts water supply from a spring which sets an attribute isWaterFlowing to true.
	 */
	public void startWaterSupply() {
		isWaterFlowing = true;
		// Initialize a set to keep track of visited components
		Set<Component> visited = new HashSet<>();
		// Call the recursive method to start water supply
		startWaterSupplyDFS(this, visited);
		this.getLocation().getMap().checkForFreeEnds();
	}

	// Recursive method to start water supply using depth-first search (DFS)
	public void startWaterSupplyDFS(Component component, Set<Component> visited) {
		// Mark the current component as visited
		visited.add(component);

		// If the current component is a pipe, set its isWaterFlowing attribute to true
		if (component instanceof Pipe) {
			Pipe pipe = (Pipe) component;
			pipe.setWaterFlowing(true);
			if (pipe.isBroken()) {
				pipe.startLeaking();
				return;
			}
			// Check if the pipe has only one connected component
			if (pipe.getConnectedComponents().size() == 1) {
				return; // Stop traversal if the pipe has only one connected component
			}
		} else if (component instanceof Pump) {
			Pump pump = (Pump) component;
        	if (pump.isBroken()) {
            	pump.fillReservoir();
            	return; // Stop traversal if the pump is broken
			}
			// Check if the pump has only one connected component
			if (pump.getConnectedComponents().size() == 1) {
				return; // Stop traversal if the pump has only one connected component
			}
		}
		
		// Traverse the connected components recursively
		for (Component connectedComponent : component.getConnectedComponents().values()) {
			// Skip if the connected component has already been visited
			if (!visited.contains(connectedComponent)) {
				handleConnectedComponent(component, connectedComponent, visited);
			}
		}
	}

	private void handleConnectedComponent(Component component, Component connectedComponent, Set<Component> visited) {
		if (connectedComponent instanceof Pump) {
			handlePumpComponent(component, (Pump) connectedComponent, visited);
		} else {
			// Continue traversing for other components
			startWaterSupplyDFS(connectedComponent, visited);
		}
	}

	private void handlePumpComponent(Component component, Pump connectedPump, Set<Component> visited) {
		visited.add(connectedPump);
		if (connectedPump.isBroken()) {
			connectedPump.fillReservoir();
			return;
		}
		if (connectedPump.getConnectedComponents().size() == 1) {
			return;
		}
		// Check if the current component is a pipe
		if (component instanceof Pipe) {
			Pipe currentPipe = (Pipe) component;
			// Check if the current pipe is neither the incoming nor outgoing pipe
			if (!currentPipe.equals(connectedPump.getIncomingPipe()) && !currentPipe.equals(connectedPump.getOutgoingPipe())) {
				return; // Return if it's neither incoming nor outgoing
			}
		}
		Pipe incomingPipe = connectedPump.getIncomingPipe();
		Pipe outgoingPipe = connectedPump.getOutgoingPipe();

		if (incomingPipe != null && !visited.contains(incomingPipe)) {
			startWaterSupplyDFS(incomingPipe, visited);
		}
		if (outgoingPipe != null && !visited.contains(outgoingPipe)) {
			startWaterSupplyDFS(outgoingPipe, visited);
		}

		// Special case: If both component and connectedComponent are pumps and
		// component has only one pipe
		if ((connectedPump.getIncomingPipe() != null && connectedPump.getOutgoingPipe() == null) ||
				(connectedPump.getIncomingPipe() == null && connectedPump.getOutgoingPipe() != null) && (!visited.contains(connectedPump))) {
			startWaterSupplyDFS(connectedPump, visited);
		}
	}

	/**
	 * a method that is invoked when a pipe is connected to a spring, which calls the 
	 * method startWaterSupply();
	 */
	public void onPipeConnected(){
		this.startWaterSupply();
	}

	public boolean isWaterFlowing() {
		return isWaterFlowing;
	}
}
