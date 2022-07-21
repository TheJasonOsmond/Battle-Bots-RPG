package application;

import model.Character;
import model.EnemyCharacter;
import model.PlayerCharacter;
import model.RandomEnemy;

/**
 * The TurnTracker handles the turns for the text application.
 * It has methods to display the turns as a text 
 * and methods to check and handle character death.
 * Once the game is over this class also prints 
 * a report of the game into the console. 
 * @author Jason Osmond
 */
public class TurnTracker {
	
	//====== [INSTANCE VARIABLES] =======
	
	private Character characterOne;
	private Character characterTwo; 
	private static int turn;
	private int gameMode;
	
	//====== [CONSTRUCTORS] ======
	
	/**
	 * Player versus AI
	 * This constructor is for game mode 1, where the player fights an AI enemy
	 * Adds one PlayerCharacter and one EnemyCharacter object to the class
	 * @param playerCharacter
	 */
	public TurnTracker(PlayerCharacter playerCharacter) {
		setCharacterOne(playerCharacter);
		setGameMode(1);
		setTurn(0);
		characterTwo = new RandomEnemy();
	}
	
	/**
	 * Player versus Player
	 * This Constructor is for game mode 2, where the two players fight another
	 * Adds two PlayerCharacter objects to the class
	 * @param playerCharacterOne
	 * @param playerCharacterTwo
	 */
	public TurnTracker(PlayerCharacter playerCharacterOne, PlayerCharacter playerCharacterTwo) {
		setCharacterOne(playerCharacterOne);
		setCharacterTwo(playerCharacterTwo);
		setGameMode(2);
		setTurn(0);
	}
	
	/**
	 * AI versus AI
	 * This Constructor is for game mode 3, where the two AI characters fight another
	 * Adds two EnemyCharacter objects to the class
	 * Once the class is constructed using this constructor, the entire combat will be printed into the console.
	 */
	public TurnTracker() {
		setCharacterOne(new RandomEnemy());
		setCharacterTwo(new RandomEnemy());
		setGameMode(3);
		setTurn(0);
	}
	
	//====== [METHODS] ======
	
	/**
	 * Runs a turn.
	 * Each turn each character's statistics are printed.
	 * Then the characters take their actions simultaneously.
	 * Then calls the nextTurn method.
	 */
	private void runTurn() {
		System.out.println("===================== [" + "Turn: " + getTurn() + "] =====================");
		
		System.out.println(characterOne.getName() + ":");
		System.out.println(
				"(Health: " + characterOne.getCurrentHealth() + " | " +
				"Attack: " + characterOne.getAttack() + " | " +
				"Defense: " + characterOne.getDefense() + " | " + 
				"Search: " + characterOne.getSearch() + ")");
		
		System.out.println("---------------------------------------------");
		
		System.out.println(characterTwo.getName() + ":");
		System.out.println(
				"(Health: " + characterTwo.getCurrentHealth() + " | " +
				"Attack: " + characterTwo.getAttack() + " | " +
				"Defense: " + characterTwo.getDefense() + " | " + 
				"Search: " + characterTwo.getSearch() + ")\n");
		
		// Prompt for character for action
		// Character One does have an advantage as their turns are made first. 
		System.out.println("\n    [" + characterOne.takeAction(characterTwo) + "]\n");
		
		System.out.println("    [" + characterTwo.takeAction(characterOne) + "]\n\n");
		
		nextTurn();
	}
	
