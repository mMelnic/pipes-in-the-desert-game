package exceptions;
/**
 * a class CisternMultipleComponentsConnectedException represents an exception that is thrown when
 * a plumber tried to connect a componentto a cistern when the cistern is already connected to a 
 * component
 * @author asus
 */
public class CisternMultipleComponentsConnectedException extends Exception {
    /**
     * constructor of the class
     * @param string a message that is to be printed when the exception is thrown.
     */
    public CisternMultipleComponentsConnectedException(String string){
        super(string);
    }
}
