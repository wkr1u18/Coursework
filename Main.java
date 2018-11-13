//Non-final version - for testing purposes only
public class Main {
	public static void main(String[] args) {
		House myHouse = new House();
		
		Appliance myAppliance = new CyclicFixed("mydevice", (float) 1.00, 4);
		Appliance myAppliance2 = new CyclicFixed("mywaterdevice", (float) 1.00, 4);
		myHouse.addElectricAppliance(myAppliance);
		myHouse.addWaterAppliance(myAppliance2);
		System.out.println("Total cost: " + myHouse.activate(4));
		
	}
}
