package components;
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
	public Spring() 
	{
		super();
		isWaterFlowing = false;
	}
	/**
	 * a method that starts water supply from a spring which sets an attribute isWaterFlowing to true.
	 */
	public void startWaterSupply() 
	{
		isWaterFlowing = true;
	}
	/**
	 * a method that is invoked when a pipe is connected to a spring, which calls the 
	 * method startWaterSupply();
	 */
	public void onPipeConnected(){
		this.startWaterSupply();
	}
}
