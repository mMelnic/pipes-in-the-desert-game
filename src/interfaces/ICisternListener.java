package interfaces;
/**
 * The ICisternListener interface provides a contract for classes that need to listen for events related to cisterns.
 */
public interface ICisternListener {
    /**
     * This method is called when a cistern is checked to determine if it is full.
     */
    public void onCisternFullCheck();
}
