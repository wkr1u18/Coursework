//Non-final version - for testing purposes only
public class Main {
	public static void main(String[] args) {
		House myHouse = new House();
		/**
		Appliance myAppliance = new RandomVaries("mydevice", (float) 1.00, (float) 2.00, 5);
		myHouse.addElectricAppliance(myAppliance);
		myHouse.activate();
		**/
		Meter myBatteryMeter = new Meter("batmet", (double) 1.00, (float) 0);
		myBatteryMeter.consumeUnits(-5);
		myBatteryMeter.report();
		myBatteryMeter.consumeUnits(2);
		myBatteryMeter.report();
		myBatteryMeter.consumeUnits(4);
		myBatteryMeter.report();
	}
}
