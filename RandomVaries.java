import java.util.Random;

/**
 * Class for modelling random varies appliances in the simulation. It inherits from {@link Appliance} class.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class RandomVaries extends Appliance {
	private int oneInN; 
	private float minUnitsConsumed;
	private float maxUnitsConsumed;
	Random r;
	
	/**
	 * CyclicFixed constructor taking initialising Appliance master class fields as well as class-specific fields. ActiveHours must be in range [1,24]
	 * @param name String containing appliance name.
	 * @param minUnitsConsumed float describing minimal units' usage of this device
	 * @param maxUnitsConsumed float describing maximal units' usage of this device 
	 * @param oneInN int describing 1 in N probability that device will be working
	 */
	public RandomVaries(String name, float minUnitsConsumed, float maxUnitsConsumed, int oneInN) {
		super(name);
		this.oneInN = oneInN;
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
	 * @throws Exception when trying to access non-initialised {@link Meter} object
	 */
	@Override
	public void timePasses() throws Exception {
		//Take the random number from the range [0, oneInN-1] and if it's equal to 0 (event with probability 1 / oneInN) 
		if(r.nextInt(oneInN) == 0) {
			//Consume units if equal to zero
			float unitsConsumed = minUnitsConsumed + r.nextFloat() * (maxUnitsConsumed-minUnitsConsumed);
			this.tellMeterToConsumeUnits(unitsConsumed);
		}
	}
}
