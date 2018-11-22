/**
 * Battery meter variation of {@link Meter} object.
 * @author Wojciech Rozowski (wkr1u18)
 *
 */

//TODO: Override the report method
public class BatteryMeter extends Meter {
	private Battery internalBattery;
	private float batteryMeterReading;
	
	/**
	 * Constructor creating BatteryMeter object. It's initialisition is similar to {@link Meter} object, but additionally maximal capacity for battery must be specified.
	 * @param utilityName String describing the tpe of the utility
	 * @param unitCost The cost of one unit of this type of utility
	 * @param meterReading float describing initial meter reading (from the mains)
	 * @param initialBatteryMeterReading float describing initial battery meter reading
	 * @param initialCapacity float describing maximal amount of units that can be stored in internal battery.
	 */
	public BatteryMeter(String utilityName, double unitCost, float meterReading, float initialBatteryMeterReading, float initialCapacity) {
		super(utilityName, unitCost, meterReading);
		this.batteryMeterReading = initialBatteryMeterReading;
		internalBattery = new Battery(initialCapacity);
	}
	
	/**
	 * Constructor creating BatteryMeter object. It's initialisation is similar to {@link Meter} object, but additionally maximal capacity for battery must be specified. Used by simulation restore extension.
	 * @param utilityName String describing the tpe of the utility
	 * @param unitCost The cost of one unit of this type of utility
	 * @param meterReading float describing initial meter reading (from the mains)
	 * @param initialBatteryMeterReading float describing initial battery meter reading
	 * @param initialCapacity float describing maximal amount of units that can be stored in internal battery.
	 * @param initialBatteryBalance float describing initial balance of the Battery
	 */
	public BatteryMeter(String utilityName, double unitCost, float meterReading, float initialBatteryMeterReading, float initialCapacity, float initialBatteryBalance) {
		super(utilityName, unitCost, meterReading);
		this.batteryMeterReading = initialBatteryMeterReading;
		internalBattery = new Battery(initialCapacity);
		internalBattery.store(initialBatteryBalance);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void consumeUnits(float consumedUnits) {
		//If the consumedUnits is lesser than zero (production exceeds the demand), then charge the battery
		if(consumedUnits<0) {
			internalBattery.store(-consumedUnits);
		}
		else {
			//If balance of the battery is bigger than amount of units to be consumed, just discharge the battery
			if(internalBattery.getBalance()>=consumedUnits) {
				internalBattery.use(consumedUnits);
				batteryMeterReading+=consumedUnits;
			}
			else {
				//In other cases just take the maximal possible amount of units from battery
				float restToUse = consumedUnits-internalBattery.getBalance();
				batteryMeterReading += internalBattery.getBalance();
				internalBattery.use(internalBattery.getBalance());
				super.consumeUnits(restToUse);
			}
		}
	}
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public double report() {
		System.out.println(super.getMeterReading() + " units of " + super.getUtilityName() + " have been consumed from mains");
		System.out.println(batteryMeterReading + " units of " + super.getUtilityName() + " have been consumed from battery" );
		double finalCost = super.getMeterReading()*super.getUnitCost();
		System.out.println("It costed: " + finalCost);
		//Clear the meter readings
		super.setMeterReading(0);
		batteryMeterReading = 0;
		//Returns the total cost
		return finalCost;
	}
	
	/**
	 * Getter to internal {@link Battery} object capacityLimit field. Used by simulation saving extension.
	 * @return float primitive containing capacity limit of internal battery of this battery meter.
	 */
	public float getInternalBatteryCapacity() {
		return internalBattery.getCapacityLimit();
	}
	
	/**
	 * Getter to internal {@link Battery} object storedUnits field. Used by simulation saving extension.
	 * @return float primitive containing balance of internal battery of this battery meter.
	 */
	public float getInternalBatteryBalance() {
		return internalBattery.getCapacityLimit();
	}
	
	/**
	 * Getter to batteryMeterReading field, containing the amount of units drained from internal {@link Battery} so far. Part of simulation saving extension.
	 * @return float primitive containing the reading of amounts drained from battery
	 */
	public float getBatteryMeterReading() {
		return batteryMeterReading;
	}
}
