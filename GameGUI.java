import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * 
 * @author Amy Yu
 *
 */

public class GameGUI extends Application
{
	private static final int WIDTH = 800; // constant for window width
	private static final int HEIGHT = 600; // constant for window height
	private static final int NUM_OF_TEMP = 3; // constant for number of temporary markers
	private static final int MARKER_SIZE = 8; //constant for marker size
	private static final double MARKER_OPACITY = 0.75;
	private Game game;
	private Board board;
	private int numOfOpponents;
	private Stage window;
	private GridPane boardPanel;
	private VBox gameControls;
	private Circle[] tempMarkers = new Circle[NUM_OF_TEMP];
	private boolean[] tempOnBoard = new boolean[NUM_OF_TEMP];
	private int tempMarkersUsed;
	private boolean loseProgress;
	private Circle[][] playerMarkers;
	private Color[] colorArray = {
			Color.rgb(178, 34, 34), Color.rgb(255, 192, 0),
			Color.rgb(88, 161, 188), Color.rgb(18, 51, 175)};
	private Polygon octagon = new Polygon();
	
	@Override // override the start method in the Application class
	public void start(Stage primaryStage)
	{
		window = primaryStage;
		createOctagon();
		StackPane menu = initializeMenu(); // creates the title screen
		createTempMarkers();
		Scene scene = new Scene(menu, WIDTH, HEIGHT); // creates a scene with a title screen
		primaryStage.setTitle("Can't Stop"); // sets the stage title
		primaryStage.setScene(scene); // places the scene in the stage
		primaryStage.show(); // displays the stage
	}
	
	public StackPane initializeMenu()
	{
		StackPane titleScreen = new StackPane();
		VBox menu = new VBox(30); // creates a vertical box to hold title screen contents
		Label title = new Label("Can't Stop"); // creates title
		title.setTextFill(Color.WHITE);
		title.setFont(Font.font("Arial Black", FontWeight.BOLD, 30));
		
		Button start = new Button("Start"); // creates start button
		StartGameHandlerClass startGameHandler = new StartGameHandlerClass();
		start.setOnAction(startGameHandler);
		start.setFont(Font.font("Arial Black", FontWeight.BOLD, 15));
		
		Button about = new Button("About"); // creates about button
		AboutHandlerClass aboutHandler = new AboutHandlerClass();
		about.setOnAction(aboutHandler);
		about.setFont(Font.font("Arial Black", FontWeight.BOLD, 15));
		
		Button quit = new Button("Quit"); // creates quit button
		QuitGameHandlerClass quitGameHandler = new QuitGameHandlerClass();
		quit.setOnAction(quitGameHandler);
		quit.setFont(Font.font("Arial Black", FontWeight.BOLD, 15));
		
		menu.getChildren().addAll(title, start, about, quit); // adds all buttons
		menu.setAlignment(Pos.CENTER); // center contents
		
		titleScreen.getChildren().addAll(octagon, menu);
		titleScreen.setStyle("-fx-background-color: #efefef;");
		return titleScreen;
	}
	
	public VBox initializeAboutScreen()
	{
		VBox aboutScreen = new VBox(3);
		Label title = new Label("Can't Stop Rules");
		title.setFont(new Font(30));
		Text rules = new Text();
		rules.setText(
				"In this Sid Sackson classic, players must press their luck with dice"
				+ " and choose combinations tactically to close out three columns."
				+ "\n\nThe board has one column for each possible total of two six-sided dice,"
				+ " but the number of spaces in each column varies:"
				+ " the more probable a total, the more spaces in that column"
				+ " and the more rolls it takes to complete."
				+ "\n\nOn their turn, a player rolls four dice and arranges them in duos:"
				+ " 1 4 5 6 can become 1+4 and 5+6 for 5 & 11,"
				+ " 1+5 and 4+6 for 6 & 10, or 1+6 and 4+5 for 7 & 9."
				+ "\n\nThe player places or advances progress markers in the open column(s)"
				+ " associated with their chosen totals,"
				+ " then chooses whether to roll again or end their turn"
				+ " and replace the progress markers with markers of their color."
				+ "\n\nA player can only advance three different columns in a turn"
				+ " and cannot advance a column which any player has closed out"
				+ " by reaching the end space;"
				+ " if a roll doesn’t result in any legal plays,"
				+ " the turn ends with that turn’s progress lost.\n");
		rules.setWrappingWidth(WIDTH * 0.75);
		Button returnToMenu = new Button("Return to Menu");
		ShowMenuHandlerClass showMenuHandler = new ShowMenuHandlerClass();
		returnToMenu.setOnAction(showMenuHandler);
		aboutScreen.getChildren().addAll(title, rules, returnToMenu);
		aboutScreen.setAlignment(Pos.CENTER);
		aboutScreen.setStyle("-fx-background-color: #efefef;");
		return aboutScreen;
	}
	
