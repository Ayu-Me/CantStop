/**
 * 
 * @author Amy Yu
 *
 */
public class Level
{
	private int number;
	private Marker[] markers;
	
	public Level(int number)
	{
		this.number = number;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public Marker[] getMarkers()
	{
		return markers;
	}
}