	/**
	 * Checks if both players are alive, if so, then runs another turn.
	 * If a character died, then it checks the game mode to determine if the game ends
	 * Game mode 1 ends when the player character dies. If the AI dies in this game mode,
	 * a new enemy is spawned and another turn is ran.
	 * Game mode 2 and 3 ends when either character dies.
	 */
	public void nextTurn() {
		
		// Both Character's are still alive, runs next turn
		if ((characterOne.getCurrentHealth() > 0) && (characterTwo.getCurrentHealth() > 0)) {
			setTurn(turn + 1);
			runTurn();
		}
		
		// A character has died, and the game mode is in Player versus AI
		// Check which died and either spawn new enemy or end game
		else if (getGameMode() == 1) {
			
			// Player is slain, game over
			if (characterOne.getCurrentHealth() <= 0){
				System.out.println("===================== [GAME OVER] =====================" + "\n" + 
						characterOne.getName() + " was slain by " + characterTwo.getName() +"!\n"+ 
						gameModeOneReview());
			}
			
			// Enemy is slain, create a new enemy, runs next turn
			else {
				System.out.println( characterOne.getName() + " has slain " + characterTwo.getName() + "!");
				
				characterTwo = new RandomEnemy();
				
				setTurn(turn + 1);
				runTurn();
			}
		}
		// A character has died, and the game mode is either in player vs player or AI vs AI
		// Ends the game
		else {
			// Character one is alive
			if (characterOne.getCurrentHealth() > 0)
				System.out.println("===================== [GAME OVER] =====================" + "\n" + 
					characterTwo.getName() + " was slain by " + characterOne.getName() +"!\n"+ 
					characterOne.getName() + " WINS!\n" + 
					gameModeTwoReview());
			// Character two is alive
			else if (characterTwo.getCurrentHealth() > 0)
				System.out.println("===================== [GAME OVER] =====================" + "\n" + 
					characterOne.getName() + " was slain by " + characterTwo.getName() +"!\n"+ 
					characterTwo.getName() + " WINS!\n" + 
					gameModeTwoReview());
			// Both characters are dead
			else
				System.out.println("===================== [GAME OVER] =====================" + "\n" + 
					characterOne.getName() + " and " + characterTwo.getName() + " have both fallen!\n"+ 
					characterOne.getName() + " and " + characterTwo.getName() + "DRAW!\n" + 
					gameModeTwoReview());
		}
	}
	
	/**
	 * This is the end of game review for game mode one.
	 * This creates a string contains an overview of the game.
	 * Includes the total turns and enemies encountered, as well as the player's final statistics.
	 * @return A string containing the overview
	 */
	private String gameModeOneReview() {
		String outputString = 
				"Total turns: " + getTurn() + "\n" +
				"Enemies encountered: " + EnemyCharacter.getTotalEnemyCount() + "\n\n" + 
				characterOne.displayStats() + "\n" + 
				"Thanks for playing!";
		
		return outputString;
	}
	
	/**
	 * This is the end of game review for game mode two and tree.
	 * This creates a string contains an overview of the game.
	 * Includes the total turns and enemies encountered, as well as the player's final statistics.
	 * @return A string containing the overview
	 */
	private String gameModeTwoReview() {
		String outputString = 
				"Total turns: " + getTurn() + "\n\n" + 
				characterOne.displayStats() + "\n" +
				characterTwo.displayStats() + "\n" +
				"Thanks for playing!";
		return outputString;
	}
	
	
	//====== [GETTER AND SETTER METHODS] ======
	
	/**
	 * Gets the turn number
	 * @return the turn
	 */
	public static int getTurn() {
		return turn;
	}

	/**
	 * Sets the turn number
	 * Can not be less than 0.
	 * @param turn the turn to set
	 */
	private void setTurn(int turn) {
		if (turn >= 0) this.turn = turn;
	}

	/**
	 * @param characterOne the characterOne to set
	 */
	public void setCharacterOne(Character characterOne) {
		this.characterOne = characterOne;
	}

	/**
	 * @param characterTwo the characterTwo to set
	 */
	public void setCharacterTwo(Character characterTwo) {
		this.characterTwo = characterTwo;
	}

	/**
	 * @return the gameMode
	 */
	public int getGameMode() {
		return gameMode;
	}

	/**
	 * @param gameMode the gameMode to set
	 */
	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}	
}
