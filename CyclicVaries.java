import java.util.Random;

/**
 * Class for modelling cyclic varies appliances in the simulation. It inherits from {@link Appliance} class.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class CyclicVaries extends Appliance {
	private int activeHours;
	private float minUnitsConsumed;
	private float maxUnitsConsumed;
	
	private int internalClock = 0;
	Random r;
	/**
	 * CyclicVaries constructor taking initialising Appliance master class fields as well as class-specific fields.
	 * @param name String containing appliance name.
	 * @param minUnitsConsumed float describing minimal units' usage of this device
	 * @param maxUnitsConsumed float describing maximal units' usage of this device 
	 * @param activeHours int describing number of hours, when the device is active.
	 * @throws IllegalArgumentException when activeHours is not in range [1, 24]
	 */
	public CyclicVaries(String name, float minUnitsConsumed, float maxUnitsConsumed, int activeHours) {
		super(name);
		this.activeHours = activeHours;
		
		//If the min and max values are in incorrect order, swap them
		if(minUnitsConsumed<maxUnitsConsumed) {
			this.minUnitsConsumed = minUnitsConsumed;
			this.maxUnitsConsumed = maxUnitsConsumed;
		}
		else {
			this.minUnitsConsumed = maxUnitsConsumed;
			this.maxUnitsConsumed = minUnitsConsumed;
		}
		r = new Random();
	}
	
	/**
	 * Handles the cyclic fixed appliance's behaviour when the time passes.
	 */
	@Override
	public void timePasses() {
		if(internalClock == 24) {
			internalClock = 0;
		}
		if(internalClock<activeHours) {
			float unitsConsumed = minUnitsConsumed + r.nextFloat() * (maxUnitsConsumed-minUnitsConsumed);
			this.tellMeterToConsumeUnits(unitsConsumed);
		}
		internalClock++;
	}
}

