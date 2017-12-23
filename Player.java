/**
 * 
 * @author Amy Yu
 *
 */
public class Player
{
	private Marker[] markers;
	private String playerName;
	
	public Player(Board board, int playerNumber)
	{
		markers = new Marker[board.getBoardWidth()];
		for (int i = 0; i < markers.length; i++)
		{
			markers[i] = new Marker(i, 0);
		}
		playerName = "Player " + (playerNumber + 1);
	}
	
	public void moveMarkers(int[] tempMarkers)
	{
		for (int i = 0; i < tempMarkers.length; i++)
		{
			markers[i].increaseLevel(tempMarkers[i]);
		}
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public Marker[] getMarkers()
	{
		return markers;
	}
}
