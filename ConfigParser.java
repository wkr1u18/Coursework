import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class used for parsing configuration files and creating the appropriate {@link House} and {@link Appliance} objects.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */

public class ConfigParser {
	private BufferedReader fileIn;
	private House simulationHouse;
	
	/**
	 * Parses a given configuration file into {@link House} object
	 * @param directory String containing path to configuration file formatted in coursework specification
	 * @return {@link House} object constructed using rules from configuration file 
	 */
	public House parseConfig(String directory, Meter electricMeter, Meter waterMeter){
		simulationHouse = new House(electricMeter, waterMeter);
		String inputLine = null;
		
		try {
			fileIn = new BufferedReader(new FileReader(new File(directory)));
			while((inputLine = fileIn.readLine()) != null)
			{
				String[] splittedInstruction = inputLine.split(": ", 2);
				switch(splittedInstruction[0]) {
				case "name":
					parseBlock(splittedInstruction[1]);
					break;
				case "doubleappliance":
					//Parse two following blocks - for water and electric part of DoubleAppliance
					Appliance waterPart = parseBlock(splittedInstruction[1]+"water");
					Appliance electricPart = parseBlock(splittedInstruction[1]+"electric");
					//Create and attach the device to the house
					DoubleAppliance newDoubleAppliance = new DoubleAppliance(splittedInstruction[1], waterPart, electricPart);
					simulationHouse.addDoubleAppliance(newDoubleAppliance);
					break;
				case "ecodoubleappliance":
					if((inputLine = fileIn.readLine())==null)
					{
						throw new Exception("Empty line in eco double appliance declaration");
					}
					//Construct all the components of the DoubleAppliance by parsing the blocks of the config file
					Appliance waterPartNormal = parseBlock(splittedInstruction[1]+"water");
					Appliance electricPartNormal = parseBlock(splittedInstruction[1]+"electric");
					Appliance waterPartEco = parseBlock(splittedInstruction[1]+"waterEco");
					Appliance electricPartEco = parseBlock(splittedInstruction[1]+"electricEco");
					//Create DoubleAppliance with eco mode by using overloaded constructor 
					DoubleAppliance newEcoDoubleAppliance = new DoubleAppliance(splittedInstruction[1], waterPartNormal, electricPartNormal, waterPartEco, electricPartEco);
					
					//Decode the line stating the mode of the device, whether is it eco or not
					String[] modeInstruction = inputLine.split(": ");
					if(!modeInstruction[0].equals("mode")) {
						throw new Exception("Not a \"mode:\" command!");
					}
					switch(modeInstruction[1]) {
					case "eco":
						newEcoDoubleAppliance.setToEco(true);
						break;
					case "normal":
					default:
						newEcoDoubleAppliance.setToEco(false);
						break;
					}
					simulationHouse.addDoubleAppliance(newEcoDoubleAppliance);
					break;
				default:
						throw new Exception("Syntax error, while reading config file!");
				}
			}
			fileIn.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '"+ directory + "'");
			System.exit(0);
		}
		catch(IOException ex) {
			System.out.println("Error reading file '"+ directory + "'");
			System.exit(0);
		}
		catch(Exception e) {
			System.out.println(e);
			System.exit(0);
		}
		return simulationHouse;
	}

	/*
	 * Internal method parsing one block of instructions and constructing appropriate Appliance object and attaching it to master House object. Throws Exception, which is handled by parseConfig method.
	 * Returns Appliance object which is created while parsing. Normal Appliances are automatically connected to the House. Attaching of the appliances forming a DoubleAppliance are is done outside of this method, so the reference to created Appliance object is needed. 
	 */
	private Appliance parseBlock(String applianceName) throws Exception{
		String inputLine = null;
		int counter = 0;
		
		//Variables used while creating new Appliances
		String meter = "";
		String subclass = "";
		float minUnitsConsumed = 0;
		float maxUnitsConsumed = 0;
		float fixedUnitsConsumed = 0;
		int oneInN = 0;
		int cycleLength = 0 ;
		
		try {
			while(counter<8 && (inputLine = fileIn.readLine()) != null)
			{
				//Split the statements over ": " separator
				String[] splittedInstruction = inputLine.split(": ");
				//If it consists of two parts, ie. command is defined, then parse it
				if(splittedInstruction.length==2) {
					//switch over the command (converted to lower case)
					switch(splittedInstruction[0].toLowerCase()) {
				case "subclass":
						subclass = splittedInstruction[1];
						break;
				case "meter":
						meter = splittedInstruction[1];
						break;
				case "min units consumed":
						minUnitsConsumed = Float.parseFloat(splittedInstruction[1]);
						break;
				case "max units consumed":
						maxUnitsConsumed = Float.parseFloat(splittedInstruction[1]);
						break;					
				case "fixed units consumed":
						fixedUnitsConsumed = Float.parseFloat(splittedInstruction[1]);
						break;
				case "probability switched on": 
						//If its probability given in format 1 in N, split it over " in " String
						String[] fraction = splittedInstruction[1].split(" in ");
						if(fraction.length==2) {
							oneInN = Integer.parseInt(fraction[1]);
						}
						else {
							//If after splitting, it consists of one part, then throw an Exception
							throw new Exception("syntax error while parsing config file");
						}
						break;
				case "cycle length":
					//If it is cycle length given in N/24 format split over "/" sign
					String [] cycleLengthString = splittedInstruction[1].split("/");
					if(cycleLengthString.length==2) {
						cycleLength = Integer.parseInt(cycleLengthString[0]);
					}
					else {
						//If after splitting, it consists of one part, then throw an Exception
						throw new Exception("syntax error while parsing config file");
					}
					break;
				default:
					//If none of the above, then throw an Exception
					throw new Exception("syntax error: command not recognised " + splittedInstruction[0]);
					}
				}
				//Increment the counter, which manages the position in block of commands describing Appliance
				counter++;
			}

		}
		catch(IOException e) {
			System.out.println(e);
		}
		//After processing whole text block, create new Appliance object of required type and add it to the simulated house
		Appliance newAppliance;
		
		//Switch over subclass string (converted to lower case)
		switch(subclass.toLowerCase()) {
		case "cyclicfixed":
			newAppliance = new CyclicFixed(applianceName, fixedUnitsConsumed, cycleLength);
			break;
		case "cyclicvaries":
			newAppliance = new CyclicVaries(applianceName, minUnitsConsumed, maxUnitsConsumed, cycleLength);
			break;
		case "randomfixed":
			newAppliance = new RandomFixed(applianceName, fixedUnitsConsumed, oneInN);
			break;
		case "randomvaries":
			newAppliance = new RandomVaries(applianceName, minUnitsConsumed, maxUnitsConsumed, oneInN);
			break;
		default:
				throw new Exception("syntax error: wrong name of the appliance");
		}
		
		//Switch over the type of connected meter and call appropriate method on house
		switch(meter) {
		case "electricity":
		case "electric":
			newAppliance.setUtilityType("electric");
			simulationHouse.addElectricAppliance(newAppliance);
			return newAppliance;
		case "water":
			newAppliance.setUtilityType("water");
			simulationHouse.addWaterAppliance(newAppliance);
			return newAppliance;
		case "electricdouble": 
		case "electricitydouble": 
			newAppliance.setUtilityType("electric");
			return newAppliance;
		case "waterdouble": 
			newAppliance.setUtilityType("water");
			return newAppliance;
		default:
			throw new Exception("syntax error: meter name: " + meter + " not recognised");
		}
		
	}
}
