import java.util.ArrayList;

/**
 * Main class providing main method for Smart House simulator
 * @author Wojciech Rozowski (wkr1u18)
 *
 */
public class SmartHouseSimulator {
	/**
	 * Main method creating simulation using config file
	 * @param args Command line arguments containing path to configuration file and simulation length in hours. If no initial time is specified, the default value of 7*24 hours is used.  
	 */
	public static void main(String[] args) {

		ArrayList<String> inputFiles = new ArrayList<String>();
		String outputFile = null;
		//Default path to configuration file
		String filePath = "config.txt";
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
					case "-h":
					case "--help":
					default:
						System.out.println("SmartHomeSimulator by Wojciech Rozowski (wkr1u18@soton.ac.uk)");
						System.out.println("Valid command line options: ");
						System.out.println("-i [file] or --input [file] : reads appliance configuration from [file]");
						System.out.println("-o [file] or --output [file] : saves appliance configuration to [file]");
						System.out.println("-t [hours] or --time [houes] : runs the simulation for given amount of hours");
						System.out.println("-m or --merge : merges input appliance configuration files into one output file");
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

		//Parses the configuration file and starts the simulation
		
		Meter waterMeter = new Meter("water", (float) 0.002, 0);
		BatteryMeter electricMeter = new BatteryMeter("electric", (double) 0.013, (float) 0, (float) 0, (float) 6.0);
		House myHouse = new House(electricMeter, waterMeter);
		
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
		
		
		//Performs the simulation
		if(simulationLength>0) {
			double total = myHouse.activate(simulationLength);
			System.out.println("Simulation finnished. Total cost: " + total);
		}
		
		
		//Saves the appliances configurations
		if(outputFile!=null) {
			myParser.saveAppliances(outputFile);
		}

	}
}
