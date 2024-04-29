package exceptions;
/**
 * a class SpringMultipleComponensConnectedException represents an exception that is thrown when a plumber
 * tried to connect a component to a spring when the spring is already connected to a component
 */
public class SpringMultipleComponensConnectedException extends Exception {
    /**
     * a constructor of the class
     * @param string a message that is to be printed when the exception is thrown.
     */
    public SpringMultipleComponensConnectedException(String string){
        super(string);
    }
}
