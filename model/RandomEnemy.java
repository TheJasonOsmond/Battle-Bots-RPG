package model;

import java.util.Random;

/**
 * A child class of EnemyCharacter.
 * This enemy type randomly takes actions.
 * @author Jason Osmond
 */
public class RandomEnemy extends EnemyCharacter{
	
	//====== [INSTANCE VARIABLES] =======
	
	private static int randomEnemiesFought = 0;
	
	//====== [CONSTRUCTORS] =======
	
	/**
	 * Calls constructor in parent class
	 * This will call the setDefaultStats() method from the EnemyCharacter class
	 * which will set the stats to random values based on the turn number.
	 * Increases the the count of randomEnemiesFought and names the character.
	 */
	public RandomEnemy() {
		super();
		
		setRandomEnemiesFought(getRandomEnemiesFought() + 1);
		
		setName("Random Robot " + getRandomEnemiesFought() + " [Lvl " + getLevel()  + "]");
	}
	
	//====== [METHODS] =======	
	
	/**
	 * Randomly picks an action to take.
	 * The actions it can take are attack, defend, search, and weaken.
	 * Each action has a 25% chance to be taken.
	 * Then calls the executeAction method passing the action selected as a parameter
	 * @param opponent 
	 * @return A String describing the outcome of the action
	 */
	public String takeAction(Character playerCharacter) {
		Random randomAction = new Random();
		int randomValue = randomAction.nextInt(100);
		String action;
		
		// Possible Actions: Attack (A), Defend (D), Search (S), Weaken (W)
		
		// 25% chance to attack
		if (randomValue < 25)
			action = "attack";
		
		// 25% chance to defend
		else if (randomValue < 50)
			action = "defend";
		
		// 25% chance to search
		else if (randomValue < 75)
			action = "search";
		
		// 25% chance to weaken
		else
			action = "weaken";
		
		return executeAction(playerCharacter,action);
	}

	//====== [GETTER AND SETTER METHODS] ======
	
	/**
	 * Gets the count of all the random enemies fought
	 * @return the randomEnemiesFought
	 */
	private static int getRandomEnemiesFought() {
		return randomEnemiesFought;
	}

	/**
	 * Set the number of random enemies fought to 
	 * @param randomEnemiesFought the randomEnemiesFought to set
	 */
	private static void setRandomEnemiesFought(int randomEnemiesFought) {
		if (randomEnemiesFought >= 0)
			RandomEnemy.randomEnemiesFought = randomEnemiesFought;
	}
}
