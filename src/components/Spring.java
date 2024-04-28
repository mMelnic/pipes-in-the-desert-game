package components;

public class Spring extends Component 
{
	private boolean isWaterFlowing;

	public Spring() 
	{
		super();
		isWaterFlowing = false;
	}

	public void startWaterSupply(boolean isPipeConnected) 
	{
		isWaterFlowing = true;
	}
}
