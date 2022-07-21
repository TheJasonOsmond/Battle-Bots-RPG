package model;
import java.util.Random;

/**
 * Creates an abstract character. 
 * This character can be controlled by player or AI depending on the child class.
 * This character relies on child classes to determine the character's
 * statistics and how the character decides what action to take.
 * This class contains all the character's statistics and actions the character can take.
 * @author Jason Osmond
 */
public abstract class Character {
	
	//====== [INSTANCE VARIABLES] =======	

	private String name;
	private int baseHealth;
	private int attack;
	private int defense;
	private int search;
	private int toughness; 
	private int currentHealth;
	private int weakenModifier = 1;
	
	//====== [CONSTRUCTORS] =======	
	
	/**
	 * This constructor calls the abstract method setDefaultStats()
	 * to set the characters stats the to the default of the child classes
	 * After setting all the stats, the constructors initializes the current health.
	 */
	public Character() {
		this.setDefaultStats();
		this.initializeCurrentHealth();
	}
	
	/**
	 * This constructor takes all the stat values as parameters,
	 * then sets those stats using those parameters.
	 * After setting all the stats, the constructors initializes the current health.
	 * @param baseHealth
	 * @param attackStat
	 * @param defenseStat
	 * @param searchStat
	 * @param toughnessStat
	 */
	public Character(int baseHealth, int attackStat, int defenseStat, int searchStat, int toughnessStat) {
		this.setBaseHealth(baseHealth);
		this.setAttack(attackStat);
		this.setDefense(defenseStat);
		this.setSearch(searchStat);
		this.setToughness(toughnessStat);
		this.initializeCurrentHealth();
	}
	
	//====== [ABSTRACT METHODS] =======	
	
	/**
	 * Abstract call for the setDefaultStats method
	 * This method sets the character statistics 
	 * For PlayerCharacters stats will be set to the recommended preset
	 * For EnenmyChatacters stats are chosen at random and scale with the turn count
	 * @param aCharacter
	 * @return
	 */
	protected abstract void setDefaultStats();
	
	/**
	 * Abstract call for the takeAction method
	 * This method will decide what the character will take 
	 * @param aCharacter
	 * @return A String describing the outcome of the action
	 */
	public abstract String takeAction(Character aCharacter);
	
	/**
	 * Abstract call for the getIncreaseDefenseAmount
	 * This will get the amount a character's defense will increase by when taking the defend action.
	 * Made this abstract for when I add items that would increase this.
	 * Certain enemies might have different amounts as well.
	 * @return
	 */
	public abstract int getIncreaseDefenseAmount();
	
	//====== [METHODS] =======	

	/**
	 * This method initializes the current health by taking
	 * the base health combining it with the character's toughness
	 */
	protected void initializeCurrentHealth() {
		float toughnessModifier = 0.05f * toughness;
		
		setBaseHealth(Math.round(baseHealth * (toughnessModifier + 1)));
		
		this.setCurrentHealth(getBaseHealth());
	}
	
	/**
	 * This method reduces the characters health by an amount
	 * equal to the difference between opponents's attack power 
	 * and this character's defense.
	 * The minimum damage a character can take is 1.
	 * @param damage
	 * @return
	 */
	protected int takeDamage(int damage) {
		int damageAfterBlock = damage - getDefense();
		
		if (damageAfterBlock < 1) damageAfterBlock = 1;
		
		this.currentHealth = currentHealth - damageAfterBlock;
		
		return damageAfterBlock;
	}
	
	/**
	 * This method "weakens" this character.
	 * Being weakened reduces the charcacter's current health,
	 * defense, and search by an amount equal to the weakenModifier.
	 * Defense and search cannot be reduced past 0.
	 * @param weakenModifier
	 */
	protected void weakened(int weakenModifier) {
		setCurrentHealth(getCurrentHealth() - weakenModifier);
		
		if ((getDefense() - weakenModifier) > 0)
			setDefense(getDefense() - weakenModifier);
		else
			setDefense(0);
		
		if ((getSearch() - weakenModifier) > 0)
			setSearch(getSearch() - weakenModifier);
		else
			setSearch(0);
	}
	
