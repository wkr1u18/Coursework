/**
 * Class for modelling cyclic fixed appliances in the simulation. It inherits from {@link Appliance} class.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */

public class CyclicFixed extends Appliance {
	private float unitsUsage;
	private int activeHours; 
	
	private int internalClock = 0;
	
	/**
	 * CyclicFixed constructor taking initialising Appliance master class fields as well as class-specific fields. ActiveHours must be in range [1,24]
	 * @param name String containing appliance name.
	 * @param unitsUsage float describing units' usage of this device
	 * @param activeHours int describing number of hours, when the device is active.
	 * @throws IllegalArgumentException when activeHours is not in range [1, 24]
	 */
	public CyclicFixed(String name, float unitsUsage, int activeHours) throws IllegalArgumentException {
		super(name);
		this.unitsUsage = unitsUsage;
		this.activeHours = activeHours;
		if(this.activeHours < 1 || this.activeHours > 24) {
			throw new IllegalArgumentException("activeHours out of range: " + this.activeHours + " expected range: [1,24]");
		}
	}
	
	/**
	 * Handles the cyclic fixed appliance's behaviour when the time passes.
	 * @throws Exception when trying to access non-initialised {@link Meter} object
	 */
	@Override
	public void timePasses() throws Exception {
		if(internalClock == 24) {
			internalClock = 0;
		}
		if(isActive() && internalClock<activeHours) {
			this.tellMeterToConsumeUnits(unitsUsage);
		}
		internalClock++;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float getFixedUnitsUsage() {
		return unitsUsage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCycleLength() {
		return activeHours;
	}
}