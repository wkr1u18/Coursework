//Non-final version - for testing purposes only
public class Main {
	public static void main(String[] args) {
		
		/*House myHouse = new House();
		
		Appliance myAppliance = new CyclicFixed("mydevice", (float) 2.00, 4);
		Appliance myAppliance2 = new CyclicFixed("mywaterdevice", (float) 16.00, 8);
		myHouse.addElectricAppliance(myAppliance);
		myHouse.addWaterAppliance(myAppliance2);
		System.out.println("Total cost: " + myHouse.activate(30));
		*/
		ConfigParser myparser = new ConfigParser();
		House myHouse = myparser.parseConfig("config.txt");
		double total = myHouse.activate(7*24);
		System.out.println("total cost: " + total);
		
	}
}