	public StackPane initializeGameSetupScreen()
	{
		StackPane gameSetupScreen = new StackPane();
		VBox gameSetup = new VBox(20);
		Label title = new Label("Game Setup");
		title.setTextFill(Color.WHITE);
		title.setFont(Font.font("Arial Black", FontWeight.NORMAL, 30));
				
		Button start = new Button("Start Game");
		start.setDisable(true);
		start.setFont(Font.font("Arial Black", FontWeight.NORMAL, 15));
		
		Label oppCountLabel = new Label("Choose Number of Opponents");
		oppCountLabel.setTextFill(Color.WHITE);
		oppCountLabel.setFont(Font.font("Arial Black", FontWeight.NORMAL, 15));
		ComboBox<Integer> numOfOpp = new ComboBox<Integer>();
		numOfOpp.getItems().addAll(1,2,3);
		numOfOpp.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e)
			{
				numOfOpponents = numOfOpp.getSelectionModel().getSelectedItem();
				start.setDisable(false);
			}
		});
		numOfOpp.setStyle("-fx-font: 15px \"Arial Black\";");
		
		StartGameplayHandlerClass startGameplayHandler = new StartGameplayHandlerClass();
		start.setOnAction(startGameplayHandler);
		     
		gameSetup.getChildren().addAll(title, oppCountLabel, numOfOpp, start);
		gameSetup.setAlignment(Pos.CENTER);
		gameSetupScreen.getChildren().addAll(octagon, gameSetup);
		gameSetupScreen.setStyle("-fx-background-color: #efefef;");
		return gameSetupScreen;
	}
	
	public BorderPane initializeGameplayScreen()
	{
		BorderPane gameplayScreen = new BorderPane();
		
		GridPane boardPanel = initializeBoard(); // creates the board GUI
		gameplayScreen.setCenter(boardPanel);
		
		VBox dicePanel = initializeDice(); // creates the dice GUI
		gameplayScreen.setRight(dicePanel);
		
		HBox controlPanel = initializeControls(); // creates the control panel
		gameplayScreen.setBottom(controlPanel);
		
		return gameplayScreen;
	}
	
	public GridPane initializeBoard()
	{
		boardPanel = new GridPane(); // creates the board GUI
		boardPanel.setHgap(20);
		boardPanel.setVgap(10);
		int boardWidth = board.getBoardWidth();
		Column[] columns = board.getColumns();
		int boardHeight = columns[0].getBoardHeight();
		for (int c = 0; c < boardWidth; c++)
		{
			int colMaxLvl = columns[c].getMaxLevel();
			int spaceWidth = 25;
			for (int r = 0; r < colMaxLvl; r++)
			{
				HBox textBox = new HBox();
				Text label = new Text("" + (c + 2));
				label.setTextAlignment(TextAlignment.CENTER);
				label.setWrappingWidth(spaceWidth);
				textBox.getChildren().add(label);
				textBox.setStyle("-fx-border-color: black;");
				textBox.setAlignment(Pos.CENTER);
				boardPanel.add(textBox, c, boardHeight - r - (boardHeight - colMaxLvl)/2);
			}
		}
		for (int i = 0; i < tempMarkers.length; i++)
		{
			boardPanel.add(tempMarkers[i], 0, i + 1);
		}
		boardPanel.setAlignment(Pos.CENTER);
		boardPanel.setStyle("-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-background-color: #efefef;" +
                "-fx-border-color: black;");
		return boardPanel;
	}
	
	public VBox initializeDice()
	{
		gameControls = new VBox(5); // creates the dice GUI
		updateDice();
		gameControls.setAlignment(Pos.CENTER);
		gameControls.setPrefWidth(200);
		gameControls.setStyle("-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-background-color: #efefef;" +
                "-fx-border-color: black;");
		return gameControls;
	}
	
	public void updateDice()
	{
		gameControls.getChildren().clear();
		loseProgress = false;
		
		String currentPlayerName = game.getPlayers()[game.getTurn()].getPlayerName();
		Label currentPlayer = new Label("It is " + currentPlayerName + "'s turn");
		currentPlayer.setFont(Font.font("Arial Black", FontWeight.NORMAL, 15));
		gameControls.getChildren().add(currentPlayer);
		
		GridPane dicePanel = new GridPane();
		dicePanel.setVgap(5);
		dicePanel.setHgap(5);
		dicePanel.setAlignment(Pos.CENTER);
		dicePanel.setStyle("-fx-padding: 10;");
		int col = 0;
		int row = 0;
		int diceCount = 1;
		Die[] dice = game.getDice();
		for (Die die : dice)
		{
			Label label = new Label("" + die.getDiceValue());
			label.setStyle("-fx-label-padding: 8 13 8 13;"
					+ "-fx-border-radius: 10;"
					+ "-fx-border-color: black;");
		    CornerRadii corn = new CornerRadii(10);
		    BackgroundFill bgFill = new BackgroundFill(Color.FIREBRICK, corn, Insets.EMPTY);
		    Background background = new Background(bgFill);
		    label.setBackground(background);
			label.setTextFill(Color.WHITE);
			label.setFont(Font.font("Arial Black", FontWeight.NORMAL, 15));
			dicePanel.add(label, col, row);
			if (diceCount % 2 == 0)
			{
				row++;
			}
			if (col == 1)
			{
				col = 0;
			}
			else
			{
				col++;
			}
			diceCount++;
		}
		gameControls.getChildren().add(dicePanel);
		int[][] sums = game.getSums();
		boolean[] isSplit = game.getIsSplit();
		int pairNumber = 0;
		int totalSum = 0;
		int[][] dicePairs = game.getDicePairs();
		for (int[] pairs : sums)
		{
			int pairSum = 0;
			String firstCombo = dicePairs[pairNumber][0] + " + " + dicePairs[pairNumber][1];
			String secondCombo = dicePairs[pairNumber][2] + " + " + dicePairs[pairNumber][3];
			Label combo = new Label(firstCombo + " and " + secondCombo);
			for (int sum : pairs)
			{
				pairSum += sum;
			}
			totalSum += pairSum;
			if (pairSum == 0)
			{
				Button invalid = new Button("Invalid move");
				invalid.setDisable(true);
				gameControls.getChildren().addAll(combo, invalid);
			}
			else if (isSplit[pairNumber])
			{
				if (pairs[0] != 0)
				{
					Button sum1 = new Button("Progress on " + pairs[0]);
					int[] pair1 = {pairs[0], 0};
					DiceSelectionHandlerClass diceSelectionHandler1 = 
							new DiceSelectionHandlerClass(pair1);
					sum1.setOnAction(diceSelectionHandler1);
					gameControls.getChildren().addAll(new Label(firstCombo), sum1);
				}
				if (pairs[1] != 0)
				{
					Button sum2 = new Button("Progress on " + pairs[1]);
					int[] pair2 = {pairs[1], 0};
					DiceSelectionHandlerClass diceSelectionHandler2 = 
							new DiceSelectionHandlerClass(pair2);
					sum2.setOnAction(diceSelectionHandler2);
					gameControls.getChildren().addAll(new Label(secondCombo), sum2);
				}
			}
			else
			{
				if (pairs[0] == 0)
				{
					Button pair = new Button("Progress on " + pairs[1]);
					DiceSelectionHandlerClass diceSelectionHandler = 
							new DiceSelectionHandlerClass(pairs);
					pair.setOnAction(diceSelectionHandler);
					gameControls.getChildren().addAll(combo, pair);
				}
				else if (pairs[1] == 0)
				{
					Button pair = new Button("Progress on " + pairs[0]);
					DiceSelectionHandlerClass diceSelectionHandler = 
							new DiceSelectionHandlerClass(pairs);
					pair.setOnAction(diceSelectionHandler);
					gameControls.getChildren().addAll(combo, pair);
				}
				else
				{
					Button pair = new Button("Progress on " + pairs[0] + " and " + pairs[1]);
					DiceSelectionHandlerClass diceSelectionHandler = 
							new DiceSelectionHandlerClass(pairs);
					pair.setOnAction(diceSelectionHandler);
					gameControls.getChildren().addAll(combo, pair);
				}
			}
			pairNumber++;
		}
		if (totalSum == 0)
		{
			loseProgress = true;
			Button endTurn = new Button("No valid moves");
			EndTurnHandlerClass endTurnHander = new EndTurnHandlerClass();
			endTurn.setOnAction(endTurnHander);
			gameControls.getChildren().add(endTurn);
		}
	}
	
	public void hideDice()
	{
		gameControls.getChildren().clear();
		
		Button stop = new Button("Stop");
		stop.setFont(Font.font("Arial Black", FontWeight.NORMAL, 15));
		EndTurnHandlerClass endTurnHandler = new EndTurnHandlerClass();
		stop.setOnAction(endTurnHandler);
		
		Button go = new Button("Continue");
		go.setFont(Font.font("Arial Black", FontWeight.NORMAL, 15));
		ContinueTurnHandlerClass continueTurnHandler = new ContinueTurnHandlerClass();
		go.setOnAction(continueTurnHandler);
		
		gameControls.getChildren().addAll(stop, go);
	}
	
	public void createTempMarkers()
	{
		for (int i = 0; i < tempMarkers.length; i++)
		{
			tempMarkers[i] = new Circle(MARKER_SIZE);
		}
	}
	
	public void placeTempMarker(int col)
	{
		int currentLevel = game.getPlayers()[game.getTurn()].getMarkers()[col].getLevelNumber();
		boolean isOnBoard = false;
		for (int i = 0; i < NUM_OF_TEMP; i++)
		{
			if (tempOnBoard[i] == true && GridPane.getColumnIndex(tempMarkers[i]) == col)
			{
				isOnBoard = true;
				int nextRow = GridPane.getRowIndex(tempMarkers[i]) - 1;
				GridPane.setRowIndex(tempMarkers[i], nextRow);
			}
		}
		if (!isOnBoard)
		{
			Column[] columns = board.getColumns();
			int boardHeight = columns[0].getBoardHeight();
			int colMaxLvl = columns[col].getMaxLevel();
			GridPane.setColumnIndex(tempMarkers[tempMarkersUsed], col);
			int row = boardHeight - (boardHeight - colMaxLvl)/2 - currentLevel;
			GridPane.setRowIndex(tempMarkers[tempMarkersUsed], row);
			tempOnBoard[tempMarkersUsed] = true;
			tempMarkersUsed++;
			game.useTempMarker();
		}
	}
	
	public void resetTempMarkers()
	{
		for (int i = 0; i < NUM_OF_TEMP; i++)
		{
			GridPane.setColumnIndex(tempMarkers[i], 0);
			GridPane.setRowIndex(tempMarkers[i], i + 1);
			tempOnBoard[i] = false;
		}
		tempMarkersUsed = 0;
		
	}
	
	public void createPlayerMarkers()
	{
		playerMarkers = new Circle[numOfOpponents + 1][board.getBoardWidth()];
	}
	
	public void movePlayerMarker(int col, int count)
	{
		boolean isOnBoard = false;
		if (game.getPlayers()[game.getTurn()].getMarkers()[col].getLevelNumber() > 0)
		{
			isOnBoard = true;
			int nextRow = GridPane.getRowIndex(playerMarkers[game.getTurn()][col]) - count;
			GridPane.setRowIndex(playerMarkers[game.getTurn()][col], nextRow);
		}
		if (!isOnBoard)
		{
			Column[] columns = board.getColumns();
			int boardHeight = columns[0].getBoardHeight();
			int colMaxLvl = columns[col].getMaxLevel();
			int row = boardHeight - (boardHeight - colMaxLvl)/2 - count + 1;
			Circle playerMarker = new Circle(MARKER_SIZE);
			playerMarker.setFill(colorArray[game.getTurn()]);
			playerMarker.setOpacity(MARKER_OPACITY);
			boardPanel.add(playerMarker, col, row);
			playerMarkers[game.getTurn()][col] = playerMarker;
		}
	}
	
	public HBox initializeControls()
	{
		HBox controlPanel = new HBox(); // creates the control panel
		Button quit = new Button("Quit Game");
		QuitGameHandlerClass quitGameHandler = new QuitGameHandlerClass();
		quit.setOnAction(quitGameHandler);
		controlPanel.getChildren().addAll(quit);
		controlPanel.setAlignment(Pos.CENTER);
		controlPanel.setStyle("-fx-background-color: #efefef; -fx-padding: 0 0 5 0;");
		return controlPanel;
	}
	
	public StackPane initializeResultsScreen()
	{
		StackPane resultsScreen = new StackPane();
		VBox resultsMessage = new VBox(20);
		Label results = new Label(game.getWinner() + " is the winner!");
		results.setTextFill(Color.WHITE);
		results.setFont(Font.font("Arial Black", FontWeight.BOLD, 30));
		
		Label message = new Label("Thanks for playing!");
		message.setTextFill(Color.WHITE);
		message.setFont(Font.font("Arial Black", FontWeight.NORMAL, 15));
		
		Button returnToMenu = new Button("Return to Menu");
		ShowMenuHandlerClass showMenuHandler = new ShowMenuHandlerClass();
		returnToMenu.setOnAction(showMenuHandler);
		returnToMenu.setFont(Font.font("Arial Black", FontWeight.BOLD, 15));
		
		Button quit = new Button("Quit"); // creates quit button
		QuitGameHandlerClass quitGameHandler = new QuitGameHandlerClass();
		quit.setOnAction(quitGameHandler);
		quit.setFont(Font.font("Arial Black", FontWeight.BOLD, 15));
		
		resultsMessage.getChildren().addAll(results, message, returnToMenu, quit); // adds all buttons
		resultsMessage.setAlignment(Pos.CENTER); // center contents
		
		resultsScreen.getChildren().addAll(octagon, resultsMessage);
		resultsScreen.setStyle("-fx-background-color: #efefef;");
		return resultsScreen;
	}
	
	public void createOctagon()
	{
		double x = 200;
		double y = 2 * x;
		double z = Math.sqrt(x*x/2);
		double a = y + z;
		double b = x + z;
		double c = x - z;
		octagon.getPoints().addAll(new Double[]
		{
			x, x,
			y, x,
			a, b,
			a, y + z,
			y, y + 2*z,
			x, y + 2*z,
			c, a,
			c, b
		});
		octagon.setFill(Color.FIREBRICK);
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	class ShowMenuHandlerClass implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e)
		{
			StackPane menu = initializeMenu();
			Scene scene = new Scene(menu, WIDTH, HEIGHT);
			window.setScene(scene);
		}
	}
	
	class AboutHandlerClass implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			VBox aboutScreen = initializeAboutScreen();
			Scene scene = new Scene(aboutScreen, WIDTH, HEIGHT);
			window.setScene(scene);
		}
	}
	
	class StartGameHandlerClass implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e)
		{
			StackPane gameSetupScreen = initializeGameSetupScreen();
			Scene scene = new Scene(gameSetupScreen, WIDTH, HEIGHT);
			window.setScene(scene);
		}
	}
	
	class StartGameplayHandlerClass implements EventHandler<ActionEvent> 
	{	
		@Override
		public void handle(ActionEvent e)
		{
			game = new Game(numOfOpponents);
			game.startGame();
			board = game.getBoard();
			createPlayerMarkers();
			BorderPane gameplayScreen = initializeGameplayScreen();
			Scene scene = new Scene(gameplayScreen, WIDTH, HEIGHT);
			window.setScene(scene);
		}
	}
	
	class QuitGameHandlerClass implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e)
		{
			window.close();
		}
	}
	
	class DiceSelectionHandlerClass implements EventHandler<ActionEvent> 
	{
		int[] pairs;
		DiceSelectionHandlerClass(int[] pairs)
		{
			this.pairs = pairs;
		}
		@Override
		public void handle(ActionEvent e)
		{
			for (int sum : pairs)
			{
				if (sum >= 2)
				{
					placeTempMarker(sum - 2);
				}
			}
			game.updateTempMarkers(pairs);
			hideDice();
		}
	}
	
	class EndTurnHandlerClass implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			if (!loseProgress)
			{
				int[] tempMarkers = game.getTempMarkers();
				for (int i = 0; i < tempMarkers.length; i++)
				{
					int count = tempMarkers[i];
					if (count > 0)
					{
						movePlayerMarker(i, count);
					}
				}
				game.updateMarkers();
			}
			resetTempMarkers();
			game.resetTempMarkers();
			game.checkWinState();
			if (game.isFinished())
			{
				// change to game results screen
				StackPane resultsScreen = initializeResultsScreen();
				Scene scene = new Scene(resultsScreen, WIDTH, HEIGHT);
				window.setScene(scene);
			}
			else
			{
				game.incrementTurn();
				game.rollDice();
				updateDice();
			}
		}
	}
	
	class ContinueTurnHandlerClass implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			game.rollDice();
			updateDice();
		}
	}
}