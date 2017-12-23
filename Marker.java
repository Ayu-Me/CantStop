/**
 * 
 * @author Amy Yu
 *
 */
public class Marker
{
	private int columnNumber;
	private int levelNumber;
	
	public Marker(int columnNumber, int levelNumber)
	{
		this.columnNumber = columnNumber;
		this.levelNumber = levelNumber;
	}
	
	public void increaseLevel(int increase)
	{
		levelNumber += increase;
	}
	
	public int getColumnNumber()
	{
		return columnNumber;
	}
	
	public int getLevelNumber()
	{
		return levelNumber;
	}
}
