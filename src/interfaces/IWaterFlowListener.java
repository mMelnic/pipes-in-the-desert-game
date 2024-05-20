package interfaces;

import components.Pipe;

/**
 * The IWaterFlowListener interface provides a contract for classes that need to listen for changes in water flow within pipes.
 */
public interface IWaterFlowListener {
    /**
     * This method is called when the water flow within a pipe changes.
     *
     * @param pipe The pipe whose water flow has changed.
     */
    void onWaterFlowChanged(Pipe pipe);
}