	/**
	 * Increase's character's current health by amount
	 * @param amount
	 */
	protected void increaseCurrentHealth(int amount) {
		setCurrentHealth(getCurrentHealth() + amount);
	}
	
	/**
	 * Increase's character's attack power by amount
	 * @param amount
	 */
	protected void increaseAttack(int amount) {
		setAttack(getAttack() + amount);
	}	
	
	/**
	 * Increase's character's defense by amount
	 * @param amount
	 */
	protected void increaseDefense(int amount) {
		setDefense(getDefense() + amount);
	}
	
	/**
	 * Increase's character's search power by amount
	 * @param amount
	 */
	protected void increaseSearch(int amount) {
		setSearch(getSearch() + amount);
	}
	
	/**
	 * Increase's character's weaken modifier by amount.
	 * This modifier determines what amount the opponent's
	 * statistics are reduced when weakened
	 * @param amount
	 */
	protected void increaseWeakenModifier(int amount) {
		setWeakenModifier(getWeakenModifier() + amount);
	}
	
	/**
	 * This method executes an action made by this character.
	 * Calls the take damage method. 
	 * Has a variable that allows for the attack power to be modified
	 * @param Opponent
	 * @return A String describing the Attack action
	 */
	protected String executeActionAttack(Character Opponent) {
		int attackPower = getAttack(); //added this to have critical strikes to be made later
		
		int damageAfterBlock = Opponent.takeDamage(attackPower);
		
		return getName() + " attacks! (" + damageAfterBlock + " damage dealt)";
	}
	
	/**
	 * This method executes an action made by this character.
	 * Increases the defense value by an amount equal to the character's IncreaseDefenseAmount. 
	 * @param Opponent
	 * @return A String describing the Defend action
	 */
	protected String executeActionDefend() {
		this.increaseDefense(this.getIncreaseDefenseAmount()); // Use getter, or make final, or neither
		
		return getName() + " defends! (+" + getIncreaseDefenseAmount() + " defense)";
	}

	/**
	 * This method executes an action made by this character.
	 * Uses randomly generated values to randomly give the character different buffs.
	 * The value of the buff is also random, with the range of this random value 
	 * equal to the character's search power.
	 * @param Opponent
	 * @return A String describing what buff was gained through the Search action
	 */
	protected String executeActionSearch() {

		Random randomActionSearch = new Random();
		
		// Random number from 0 to 99
		int randomValue = randomActionSearch.nextInt(100);

		int searchModifier = 1;
		
		int searchModifierRange = getSearch();
		
		// Search modifier is a random number between 1 and the character's search stat
		if (searchModifierRange > 0) 
			searchModifier += randomActionSearch.nextInt(searchModifierRange);
		
		// 2% chance to increase all statistics and current health
		if (randomValue < 2) {
			increaseCurrentHealth(searchModifier * 3);
			increaseAttack(searchModifier);
			increaseDefense(searchModifier);
			increaseSearch(searchModifier);
			return getName() + " found the jackpot!" + "(+" + searchModifier + " to all stats, +" + searchModifier * 3 + " health)";
			
		}
		
		// 10% chance to increase Search
		else if (randomValue < 12) {
			increaseSearch(searchModifier);
			return getName() + " found a better magnifying glass! " + "(+" + searchModifier + " search)";
		}
		
		// 10% chance to increase weaken modifier
		else if (randomValue < 22) {
			increaseWeakenModifier(1);
			return getName() + " found some poison! " + "(+" + 1 + " to weakens)";
		}
		
		// 28% chance to increase current health
		else if (randomValue < 50) {
			increaseCurrentHealth(searchModifier * 3);
			return getName() + " found a healing potion! " + "(+" + searchModifier * 3 + " health)";
		}
		
		// 50% chance to increase attack
		else if (randomValue < 100) {
			increaseAttack(searchModifier);
			return getName() + " found new weapon! " + "(+" + searchModifier + " attack)";
		}
		
		else
			return "Random value out of range! ";
	}
	
