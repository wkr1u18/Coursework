/**
 * Class modering the battery, which can store units of particular utility
 * @author wkr1u18
 *
 */
public class Battery {
	float storedUnits;
	float capacityLimit;
	
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
	
	public void store(float numberOfUnits) {
		float newBalance = numberOfUnits + storedUnits;
		if(newBalance>capacityLimit) {
			storedUnits = capacityLimit;
		}
		else {
			storedUnits = newBalance;
		}
	}
	
	public float getBalance() {
		return storedUnits;
	}
	
	public void use(float numberOfUnits) throws IllegalArgumentException{
		if(numberOfUnits>storedUnits) {
			throw new IllegalArgumentException("numberOfUnits cannot be greater than capacity of battery");
		}
		storedUnits-=numberOfUnits;
	}
	
}
