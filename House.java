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
	private ArrayList<EcoDevice> ecoDevices;
	
	//Keeps track of overdubbed appliances (eg. if device consists of four appliances this variable should be increased by 3)
	private int virtualAppliances; 
	
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
		ecoDevices = new ArrayList<EcoDevice>();
		virtualAppliances = 0;
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
		try {
			newAppliance.setMeter(electricMeter);
		}
		catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	
	/**
	 * Overloaded version of addElectricAppliance() method, which adds ArrayList of {@link Appliance} objects to the house.
	 * @param electricAppliances ArrayList of {@link Appliance} objects to be added to the house.
	 */
	public void addElectricAppliance(ArrayList<Appliance> electricAppliances) {
		for(Appliance e: electricAppliances) {
			this.addElectricAppliance(e);
		}
	}
	
	/**
	 * Adds new {@link Appliance} object to the House and attaches it to the water meter.
	 * @param newAppliance reference to {@link Appliance} object to be installed in the house. 
	 */
	public void addWaterAppliance(Appliance newAppliance) {
		applianceList.add(newAppliance);
		try {
			newAppliance.setMeter(waterMeter);
		}
		catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	
	/**
	 * Overloaded version of addWaterAppliance() method, which adds ArrayList of {@link Appliance} objects to the house.
	 * @param waterAppliances ArrayList of {@link Appliance} objects to be added to the house.
	 */
	public void addWaterAppliance(ArrayList<Appliance> waterAppliances) {
		for(Appliance w: waterAppliances) {
			this.addWaterAppliance(w);
		}
	}	
	
	/**
	 * Adds {@link DoubleAppliance} to the house.
	 * @param newAppliance reference to {@link DoubleAppliance} object to be added to the hous
	 */
	public void addDoubleAppliance(DoubleAppliance newAppliance) {
		ecoDevices.add(newAppliance);
		if(newAppliance.hasEco()) {
			//Eco DoubleAppliance consists of four Appliance objects so there are three virtual (overdubbed) appliances
			virtualAppliances += 3;
		} 
		else {
			//Normal DoubleAppliance consists of two Appliance objects so there is one virtual (overdubbed) appliance
			virtualAppliances += 1;
		}
		this.addWaterAppliance(newAppliance.getWaterAppliances());
		this.addElectricAppliance(newAppliance.getElectricAppliances());
	}
	
	/**
	 * Removes specified {@link Appliance} object form the house
	 * @param applianceToRemove {@link Appliance} to be removed from house
	 */
	public void removeAppliance(Appliance applianceToRemove) {
		applianceList.remove(applianceToRemove);
	}
	
	/**
	 * Removes all the {@link Appliance} objects from given ArrayList from the house.
	 * @param appliancesToRemove ArrayList of {@link Appliance} objects to be removed from the house
	 */
	public void removeAppliance(ArrayList<Appliance> appliancesToRemove){
		for(Appliance a : appliancesToRemove) {
			removeAppliance(a);
		}
	}
	
	/**
	 * Removes specified {@link DoubleAppliance} object from the house
	 * @param applianceToRemove reference to {@link DoubleAppliance} object to be removed from the house
	 */
	public void removeAppliance(DoubleAppliance applianceToRemove) {
		ecoDevices.remove(applianceToRemove);
		removeAppliance(applianceToRemove.getElectricAppliances());
		removeAppliance(applianceToRemove.getWaterAppliances());
	}
	
	/**
	 * Method to inquire about the number of {@link Appliance} objects connected to the house.
	 * @return int containing the amount of installed {@link Appliance} object in the house.
	 */
	public int numAppliances() {
		return applianceList.size() - virtualAppliances;
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
	
	/**
	 * Overloaded version of activate() method which activates the house for the specified number of hours.
	 * @param numberOfHours int specifying number of hours that will be simulated.
	 * @return double specifying the total costs of running the house for specified period of time
	 */
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
	
	/**
	 * Switches all the eco devices in the house to specified mode.
	 * @param mode Boolean value specifying new mode of all connected eco devices. True for eco mode, false for default mode.
	 */
	public void switchToEco(Boolean mode) {
		for(EcoDevice e : ecoDevices) {
			e.setToEco(mode);
		}
	}
	
	/**
	 * Public getter to internal ArrayList of {@link Appliance} objects managed by house.
	 * @return ArrayList of {@link Appliance} objects stored in instance of House class
	 */
	public ArrayList<Appliance> getAppliances() {
		return applianceList;
	}
	
	/**
	 * Sets the time of internal clocks of all {@link Appliance} objects.
	 * Part of simulation restoring extension
	 * @param numOfHours int primitive stating the number of hour to shift from initial state
	 */
	public void setTime(int numOfHours) {
		for(Appliance a: applianceList) {
			a.setTime(numOfHours);
		}
	}
	
	/**
	 * Main method for running the simulation. Parses the command line arguments and initialises the simulation.
	 * @param args Command line arguments containing path to configuration file and simulation length in hours. If no initial time is specified, the default value of 7*24 hours is used.  
	 */
	public static void main(String[] args) {

		ArrayList<String> inputFiles = new ArrayList<String>();
		//Initial settings collected by command line arguments parser
		String outputFile = null;
		Boolean toEco = false;
		Boolean toNormal = false;
		Boolean restore = false;
		String restorePath = null;
		Boolean save = false;
		String savePath = null;
		int simulationLength = 7*24;
		if(args.length>0) {
			int i = 0;
			while(i<args.length) {
				switch(args[i]) {
					case "-i":
					case "--input":
						if((i+1)<args.length) {
							inputFiles.add(args[++i]);
						}
						else {
							System.out.println("Invalid syntax!");
							System.exit(0);
						}
						break;
					case "-o":
					case "--output":
						if((i+1)<args.length) {
							outputFile = args[++i];
						}
						else {
							System.out.println("Invalid syntax!");
							System.exit(0);
						}
						break;
					case "-t":
					case "--time":
						if((i+1)<args.length) {
							simulationLength = Integer.parseInt(args[++i]);
						}
						else {
							System.out.println("Invalid syntax!");
							System.exit(0);
						}
						break;
					case "-m":
					case "--merge":
						simulationLength = 0;
						break;
					case "-e":
					case "--eco":
						toEco = true;
						break;
					case "-n":
					case "--normal":
						toNormal = true;
						break;
					case "-r":
					case "--restore":
						if((i+1)<args.length) {
							restorePath = args[++i];
							restore = true;
						}
						else {
							System.out.println("Invalid syntax!");
							System.exit(0);
						}
						break;
					case "-s":
					case "--save":
						if((i+1)<args.length) {
							savePath = args[++i];
							save = true;
						}
						else {
							System.out.println("Invalid syntax!");
							System.exit(0);
						}
						break;
					case "-h":
					case "--help":
					default:
						System.out.println("SmartHomeSimulator by Wojciech Rozowski (wkr1u18@soton.ac.uk)");
						System.out.println("Valid command line options: ");
						System.out.println("-i [file] or --input [file] : reads appliance configuration from [file]");
						System.out.println("-o [file] or --output [file] : saves appliance configuration to [file]");
						System.out.println("-t [hours] or --time [houes] : runs the simulation for given amount of hours");
						System.out.println("-m or --merge : merges input appliance configuration files into one output file");
						System.out.println("-e or --eco : switches devices with eco mode to power saving mode");
						System.out.println("-n or --normal : switches devices with eco mode to normal mode");
						System.out.println("-r [file] or --restore [file]: loads simulation state from state config file");
						System.out.println("-s [file] or --save [file]: saves simulation state to state config file");
						System.exit(0);
				}
				i++;
			}
		}
		else {
			//Opens config.txt for 7*24 hours.  Used mostly for testing, while developing.
			System.out.println("No initial values specified. Starting simulation for default testing setting");
			System.out.println("To find out about all possible command line options use -h or --help");
		}

		//Depending on the command line options - parses the simulation configuration file or sets default values for simulation
		SimulationStateManager manager;
		if(restore) {
			manager = new SimulationStateManager(restorePath);
		}
		else {
			manager = new SimulationStateManager();
		}
		House myHouse = manager.getHouse();
		System.out.println("Starting simulation with initial time: " + manager.getInitialTime() + " hours.");
		myHouse.setTime(manager.getInitialTime());
		
		//Reads appliance configuration files
		ConfigParser myParser = new ConfigParser(myHouse);
		if(inputFiles.size()==0) {
			//If no input files given open the default named file from the same directory
			myParser.parseConfig("config.txt");
		}
		else {
			for(String s : inputFiles) {
				myParser.parseConfig(s);
			}
		}
		
		//Runs the simulation in eco or normal mode
		if(toEco ^ toNormal) {
			myHouse.switchToEco(toEco);
		}
		
		double total = 0;
		//Performs the simulation
		if(simulationLength>0) {
			total = myHouse.activate(simulationLength);
			if(restore) {
				total += manager.getInitialCost();
			}
			System.out.println("Simulation finnished. Total cost: " + total);
		}
		
		//Saves the appliances configurations
		if(outputFile!=null) {
			myParser.saveAppliances(outputFile);
		}
		
		//Saves the simulation state
		if(save) {
			manager.saveState(savePath, total, simulationLength);
		}

	}
}
