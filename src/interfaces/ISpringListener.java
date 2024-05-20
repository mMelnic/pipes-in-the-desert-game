package interfaces;

/**
 * The ISpringListener interface provides a contract for classes that need to listen for events related to springs.
 */
public interface ISpringListener {
    /**
     * This method is called when a spring is connected to a pipe.
     */
    public void OnSpringConnected();
}
