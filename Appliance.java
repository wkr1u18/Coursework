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
	 * @param meter Reference for {@link Meter} object managing this appliance usage.
	 */
	public Appliance(String name, Meter meter) {
		this.name = name;
		this.meter = meter;
	}
	
	/**
	 * Abstract method denoting the time passing.
	 */
	public void timePasses() {
		
	}
}
