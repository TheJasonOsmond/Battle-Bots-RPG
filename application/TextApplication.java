package application;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.PlayerCharacter;
/**
 * When ran the text application class launches the game.
 * The class asks user for the game mode 
 * and lets the user create the player characters. 
 * Then calls the TurnTracker class to run the turns.
 * @author Jason Osmond
 */
public class TextApplication {
	
	//====== [METHODS] ======
	
	/**
	 * The start program starts the setup for the game.
	 * The user chooses the game mode, depending on the game mode
	 * the method createPlayerCharacter may be called. 
	 * An instance of TurnTracker is also created and 
	 * at the end of this method it calls the nextTurn() method from TurnTracker
	 */
	public void start() {
		TurnTracker turnTracker;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Welcome to the game!");
		System.out.println();
		
		int gameMode = -1;
		
		/* The while loop asks the user what game mode the game will run.
		 * This will loop until the game mode is set to 1,2, or 3. 
		 * If non-integer are inputed or integers no equal to 1,2 or 3 are inputed
		 * the user will be told their input was invalid and will need a new input.
		*/
		while (gameMode != 1 && gameMode != 2 && gameMode != 3) {		
			
			System.out.println("What game mode do you want to play? \n" + 
				"Player vs AI: 1 \n" +
				"Player vs Player: 2 \n" + 
				"AI vs AI: 3 \n");
			
			System.out.print("Type '1', '2' or '3' to choose your gamemode: ");
			
			try {				
				gameMode = scan.nextInt();
				
				if ((gameMode != 1 && gameMode != 2 && gameMode != 3))
					System.out.println("\n" + "INVALID INPUT! Game mode '" + gameMode + "' not recognized. Try again" + "\n");
			}
			
			catch (InputMismatchException ime){
				scan.nextLine(); // clears remaining  text in scanner, removal causes infinite loop
				
				System.out.println("\n" + "INVALID INPUT! User input was not an integer. Try again." + "\n");
			}
		}
		
		System.out.println();
		
		// For Player vs AI only one PlayerCharacter is made
		if (gameMode == 1) {
			PlayerCharacter characterOne = createPlayerCharacter();
			
			turnTracker = new TurnTracker(characterOne);
		}
		
		// For Player vs Player two PlayerCharacters are made
		else if (gameMode == 2) {
			System.out.println("====== [Player One Character Creation] ====== \n");
				PlayerCharacter characterOne = createPlayerCharacter();
			
			System.out.println("====== [Player Two Character Creation] ====== \n");
				PlayerCharacter characterTwo = createPlayerCharacter();
				
			turnTracker = new TurnTracker(characterOne,characterTwo);
		}
		
		// For AI vs AI no PlayerCharacters are made
		else {
			turnTracker = new TurnTracker();
		}
		
		// Runs the first turn
		turnTracker.nextTurn();
	}
	
	/**
	 * Prompts user for information regarding their character.
	 * User's have the option to choose the name and stats of the character.
	 * The user can manually input stats or use the recommended preset.
	 * @return a PlayerCharacter
	 */
	private PlayerCharacter createPlayerCharacter() {
		Scanner scan = new Scanner(System.in);
		PlayerCharacter playerCharacter = null;
		String finalizeCharacter = "no";
		
		// Prompt user for character name
		System.out.println("What is the name of your character? ");
			String playerCharacterName = scan.nextLine(); 
			
		while (finalizeCharacter.equals("yes") != true) {
			
			// Ask user if they want to select their stats
			System.out.println("Would you like to choose " + playerCharacterName + "'s stats? (yes/no)" );
				String chooseStatsSelection = scan.nextLine();
				chooseStatsSelection = chooseStatsSelection.toLowerCase();
			
			//If the user chose to input stats.
			if (chooseStatsSelection.equals("yes")) {
				int maxPoints = PlayerCharacter.DEFAULT_POINTS_AVAILABLE;
				int statValue = -1;
				
				System.out.println("\nYou have " + maxPoints + " points to use.");
				System.out.println("Available Stats: Attack /  Defense / Search / Toughness \n");
				
				// Creates an array of strings called stats
				String [] stats = {"Attack (+3/point)" , "Defense (+3/point)" , "Search (+1/point)" , "Toughness (+5% health/point)"};
				int [] playerStatsSelection = new int[stats.length];
				
				System.out.println("Enter the values for " + playerCharacterName + "'s stats:");
				
				// Goes through each element in stats and record the user's input for each stat
				for (int index = 0; index < stats.length; index++) {
					statValue = -1;
					
					// Loops user prompt until user's input is acceptable
					while  (statValue < 0 || statValue > maxPoints) {
						System.out.print(stats[index] + ": ");
						try {				
							statValue = scan.nextInt();
							
							if (statValue < 0 )
								System.out.println("\n" + "INVALID INPUT! User input was negative. Try again");
							
							else if (statValue > maxPoints)
								System.out.println("\n" + "INVALID INPUT! " + statValue + " is greater than remaining points." + "\n" +
										"Points remaining: " + maxPoints + "\n");
							
						}
						
						catch (InputMismatchException ime){
							scan.nextLine(); //clears the text in scanner, removal causes infinite loop
							System.out.println("\n" + "INVALID INPUT! User input was not an integer. Try again." + "\n");
						}
					}
					// Reduces the remaining points left and records the stat value in a integer array called playerStatsSelection.
					maxPoints -= statValue;
					playerStatsSelection[index] = statValue; 
				}
				
				// Constructs a new PlayerCharacter using entries in playerStatsSelection.
				playerCharacter = new PlayerCharacter(
						playerStatsSelection[0] * 3,
						playerStatsSelection[1] * 3,
						playerStatsSelection[2],
						playerStatsSelection[3]
						);
				
				playerCharacter.setName(playerCharacterName);
				
				// scan.nextInt leaves a "\n" left over so if i were to run scan.nextLine, it would scan only that "\n" and not the user input
				// (https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo)
				scan.nextLine();
			}
			
			// If the player chose no to input the character's stats, PlayerCharacter is made with default stats
			else {
				playerCharacter = new PlayerCharacter();
				playerCharacter.setName(playerCharacterName);
			}
			
			// displays stats into terminal, and asks the user if the stats are acceptable
			System.out.println("\n" + playerCharacter.displayStats());
			System.out.println("Are " + playerCharacter.getName() + "'s stats acceptable? (yes/no)");
			
			// If the user does not type "yes", the loop will restart
			finalizeCharacter = scan.nextLine().toLowerCase();
		}
		
		System.out.println();
		
		return playerCharacter;
	}
	
	/**
	 * This method launches the game.
	 * Creates a textApplication object and calls the start method.
	 * @param args
	 */
	public static void main(String[] args) {
		TextApplication game = new TextApplication();
		game.start();
	}
}

