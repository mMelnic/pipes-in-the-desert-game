package interfaces;

/**
 * The {@code ILeakage} interface represents objects that have the ability to start and stop leaking.
 * Implementing classes should provide functionality to start and stop leakage.
 */
public interface ILeakage {
    
    /**
     * Starts the leakage.
     * Implementing classes should define the behavior of starting the leakage.
     */
    void startLeaking();
    /**
     * Stops the leakage.
     * Implementing classes should define the behavior of stopping the leakage.
     */
    long stopLeaking();
    
}