	/**
	 * This method executes an action made by this character.
	 * Calls the weakened method on the opponent using this character's weakenModifier
	 * @param Opponent
	 * @return A String describing which of the opponents stats were reduced and by how much
	 */
	protected String executeActionWeaken(Character Opponent) {
		Opponent.weakened(getWeakenModifier());
		return getName() + " Weakens " + Opponent.getName() + " (-" + getWeakenModifier() + " to enemy Defense, Search, and Health)";
	}
	
	/**
	 * Creates an output string containing a list of the character's stats.
	 * @return a String containing the character's stats
	 */
	public String displayStats() {
		 String outputString = 
				getName() + "'s total health: " + 	getCurrentHealth() 	+ "\n" +
				getName() + "'s attack power: " + 	getAttack() 		+ "\n" + 
				getName() + "'s defense power: " + getDefense() 		+ "\n" + 
				getName() + "'s search power: " + 	getSearch() 		+ "\n";		
		
		return outputString;	
	}
	
	//====== [GETTER AND SETTER METHODS] =======	
	
	/**
	 * Gets this character's current health.
	 * @return the currentHealth
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	/**
	 * Sets this character's current health.
	 * Can be negative.
	 * @param currentHealth the currentHealth to set
	 */
	protected void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}
	
	/**
	 * Gets this character's name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets this character's name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets this character's base health.
	 * @return the health
	 */
	public int getBaseHealth() {
		return baseHealth;
	}
	
	/**
	 * Sets this character's base health.
	 * Must be greater than 0.
	 * @param health the health to set
	 */
	protected void setBaseHealth(int health) {
		if (health > 0)
			this.baseHealth = health;
	}
	
	/**
	 * Gets this character's attack value.
	 * @return the attack
	 */
	public int getAttack() {
		return attack;
	}
	
	/**
	 * Sets this character's attack value.
	 * Must be greater than or equal to 0.
	 * @param attack the attack to set
	 */
	protected void setAttack(int attack) {
		if (attack >=  0)
			this.attack = attack;
	}

	/**
	 * Gets this character's defend value.
	 * @return the defense
	 */
	public int getDefense() {
		return defense;
	}
	
	/**
	 * Sets this character's defense value.
	 * Must be greater than or equal to 0.
	 * @param defense the defense to set
	 */
	protected void setDefense(int defense) {
		if (defense >=  0)
			this.defense = defense;
	}
	
	/**
	 * Gets this character's search value.
	 * @return the search
	 */
	public int getSearch() {
		return search;
	}
	
	/**
	 * Sets this character's search value.
	 * Must be greater than or equal to 0.
	 * @param search the search to set
	 */
	protected void setSearch(int search) {
		if (search >= 0)
			this.search = search;
	}
	
	/**
	 * Gets this character's toughness.
	 * @return the toughness
	 */
	public int getToughness() {
		return toughness;
	}
	
	/**
	 * Sets this character's toughness.
	 * Must be greater than or equal to 0.
	 * @param toughness the toughness to set
	 */
	protected void setToughness(int toughness) {
		if (toughness >=  0)
			this.toughness = toughness;
	}
	
	/**
	 * Sets this character's weaken modifier
	 */
	public int getWeakenModifier() {
		return weakenModifier;
	}
	/**
	 * Sets this character's weaken modifier
	 * Must be greater than or equal to 0.
	 * @param weakenModifier
	 */
	protected void setWeakenModifier(int weakenModifier) {
		if (weakenModifier >=  0)
			this.weakenModifier = weakenModifier;
	}
	

}	

