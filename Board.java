/**
 * 
 * @author Amy Yu
 *
 */
public class Board
{
	private static final int BOARD_SIZE = 11; // constant for number of columns
	private Column[] columns = new Column[BOARD_SIZE]; // array of columns
	
	public Board()
	{
		createColumns(); // creates the columns for the board
	}
	
	public void createColumns()
	{
		for (int i = 0; i < BOARD_SIZE; i++)
		{
			columns[i] = new Column(i + 2); // creates each column
		}
	}
	
	public Column[] getColumns()
	{
		return columns;
	}
	
	public int getBoardWidth()
	{
		return BOARD_SIZE;
	}
}
