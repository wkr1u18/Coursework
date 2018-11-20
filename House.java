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
	 * Constructor creating House object and initialising it's {@link Meter} objects using given references.
	 * @param electricMeter reference to {@link Meter} object measuring the electricity usage
	 * @param waterMeter reference to {@link Meter} object measuring the water usage
	 */
	public House(Meter electricMeter, Meter waterMeter) {
		//Assigns the given references to waterMeter and electricMeter
		this.electricMeter = electricMeter;
		this.waterMeter = waterMeter;
		//Creates empty ArrayList for storing Appliance objects
		applianceList = new ArrayList<Appliance>();
	}
	
	/**
	 * Constructor creating House object and initialising it's {@link Meter} and internal objects for storing information about connected appliances.
	 */
	public House() {
		//Calls overloaded constructor with Meter objects constructed with prices given in the specification.
		this(new Meter("electric", 0.002, 0), new Meter("water", 0.013, 0));
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
	
	/**
	 * Runs the house simulation for one hour
	 * @return double containing the total cost of running all the appliances for one hour 
	 */
	public double activate(){
		double totalCost = 0;
		//Iterates through all appliances stored in applianceList and calls their timePasses method
		for(Appliance currentAppliance : applianceList) {
			try {
				currentAppliance.timePasses();
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		totalCost += waterMeter.report();
		totalCost += electricMeter.report();
		return totalCost;
	}
	
	
	public double activate(int numberOfHours) {
		double totalCost = 0;
		for(int i = 0; i<numberOfHours; i++) {
			try {
				//Wait for 500 ms and then call the one-hour version of activate() method
				Thread.sleep(500);
				totalCost += activate();	
			}
			catch(InterruptedException e) {
				//Ignore the exception
			}
		}
		return totalCost;
	}
}
