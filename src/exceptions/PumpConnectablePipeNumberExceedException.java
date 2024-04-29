package exceptions;
/**
 * a class PumpConnectablePipeNumberExceedException represents an exception that is thrown when a plumber
 * tried to connect a component to a pump that is already connected to maximum number of components,
 * pumps max connectable number may differ.
 */
public class PumpConnectablePipeNumberExceedException extends Exception {
    /**
     * a constructor of a class
     * @param string a message that is to be printed when the exception is thrown.
     */
    public PumpConnectablePipeNumberExceedException(String string){
        super(string);
    }
}
