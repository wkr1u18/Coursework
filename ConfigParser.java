import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Class used for parsing configuration files and creating the appropriate {@link House} and {@link Appliance} objects.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */

public class ConfigParser {
	private BufferedReader fileIn;
	private House simulationHouse;
	private PrintWriter fileOut;
	
	public ConfigParser(House simulationHouse) {
		this.simulationHouse = simulationHouse;
	}
	
	/**
	 * Parses a given configuration file into {@link House} object
	 * @param directory String containing path to configuration file formatted in coursework specification
	 */
	public void parseConfig(String directory){
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
					Appliance waterPart = parseBlock(splittedInstruction[1]+"#double@w0");
					Appliance electricPart = parseBlock(splittedInstruction[1]+"#double@e0");
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
					Appliance waterPartNormal = parseBlock(splittedInstruction[1]+"#ecodouble@w0");
					Appliance electricPartNormal = parseBlock(splittedInstruction[1]+"#ecodouble@e0");
					Appliance waterPartEco = parseBlock(splittedInstruction[1]+"#ecodouble@w1");
					Appliance electricPartEco = parseBlock(splittedInstruction[1]+"#ecodouble@1");
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
				case "":
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
		//return simulationHouse;
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
		Boolean hasMeter = false;
		
		String subclass = "";
		Boolean hasSubclass = false;
		
		float minUnitsConsumed = 0;
		Boolean hasMinUnits = false;
		
		float maxUnitsConsumed = 0;
		Boolean hasMaxUnits = false;
		
		float fixedUnitsConsumed = 0;
		Boolean hasFixedUnits = false;
		
		int oneInN = 0;
		Boolean hasOneInN = false;
		
		int cycleLength = 0;
		Boolean hasCycleLength = false;
		
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
						hasSubclass = true;
						break;
				case "meter":
						meter = splittedInstruction[1];
						hasMeter = true;
						break;
				case "min units consumed":
						minUnitsConsumed = Float.parseFloat(splittedInstruction[1]);
						hasMinUnits = true;
						break;
				case "max units consumed":
						maxUnitsConsumed = Float.parseFloat(splittedInstruction[1]);
						hasMaxUnits = true;
						break;					
				case "fixed units consumed":
						fixedUnitsConsumed = Float.parseFloat(splittedInstruction[1]);
						hasFixedUnits = true;
						break;
				case "probability switched on": 
						//If its probability given in format 1 in N, split it over " in " String
						String[] fraction = splittedInstruction[1].split(" in ");
						if(fraction.length==2) {
							oneInN = Integer.parseInt(fraction[1]);
							hasOneInN = true;
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
						hasCycleLength = true;
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
		
		if(!hasSubclass) {
			throw new Exception("No subclass specified");
		}
		
		//Switch over subclass string (converted to lower case)
		switch(subclass.toLowerCase()) {
		case "cyclicfixed":
			if(!(hasFixedUnits && hasCycleLength)) {
				throw new Exception ("too few arguments too construct cyclicfixed");
			}
			newAppliance = new CyclicFixed(applianceName, fixedUnitsConsumed, cycleLength);
			break;
		case "cyclicvaries":
			if(!(hasMinUnits && hasMaxUnits && hasCycleLength)) {
				throw new Exception ("too few arguments too construct cyclicvaries");
			}
			newAppliance = new CyclicVaries(applianceName, minUnitsConsumed, maxUnitsConsumed, cycleLength);
			break;
		case "randomfixed":
			if(!(hasFixedUnits && hasOneInN)) {
				throw new Exception ("too few arguments too construct randomfixed");
			}
			newAppliance = new RandomFixed(applianceName, fixedUnitsConsumed, oneInN);
			break;
		case "randomvaries":
			if(!(hasMinUnits && hasMaxUnits && hasOneInN)) {
				throw new Exception ("too few arguments too construct randomvaries");
			}
			newAppliance = new RandomVaries(applianceName, minUnitsConsumed, maxUnitsConsumed, oneInN);
			break;
		default:
				throw new Exception("syntax error: wrong name of the appliance");
		}
		
		if(!hasMeter) {
			throw new Exception("no meter specified");
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
	
	/**
	 * Saves all Appliances into config file.
	 * @param fileName String containing path to output file
	 */
	public void saveAppliances(String fileName) {
		try {
			fileOut = new PrintWriter(new File (fileName));
		}
		catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
		ArrayList<Appliance> houseAppliances = simulationHouse.getAppliances();
		String name = null;
		String type = null;
		for(Appliance a : houseAppliances) {
			name = a.getName();
			//If a is part of double appliance, write a header for it and then proceed to decoding its blocks
			if(name.indexOf("#")>0) {
				if(name.endsWith("#double@w0")) {
					fileOut.println("doubleappliance: " + name.split("#")[0]);
				} else if (name.endsWith("#ecodouble@w0")) {
					fileOut.println("ecodubleappliance: " +  name.split("#")[0]);
					//If normal water appliance is active, then device is in non-eco mode
					fileOut.print("mode: ");
					if(a.isActive()) {
						fileOut.println("normal");
					}
					else {
						fileOut.println("eco");
					}
				}
				type = a.getUtilityType();
				writeBlock(a, type+"double");
			}
			else {
				fileOut.println("name: " + name);
				writeBlock(a);
			}
			
		}
		fileOut.close();
	}
	
	/*
	 * Writes one block describing one Appliance object. When passed null as any argument, certain field is just skipped.
	 */
	private void writeBlock(String subclass, String meter, Float minConsumption, Float maxConsumption, Float fixedConsumption, Integer oneInN, Integer cycleLength) {
		fileOut.println("subclass: "+subclass);
		fileOut.println("meter: "+meter);
		fileOut.print("Min units consumed: ");
		if(minConsumption!=null ) {
			fileOut.print(minConsumption.toString());
		}
		fileOut.println();
		fileOut.print("Max units consumed: ");
		if(maxConsumption!=null ) {
			fileOut.print(maxConsumption.toString());
		}
		fileOut.println();
		fileOut.print("Fixed units consumed: ");
		if(fixedConsumption!=null ) {
			fileOut.print(fixedConsumption.toString());
		}
		fileOut.println();
		fileOut.print("Probability switched on: ");
		if(oneInN!=null ) {
			fileOut.print("1 in " + oneInN);
		}
		fileOut.println();
		fileOut.print("Cycle length: ");
		if(cycleLength!=null) {
			fileOut.print(cycleLength+"/24");
		}
		fileOut.println();
		fileOut.println();
	}
	
	/*
	 * Creates an output block from given Appliance and sets it's meter name to given one 
	 */
	private void writeBlock(Appliance appliance, String meterName) {
		String subclassName = null;
		if(appliance instanceof CyclicFixed) {
			subclassName = "CyclicFixed";
		} else if (appliance instanceof CyclicVaries) {
			subclassName = "CyclicVaries";
		} else if (appliance instanceof RandomFixed) {
			subclassName = "RandomFixed";
		} else if (appliance instanceof RandomVaries) {
			subclassName = "RandomVaries";
		}
		
		writeBlock(subclassName, meterName, appliance.getMinConsumption(), appliance.getMaxConsumption(), appliance.getFixedUnitsUsage(), appliance.getOneInN(), appliance.getCycleLength());
	}
	/*
	 * Creates an output block describing given Appliance using it's internal meter name. 
	 * This is default way of building block for standard appliances. Different meter name is used when dealing with DoubleAppliances.
	 */
	private void writeBlock(Appliance appliance) {
		writeBlock(appliance, appliance.getUtilityType());
	}
}
