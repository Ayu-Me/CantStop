/**
 * 
 * @author Amy Yu
 *
 */
public class Column
{
	private static final int MAX_LEVEL = 13; // constant for maximum column level
	private static final int MID_COL = 7; // constant for middle column number
	private int columnNum; // the column number
	private int maxLevel; // the maximum level for this column
	private Level[] columnLevels; // array of levels
	private boolean isClaimed;
	
	public Column(int columnNum)
	{
		this.columnNum = columnNum; // sets column number with constructor
		maxLevel = MAX_LEVEL - 2 * Math.abs(columnNum - MID_COL); // calculates max level for this column
		this.columnLevels = new Level[maxLevel]; // creates array of levels
		createLevels(); // creates levels
	}
	
	public void createLevels()
	{
		for (int i = 0; i < maxLevel; i++)
		{
			columnLevels[i] = new Level(i);
		}
	}
	
	public void claimColumn()
	{
		isClaimed = true;
	}
	
	public int getBoardHeight()
	{
		return MAX_LEVEL;
	}
	
	public int getMaxLevel()
	{
		return maxLevel;
	}
	
	public int getColumnNum()
	{
		return columnNum;
	}
	
	public Level[] getColumnLevels()
	{
		return columnLevels;
	}
	
	public boolean isClaimed()
	{
		return isClaimed;
	}
}
