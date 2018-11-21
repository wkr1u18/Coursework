/**
 * Main class providing main method for Smart House simulator
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class Main {
	/**
	 * Main method creating simulation using config file
	 * @param args Command line arguments containing path to configuration file and simulation length in hours. If no initial time is specified, the default value of 7*24 hours is used.  
	 */
	public static void main(String[] args) {

		/*
		//Default path to configuration file
		String filePath = "config.txt";
		int simulationLength = 7*24;
		if(args.length>0) {
			if(args.length>1) {
				try {
					//Parse the given number of simulation length
					simulationLength = Integer.parseInt(args[1]);
				}
				catch (NumberFormatException e) {
					System.out.println("Incorrect value specified. Using default value instead.");
				}
			}
			//Use the first argument given
			filePath = args[0];
		}
		else {
			//Opens config.txt for 7*24 hours.  Used mostly for testing, while developing.
			System.out.println("No initial values specified. Starting simulation for default testing setting");
		}

		//Parses the configuration file and starts the simulation
		ConfigParser myparser = new ConfigParser();
		House myHouse = myparser.parseConfig(filePath);
		double total = myHouse.activate(simulationLength);
		//Writes out the final cost of the running the smart house for given period.
		System.out.println("Simulation finnished. Total cost: " + total);
		*/
		House myHouse = new House();
		Appliance waterPart = new CyclicFixed("waterpart", (float) 1.0, 2);
		Appliance electricPart = new CyclicFixed("electricpart", (float)1.0, 2);
		Appliance ecoWaterPart = new CyclicFixed("ecowaterpart", (float) 0.5, 2);
		Appliance ecoElectricPart = new CyclicFixed("ecoelectricpart", (float)0.5, 2);
		
		DoubleAppliance dishwasher = new DoubleAppliance("dishwasher", waterPart, electricPart, ecoWaterPart, ecoElectricPart);
		myHouse.addDoubleAppliance(dishwasher);
		myHouse.activate();
		dishwasher.setToEco(true);
		myHouse.activate();
		myHouse.activate();
		
		

	}
}
