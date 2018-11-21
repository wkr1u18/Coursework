/**
 * Interface providing setToEco() method for all appliances having eco mode. Currently it is only used by {@link DoubleAppliance}, but it makes it easier to add more eco devices in the future to the code. 
 * @author Wojciech Rozowski (wkr1u18)
 *
 */

public interface EcoDevice {
	/**
	 * Sets the device mode to eco if true. If false, it sets it to default mode.
	 * @param mode Boolean value specifying mode to switch the device into
	 */
	public void setToEco(Boolean mode);
}
