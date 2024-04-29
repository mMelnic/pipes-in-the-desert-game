package exceptions;
/**
 * a class CellOccupiedException represents an exception that is thrown when a plumber tried to install
 * a component on an occupied cell
 */
public class CellOccupiedException extends Exception {
    /**
     * constructor of the class
     * @param string a message that is to be printed when the exception is thrown.
     */
    public CellOccupiedException(String string){
        super(string);
    }
}
