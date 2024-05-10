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
    // Initialize a set to keep track of visited components
    Set<Component> visited = new HashSet<>();
    // Call the recursive method to start water supply
    startWaterSupplyDFS(this, visited);
}

// Recursive method to start water supply using depth-first search (DFS)
private void startWaterSupplyDFS(Component component, Set<Component> visited) {
    // Mark the current component as visited
    visited.add(component);

    // If the current component is a pipe, set its isWaterFlowing attribute to true
    if (component instanceof Pipe) {
        ((Pipe) component).setWaterFlowing(true);
    }

    // Traverse the connected components recursively
    for (Component connectedComponent : component.getConnectedComponents().values()) {
        // Skip if the connected component has already been visited
        if (!visited.contains(connectedComponent) && (!(connectedComponent instanceof Pump) || (connectedComponent instanceof Pump))) { // TODO bred
                startWaterSupplyDFS(connectedComponent, visited);
            }
    }
}

	/**
	 * a method that is invoked when a pipe is connected to a spring, which calls the 
	 * method startWaterSupply();
	 */
	public void onPipeConnected(){
		this.startWaterSupply();
	}
}
