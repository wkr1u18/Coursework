//Non-final version - for testing purposes only
public class Main {
	public static void main(String[] args) {
		House myHouse = new House();
		
		Appliance myAppliance = new CyclicFixed("mydevice", (float) 1.00, 5);
		myHouse.addElectricAppliance(myAppliance);
		
		Appliance myAppliance2 = new CyclicFixed("mydevice2", (float) 1.00, 5);
		myHouse.addWaterAppliance(myAppliance2);
		
		System.out.println(myHouse.numAppliances());
		for(int i = 0; i<5; i++) {
			myHouse.activate();
		}
		System.out.println("total cost: " + myHouse.activate());
		System.out.println("total cost: " + myHouse.activate());
	}
}
