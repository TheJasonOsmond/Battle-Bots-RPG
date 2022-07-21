package model;
import java.util.Random;

import application.TurnTracker;

/**
 * An abstract class that is also the child of Character
 * This class creates the statistics of the AI Characters 
 * and contains the possible actions AI characters can take.
 * The child classes will determine the AI's attack pattern. 
 * @author Jason Osmond
 */
public abstract class EnemyCharacter extends Character {
	
	//====== [CONSTANTS] =======	
	public static final int BASE_TOTAL_STAT_VALUE = 8;
	public static final int DEFAULT_BASEHEALTH = 30;

	
	//====== [INSTANCE VARIABLES] =======	
	
	private static int totalEnemyCount = 0;
	private int increaseDefenseAmount = 2; 
	private int level;
	
	//====== [CONSTRUCTORS] =======	
	
	/**
	 * Calls constructor in parent class
	 * This will eventually call the setDefaultStats() method from this class
	 * which will set the stats to random values based on the turn number.
	 * Increases the totalEnemyCount by 1.
	 */
	public EnemyCharacter() {
		super();		
		setTotalEnemyCount(getTotalEnemyCount() + 1);
	}
	
	//====== [ABSTRACT METHODS] =======	

	/**
	 * Abstract call for the takeAction method
	 * Determines which of the actions will be executed
	 */
	public abstract String takeAction(Character playerCharacter);
	
	//====== [METHODS] =======	
	
	/**
	 * The default way to assign the stats of EnemyCharacters
	 * The amount of points a EnenmtyCharacter can assign to its stats
	 * scales with its level (in this iteration, level = turnCount).
	 * The allocation of stats is randomly determined
	 */
	protected void setDefaultStats() {
		setLevel(TurnTracker.getTurn() + 1);
		Random randomStat = new Random();
		int pointBuy = (int) (BASE_TOTAL_STAT_VALUE + (Math.ceil(getLevel()/3)));	
		
		// Initialize statistics
		setBaseHealth(EnemyCharacter.DEFAULT_BASEHEALTH);
		setAttack(0);
		setDefense(0);
		setSearch(0);	
		setToughness(0);
				
		int randomValue;
		
		// This loop will get slower the more enemies are killed,
		// Find a better way of finding random values without having skewed results
		for (int count = 0; count < pointBuy; count++ ) {
			randomValue = randomStat.nextInt(4);
			
			// Points in attack and defense increase stats by 2
			if (randomValue == 0) {
				increaseAttack(2);
			}
			else if (randomValue == 1) {
				increaseDefense(2);
			}
			else if (randomValue == 2) {
				increaseSearch(1);
			}
			else {
				setToughness(getToughness() + 1);
			}
		}		
	}
	
	/**
	 * Contains a list of possible actions that can be taken by the EnemyCharacter.
	 * Finds the chosen action within the list of actions 
	 * available to this character and executes that action.
	 * If the action is not present in the list, the takeAction method 
	 * is called again, so the AI can choose another action. 
	 * @param opponent the opponent of this character
	 * @param action the action to be executed
	 * @return A String describing the outcome of the action
	 */
	protected String executeAction(Character opponent, String action) {
		String actionReport = "No action was made";
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
		
		//Weaken
		else if (action.equals("w") || action.equals("weaken")) {
			actionReport = executeActionWeaken(opponent);	

		}
		
		// The action was invalid, no action is made.
		else {
			System.out.println("Invalid Action");
		}
		
		return actionReport;
	}
	
	//====== [GETTER AND SETTER METHODS] =======	
	
	/**
	 * Sets the totalEnemyCount
	 * @param enemyCount
	 */
	public static void setTotalEnemyCount( int enemyCount) {
		if (enemyCount >= 0)
			totalEnemyCount = enemyCount;
	}
	
	/**
	 * Gets the total number of enemies created.
	 * @return the enemyCount
	 */
	public static int getTotalEnemyCount() {
		return totalEnemyCount;
	}
	
	/**
	 * Returns the level of the EnemyCharacter.
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level of the EnemyCharacter.
	 * The new level must be greater than 0.
	 * @param level the level to set
	 */
	private void setLevel(int level) {
		if (level >= 0)
			this.level = level;
	}

	/**
	 * Overrides abstract method in Character
	 * The amount the character's defense is increased by when taking the defend action.
	 * @return the increaseDefenseAmount
	 */
	public int getIncreaseDefenseAmount() {
		return increaseDefenseAmount;
	}

	/**
	 * Changes the amount the character's defense is increased by when taking the defend action.
	 * @param increaseDefenseAmount the increaseDefenseAmount to set
	 */
	public void setIncreaseDefenseAmount(int increaseDefenseAmount) {
		this.increaseDefenseAmount = increaseDefenseAmount;
	}
	
}
