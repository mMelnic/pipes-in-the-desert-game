package exceptions;

/**
 * PipeMultipleComponentsConnectedException is thrown when multiple components are connected to a pipe.
 */
public class PipeMultipleComponentsConnectedException extends Exception {
    /**
     * Constructs a new PipeMultipleComponentsConnectedException with the specified detail message.
     * @param message the detail message
     */
    public PipeMultipleComponentsConnectedException(String string){
        super(string);
    }
}
