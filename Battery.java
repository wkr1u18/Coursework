/**
 * Class modelling the battery, which can store units of particular utility
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class Battery {
	private float storedUnits;
	private float capacityLimit;
	
	/**
	 * Constructor initialising battery with a given capacity limit
	 * @param capacityLimit float describing maximal amount of units that can be stored in battery;
	 */
	public Battery(float capacityLimit) throws IllegalArgumentException{
		this.storedUnits = 0;
		this.capacityLimit = capacityLimit;
		if(capacityLimit<=0 ) {
			throw new IllegalArgumentException("Capacity limit cannot be negative or equal to zero");
		}
	}
	
	/**
	 * Stores given amount of units in the battery. If charge exceeds the capacity limit, the maximum possible value is stored.
	 * @param numberOfUnits float describing amount of units to be stored in the battery
	 */
	public void store(float numberOfUnits) {
		float newBalance = numberOfUnits + storedUnits;
		//If new balance exceeds the capacity, set the maximum possible charge
		if(newBalance>capacityLimit) {
			storedUnits = capacityLimit;
		}
		else {
			storedUnits = newBalance;
		}
	}
	
	/**
	 * Checks how many units are stored in the battery.
	 * @return float describing amount of units stored in the battery
	 */
	public float getBalance() {
		return storedUnits;
	}
	
	/**
	 * Takes a given amount of units from the battery.
	 * @param numberOfUnits float describing the amount of units to be taken from battery's balance.
	 * @throws IllegalArgumentException if the amount of units to be taken is greater than the balance of the battery.
	 */
	public void use(float numberOfUnits) throws IllegalArgumentException{
		if(numberOfUnits>storedUnits) {
			throw new IllegalArgumentException("numberOfUnits cannot be greater than capacity of battery");
		}
		storedUnits-=numberOfUnits;
	}
	
}
