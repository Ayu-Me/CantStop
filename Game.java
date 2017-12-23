/**
 * 
 * @author Amy Yu
 *
 */
public class Game
{
	private static final int NUM_OF_DICE = 4; // constant for number of dice in game
	private static final int NUM_OF_COMBOS = 3;
	private Board board; // game board
	private boolean isFinished; // checks game state
	private Die[] dice = new Die[NUM_OF_DICE]; // array of dice
	private Player[] players;
	private int numberOfOpponents;
	private int[][] dicePairs = new int[NUM_OF_COMBOS][NUM_OF_DICE];
	private int[][] sums = new int[NUM_OF_COMBOS][NUM_OF_DICE/2];
	private boolean[] isSplit = new boolean[NUM_OF_COMBOS];
	private int turnCount;
	private int[] tempMarkers;
	private int tempMarkersUsed;
	private int winner;
		
	public Game(int numberOfOpponents)
	{
		this.numberOfOpponents = numberOfOpponents;
		board = new Board(); // create board
		for (int i = 0; i < NUM_OF_DICE; i++)
		{
			dice[i] = new Die(); // create dice
		}
		players = new Player[numberOfOpponents + 1];
		for (int i = 0; i <= numberOfOpponents; i++)
		{
			players[i] = new Player(board, i);
		}
		tempMarkers = new int[board.getBoardWidth()];
	}
	
	public void startGame()
	{
		rollDice();
	}
	
	public void rollDice()
	{
		resetSums();
		for (int i = 0; i < isSplit.length; i++)
		{
			isSplit[i] = false;
		}
		for (Die die : dice)
		{
			die.roll();
		}
		for (int i = 1; i < NUM_OF_DICE; i++)
		{
			int[] pair = new int[2];
			int firstDie = dice[0].getDiceValue();
			int secondDie = dice[i].getDiceValue();
			int sum1 = firstDie + secondDie;
			dicePairs[i - 1][0] = firstDie;
			dicePairs[i - 1][1] = secondDie;
			sums[i - 1][0] = sum1;
			pair[0] = sum1;
			int sum2 = 0;
			int diceNum = 2;
			for (int j = 1; j < NUM_OF_DICE; j++)
			{
				if (j != i)
				{
					int nextDie = dice[j].getDiceValue();
					sum2 += nextDie;
					dicePairs[i - 1][diceNum] = nextDie;
					diceNum++;
				}
			}
			sums[i - 1][1] = sum2;
			pair[1] = sum2;
			boolean[] validity = moveIsValid(pair, i - 1);
			for (int j = 0; j < pair.length; j++)
			{
				if (!validity[j])
				{
					sums[i - 1][j] = 0;
				}
			}
			
		}
	}
	
	public void resetSums()
	{
		for (int[] pair : sums)
		{
			for (int i = 0; i < pair.length; i++)
			{
				pair[i] = 0;
			}
		}
	}
	
	public void incrementTurn()
	{
		if (turnCount == numberOfOpponents)
			turnCount = 0;
		else
			turnCount++;
	}
	
	public void updateTempMarkers(int[] pairs)
	{
		for (int sum : pairs)
		{
			if (sum >= 2)
			{
				tempMarkers[sum - 2]++;
			}
		}
	}
	
	public void resetTempMarkers()
	{
		for (int i = 0; i < tempMarkers.length; i++)
			tempMarkers[i] = 0;
		tempMarkersUsed = 0;
	}
	
	public void updateMarkers()
	{
		players[turnCount].moveMarkers(tempMarkers);
	}
	
	public boolean[] moveIsValid(int[] pair, int pairNumber)
	{
		boolean[] isValid = new boolean[2];
		for (int i = 0; i < isValid.length; i++)
		{
			isValid[i] = true;
		}
		boolean allOffBoard = true;
		for (int i = 0; i < pair.length; i++)
		{
			int sum = pair[i];
			int col = sum - 2;
			int currentPlayerLevel = getPlayers()[getTurn()].getMarkers()[col].getLevelNumber();
			int currentTempLevel = tempMarkers[col];
			int potentialLevel = currentPlayerLevel + currentTempLevel;
			int topOfColumn = board.getColumns()[col].getMaxLevel();
			if (potentialLevel + 1 > topOfColumn)
			{
				isValid[i] = false;
			}
			if (board.getColumns()[col].isClaimed())
			{
				isValid[i] = false;
			}
			if (tempMarkersUsed == 3 && tempMarkers[col] == 0)
			{
				isValid[i] = false;
			}
			if (tempMarkers[col] != 0)
			{
				allOffBoard = false;
			}
		}
		if (tempMarkersUsed == 2 && allOffBoard)
		{
			isSplit[pairNumber] = true;
			
		}
		return isValid;
	}
	
	public void useTempMarker()
	{
		tempMarkersUsed++;
	}
	
	public void checkWinState()
	{
		int columnCount = 0;
		for (int i = 0; i < board.getBoardWidth(); i++)
		{
			int markerPosition = players[turnCount].getMarkers()[i].getLevelNumber();
			int topOfColumn = board.getColumns()[i].getMaxLevel();
			if (markerPosition >= topOfColumn)
			{
				columnCount++;
				board.getColumns()[i].claimColumn();
			}
		}
		if (columnCount >= 3)
		{
			isFinished = true;
			winner = turnCount;
		}
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public Die[] getDice()
	{
		return dice;
	}
	
	public int[][] getDicePairs()
	{
		return dicePairs;
	}
	
	public int[][] getSums()
	{
		return sums;
	}
	
	public boolean[] getIsSplit()
	{
		return isSplit;
	}
	
	public Player[] getPlayers()
	{
		return players;
	}
	
	public int[] getTempMarkers()
	{
		return tempMarkers;
	}
	
	public int getTurn()
	{
		return turnCount;
	}
	
	public String getWinner()
	{
		return players[winner].getPlayerName();
	}
	
	public boolean isFinished()
	{
		return isFinished;
	}
}
