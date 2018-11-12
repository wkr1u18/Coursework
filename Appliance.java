/**
 * Abstract class at top of the hierarchy of all different appliances.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class Appliance {
	String name;
	Meter meter;
	
	/**
	 * Constructor setting up the class, taking name of the appliance and connecting the {@link Meter} object to it.
	 * @param name String containing appliance name
	 */
	public Appliance(String name) {
		this.name = name;
	}
	
	/**
	 * Abstract method denoting the time passing.
	 */
	public void timePasses() {
		
	}
	
	/**
	 * Calls associated {@link Meter} to consume given amount of units.
	 * @param unitsAmount float describing amount of units to be consumed
	 */
	protected void tellMeterToConsumeUnits(float unitsAmount) {
		this.meter.consumeUnits(unitsAmount);
	}
	
	/**
	 * Sets appliance's meter to a given one.
	 * @param meter Reference to {@link Meter} object to be attached to this appliance
	 */
	public void setMeter(Meter meter) {
		this.meter = meter;
	}
}
