/**
 * Abstract class at top of the hierarchy of all different appliances.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class Appliance {
	private String name;
	private Meter meter;
	private String utilityName = null;
	
	Boolean active;
	
	/**
	 * Constructor setting up the class, taking name of the appliance and connecting the {@link Meter} object to it. The appliance is initialised as active.
	 * @param name String containing appliance name
	 */
	public Appliance(String name) {
		this.name = name;
		active = true;
	}
	
	/**
	 * Abstract method denoting the time passing.
	 * @throws Exception when children classes operate on non-initialised {@link Meter objects}
	 */
	public void timePasses() throws Exception {
		
	}
	
	/**
	 * Calls associated {@link Meter} to consume given amount of units.
	 * @param unitsAmount float describing amount of units to be consumed
	 * @throws Exception when called with no {@link Meter} object attached 
	 */
	protected void tellMeterToConsumeUnits(float unitsAmount) throws Exception {
		if(meter==null) {
			throw new Exception("Cannot update non-initialised meter.");
		}
		this.meter.consumeUnits(unitsAmount);
	}
	
	/**
	 * Sets appliance's meter to a given one.
	 * @param meter Reference to {@link Meter} object to be attached to this appliance
	 * @throws Exception if trying to attach Appliance with given {@link Meter} allegiance to wrong {@link Meter}
	 */
	public void setMeter(Meter meter) throws Exception {
		
		if(utilityName!=null) {
			if(!utilityName.equals(meter.getUtilityName())) {
				throw new Exception("Cannot set " + utilityName + " device to meter of " + meter.getUtilityName());
			}
		}
		this.meter = meter;
	}
	
	/**
	 * Sets Appliance allegiance to one specified type of utility
	 * @param utility String containing name of consumed utility
	 */
	public void setUtilityType(String utility) {
		utilityName = utility;
	}
	
	/**
	 * Sets the appliance's status - whether it is active or not. If device is not active, then calling timePasses() won't update meters.
	 * @param active Boolean value describing status of the device. True stands for active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * Checks whether devices is active.
	 * @return Boolean value describing status of the device. True stands for active.
	 */
	public Boolean isActive() {
		return active;
	}
	/**
	 * Public getter to name field
	 * @return String containing the name of Appliance
	 */
	public String getName() {
		return name;
	}
}
