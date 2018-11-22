import java.util.ArrayList;

/**
 * Class used for modelling devices using two resources at once (water and electric - eg. dishwasher)
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class DoubleAppliance implements EcoDevice{
	private String name;
	public Appliance waterAppliance;
	private Appliance electricAppliance;
	private Appliance ecoWaterAppliance;
	private Appliance ecoElectricAppliance;
	private Boolean hasEco;
	private Boolean mode;
	
	/**
	 * Creates new DoubleAppliance, which hasn't eco mode.
	 * @param name String containing the name of this appliance
	 * @param waterAppliance reference to Appliance object maintaining usage of water
	 * @param electricAppliance reference to Appliance object maintaining usage of electricity
	 */
	public DoubleAppliance(String name, Appliance waterAppliance, Appliance electricAppliance) {
		this.name = name;
		this.waterAppliance = waterAppliance;
		this.electricAppliance = electricAppliance;
		hasEco = false;
		this.setToEco(false);
	}
	
	/**
	 * Creates new DoubleAppliance, which has eco mode.
	 * @param name String containing the name of this appliance
	 * @param waterAppliance reference to Appliance object maintaining usage of water
	 * @param electricAppliance reference to Appliance object maintaining usage of electricity
	 * @param ecoWaterAppliance reference to Appliance object maintaining usage of water in eco mode
	 * @param ecoElectricAppliance reference to Appliance object maintaining usage of electricity in eco mode
	 */
	public DoubleAppliance(String name, Appliance waterAppliance, Appliance electricAppliance, Appliance ecoWaterAppliance, Appliance ecoElectricAppliance) {
		this.name = name;
		this.waterAppliance = waterAppliance;
		this.electricAppliance = electricAppliance;
		this.ecoWaterAppliance = ecoWaterAppliance;
		this.ecoElectricAppliance = ecoElectricAppliance;
		hasEco = true;
		mode = false;
		this.setToEco(false);
	}
	
	/**
	 * Returns ArrayList of {@link Appliance} objects which use electricity in this device.
	 * @return ArrayList of {@link Appliance} objects
	 */
	public ArrayList<Appliance> getElectricAppliances() {
		ArrayList<Appliance> electricAppliances = new ArrayList<Appliance>();
		electricAppliances.add(electricAppliance);
		if(ecoElectricAppliance != null) {
			electricAppliances.add(ecoElectricAppliance);
		}
		return electricAppliances;
	}
	
	/**
	 * Returns ArrayList of {@link Appliance} objects which use water in this device.
	 * @return ArrayList of {@link Appliance} objects
	 */
	
	public ArrayList<Appliance> getWaterAppliances() {
		ArrayList<Appliance> waterAppliances = new ArrayList<Appliance>();
		waterAppliances.add(waterAppliance);
		if(ecoWaterAppliance != null) {
			waterAppliances.add(ecoWaterAppliance);
		}
		return waterAppliances;
	}
	
	/**
	 * Switches the device between modes. This method toggles the active fields of all {@link Appliance} objects, which are used to model a DoubleAppliance.
	 * @param Boolean value stating the device mode to be set. True for eco, false for non-eco mode. 
	 */
	@Override
	public void setToEco(Boolean mode) {
		this.mode = mode;
		if(!hasEco) {
			return;
		}
		if(mode==false) {
			electricAppliance.setActive(true);
			waterAppliance.setActive(true);
			ecoElectricAppliance.setActive(false);
			ecoWaterAppliance.setActive(false);
		}
		else {
			electricAppliance.setActive(false);
			waterAppliance.setActive(false);
			ecoElectricAppliance.setActive(true);
			ecoWaterAppliance.setActive(true);
		}
	}
	
	/**
	 * Checks whether this appliance has eco mode.
	 * @return True if has eco mode, otherwise its false
	 */
	public Boolean hasEco() {
		return hasEco;
	}
	
}
