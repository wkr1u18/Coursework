import java.util.ArrayList;

/**
 * House method modelling behaviour of smart house, its appliances and meters connected to them.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class House {
	private Meter waterMeter;
	private Meter electricMeter;
	
	private ArrayList<Appliance> applianceList;
	
	/**
	 * Constructor creating House object and initialising it's {@link Meter} and internal objects for storing information about connected appliances
	 */
	public House() {
		//Creates two Meter objects and initialises them with null values of counters and prices given in documentation
		waterMeter = new Meter("water", 0.013, 0);
		electricMeter = new Meter("electic", 0.002, 0);
		//Creates empty ArrayList for storing Appliance objects
		applianceList = new ArrayList<Appliance>();
	}
	
	/**
	 * Adds new {@link Appliance} object to the house and attaches it to the electric meter.
	 * @param newAppliance reference to {@link Appliance} object to be installed in the house.
	 */
	public void addElectricAppliance(Appliance newAppliance) {
		applianceList.add(newAppliance);
		newAppliance.setMeter(electricMeter);
	}
	
	/**
	 * Adds new {@link Appliance} object to the House and attaches it to the water meter.
	 * @param newAppliance reference to {@link Appliance} object to be installed in the house. 
	 */
	public void addWaterAppliance(Appliance newAppliance) {
		applianceList.add(newAppliance);
		newAppliance.setMeter(waterMeter);
	}
	
	/**
	 * Removes specified {@link Appliance} object form the house
	 * @param applianceToRemove {@link Appliance} to be removed from house
	 */
	public void removeAppliance(Appliance applianceToRemove) {
		applianceList.remove(applianceToRemove);
	}
	
	/**
	 * Method to inquire about the number of {@link Appliance} objects connected to the house.
	 * @return int containing the amount of installed {@link Appliance} object in the house.
	 */
	public int numAppliances() {
		return applianceList.size();
	}
	
	public double activate() {
		double totalCost = 0;
		//Iterates through all appliances stored in applianceList and calls their timePasses method
		for(Appliance currentAppliance : applianceList) {
			currentAppliance.timePasses();
		}
		totalCost += waterMeter.report();
		totalCost += electricMeter.report();
		return totalCost;
	}
}
