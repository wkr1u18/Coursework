import java.util.Random;

/**
 * Class for modelling random fixed appliances in the simulation. It inherits from {@link Appliance} class.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class RandomFixed extends Appliance {
	private float unitsUsage;
	private int oneInN; 
	
	Random r;
	
	/**
	 * CyclicFixed constructor taking initialising Appliance master class fields as well as class-specific fields. ActiveHours must be in range [1,24]
	 * @param name String containing appliance name.
	 * @param unitsUsage float describing units' usage of this device
	 * @param oneInN int describing 1 in N probability that device will be working
	 */
	public RandomFixed(String name, float unitsUsage, int oneInN) {
		super(name);
		this.unitsUsage = unitsUsage;
		this.oneInN = oneInN;
		r = new Random();
	}
	
	/**
	 * Handles the cyclic fixed appliance's behaviour when the time passes.
	 */
	@Override
	public void timePasses() {
		//Take the random number from the range [0, oneInN-1] and if it's equal to 0 (event with probability 1 / oneInN) 
		if(r.nextInt(oneInN) == 0) {
			//Consume units if equal to zero
			this.tellMeterToConsumeUnits(unitsUsage);
		}
	}
}
