/**
 * 
 * @author Amy
 *
 */
public class Die
{
	private int value; // value obtained from rolling the die
	
	public void roll()
	{
		value = (int)(Math.random() * 6) + 1; // generates random number from 1-6 inclusive
	}
	
	public int getDiceValue()
	{
		return value;
	}
}
