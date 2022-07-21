package model;
import java.util.Dictionary;
import java.util.Random;
import java.util.Scanner;

/**
 * A child class of Character that a user will be able to control
 * The character's statistics are set using the constructors
 * @author Jason Osmond
 */
public class PlayerCharacter extends Character {
	
	//====== [CONSTANTS] =======	
	
	public static final int DEFAULT_POINTS_AVAILABLE = 12;
	public static final int DEFAULT_BASEHEALTH = 100;
	public static final int DEFAULT_ATTACK = 1;
	public static final int DEFAULT_DEFENSE = 3;
	public static final int DEFAULT_SEARCH= 4;
	public static final int DEFAULT_TOUGHNESS= 2;

	//====== [INSTANCE VARIABLES] =======	
	
	private int increaseDefenseAmount = 2; 
	
	//====== [CONSTRUCTORS] =======	
	
	/**
	 * Calls constructor in parent class
	 * This will eventually call the setDefaultStats() method from this class
	 * which will set the Character's stats to the default values.
	 */
	public PlayerCharacter() {
		super();
	}
	
	/**
	 * This constructor takes all the stat values as parameters, with the exception of health
	 * These values are passed to the super constructor. 
	 * The health parameter of the super constructor uses the constant DEFAULT_BASEHEALTH
	 * @param attackStat
	 * @param defenseStat
	 * @param searchStat
	 * @param toughnessStat
	 */
	public PlayerCharacter(int attackStat, int defenseStat, int searchStat, int toughnessStat){
		super(DEFAULT_BASEHEALTH, attackStat, defenseStat, searchStat, toughnessStat);		
	}

	//====== [METHODS] =======	
	
	/**
	 * Sets the character's stats to the default/recommended values
	 * These stats are found in the comments
	 */
	protected void setDefaultStats(){
		setBaseHealth(PlayerCharacter.DEFAULT_BASEHEALTH);	
		setAttack(PlayerCharacter.DEFAULT_ATTACK * 3);
		setDefense(PlayerCharacter.DEFAULT_DEFENSE * 3);
		setSearch(PlayerCharacter.DEFAULT_SEARCH);
		setToughness(PlayerCharacter.DEFAULT_TOUGHNESS);
	}
	
	/**
	 * Prompts the user for the action the player is taking.
	 * Then calls the executeAction method passing the action selected as a parameter
	 * @param opponent 
	 * @return A String describing the outcome of the action
	 */
	public String takeAction(Character opponent) {
		// Prompt User for input
		Scanner scan = new Scanner(System.in);

		System.out.println(getName() + "'s actions: Attack (A), Defend (D), Search (S), Weaken (W)");
		String action = scan.nextLine().toLowerCase();
		
		return executeAction(opponent, action);
	}
	
	/**
	 * Contains a list of possible actions that can be taken by the PlayerCharacter.
	 * Finds the chosen action within the list of actions 
	 * available to this character and executes that action.
	 * If the action is not present in the list, the takeAction method 
	 * is called again, so the user can input another action. 
	 * Has 2 cheat codes to make testing easier.
	 * @param opponent the opponent of this character
	 * @param action the action to be executed
	 * @return A String describing the outcome of the action
	 */
	private String executeAction(Character opponent, String action) {
		String actionReport =  "No action was selected";
		
		// Attack
		if (action.equals("a") || action.equals("attack")) {
			actionReport = executeActionAttack(opponent);
		}
		
		// Defend
		else if (action.equals("d") || action.equals("defend")) {
			actionReport = executeActionDefend();
		}
		
		// Search
		else if (action.equals("s") || action.equals("search")) {
			actionReport = executeActionSearch();		
		}
		
		// Weaken
		else if (action.equals("w") || action.equals("weaken")) { 
			actionReport = executeActionWeaken(opponent);	

		}
		
		// Cheat code for better statistics
		else if (action.equals("highground")) {
			int cheater = 100;
			increaseCurrentHealth(cheater);
			increaseAttack(cheater);
			increaseDefense(cheater);
			increaseSearch(cheater);
			
			actionReport = getName() + " has the highground! All Stats +" + cheater;
		}
		
		// Cheat code to kill character
		else if (action.equals("death")) {
			setCurrentHealth(0);
			actionReport = getName() + " has fallen!";
		}
		
		// If the action was not recognized, the user is prompted again for a new action
		else {
			System.out.println("Invalid Action");
			actionReport = this.takeAction(opponent);
		}
		return actionReport;
	}

	//====== [GETTER AND SETTER METHODS] =======	
	
	/**
	 * Overrides abstract method in Character
	 * The amount the character's defense is increased by when taking the defend action.
	 * @return the increaseDefenseAmount
	 */
	public int getIncreaseDefenseAmount() {
		return this.increaseDefenseAmount;
	}
}
