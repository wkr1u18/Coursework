Smart Home Simulator
Author: Wojciech Rozowski (wkr1u18@soton.ac.uk)

0.1 Information for markers:

Completed parts of the coursework: Parts 1-7
Extensions to the coursework: 
1) JavaDoc documentation (which can be found in the doc subfolder)
2) Added the simulation of appliances using both electricity and water. Additionally added the functionality for devices with eco mode. (extension discussed later)
3) It is possible to load multiple config files, merge them and save the state of the appliances into the config files 

0.2 Command line usage:
To run the simulations, there must be provided a configuration file in format specified in the documentation. 
It is possible to specify exact time span of time span to be simulated, but when it is not specified the default value of one week (7*24 hours) is used.

Valid formats:
java SmartHouseSimulator [config file path] [time in hours to simulate] - opens specified config file and runs the simulation for the specified number of hours
java SmartHouseSimulator [config file path] - opens specified config file and runs the simulation for time span of one week (7*24 hours)
java SmartHouseSimulator - looks for config.txt file in the same folder and runs the simulation for the time span of one week (7*24 hours) (Mostly used for testing purpuoses in Eclipse project)

0.3 Configuration file format

The simulator uses the format of the configuration file, which meets the standard given in the coursework specification.
However for the purposes of the extensions the format has been enhanced. 
The format consists of HEADERS and BLOCKS.

0.3.1 Valid headers:

Example 1: (describes an appliance having name "applianceName"):
name: applianceName

Example 2: (describes an DoubleAppliance having name "applianceName"):
doubleappliance: applianceName

Example 3: (describes an DoubleAppliance with eco mode having name "applianceName" and sets it to eco mode):
ecodoubleappliance: applianceName
mode: eco

Example 4: (describes an DoubleAppliance with eco mode having name "applianceName" and sets it to normal mode):
ecodoubleappliance: applianceName
mode: normal

NOTE: any mode string different than "eco" will set the eco DoubleAppliance to normal mode

0.3.2 Block format

Every block consists of 7 fields, which describe every appliance. The parser fetches all the lines and selects the data needed to create Appliance of specified subclass 

Format looks as following:
subclass: SubclassNameAsString (one of: CyclicFixed, CyclicVaries, RandomFixed, RandomVaries)
meter: meterNameAsAString (one of: water, electric, electricity, electricdouble, electricitydouble, waterdouble)
Min units consumed: (float number)
Max units consumed: (float number)
Fixed units consumed: (float number)
Probability switched on: 1 in N (N is an integer, greater than 0)
Cycle length: N/24 (N is an integer, greater than 0)

Example block:

subclass: CyclicFixed
meter: electric
Min units consumed:
Max units consumed:
Fixed units consumed: 0.1
Probability switched on:
Cycle length: 24/24

0.3.3 General structure rules
Every header must be followed by one or more blocks (depending on the block). Every block must be separated from other by one empty line.

If the header consists of "name: " then only one block is needed, as it models normal Appliance with given name. Example:
name: Digital clock
meter: electric
Min units consumed:
Max units consumed:
Fixed units consumed: 0.1
Probability switched on:
Cycle length: 24/24

If the header consists of "doubleappliance: ", then two blocks must be specified:
1) First one describing water part of the device, where the meter field must be: waterdouble
2) Second one describing electric part of the device, where the meter field must be: electricdouble (electricaldouble is also accepted)

If the header starts with: "ecodoubleappliance: ", then four blocks must be specified:
1) First one describing water part of the device, where the meter field must be: waterdouble
2) Second one describing electric part of the device, where the meter field must be: electricdouble (electricaldouble is also accepted)
3) Third one describing water part of the device in the eco mode, where the meter field must be: waterdouble
4) Fourth one describing electric part of the device in the eco mode, where the meter field must be: electricdouble (electricaldouble is also accepted)

0.3.4 Example valid configuration file part describing DoubleAppliance with eco mode:

ecodoubleappliance: DishWasher
mode: normal
subclass: CyclicFixed
meter: waterdouble
Min units consumed:
Max units consumed:
Fixed units consumed: 6
Probability switched on:
Cycle length: 24/24

subclass: CyclicFixed
meter: electricdouble
Min units consumed:
Max units consumed:
Fixed units consumed: 6
Probability switched on:
Cycle length: 24/24

subclass: CyclicFixed
meter: waterdouble
Min units consumed:
Max units consumed:
Fixed units consumed: 3
Probability switched on:
Cycle length: 24/24

subclass: CyclicFixed
meter: electricdouble
Min units consumed:
Max units consumed:
Fixed units consumed: 3
Probability switched on:
Cycle length: 24/24

0.4 Default meters setup
There are two meters initialised:
1) Meter for "water" with initial reading of 0, where one unit costs 0.002
2) BatteryMeter for "electric" with initial reading of 0, initial mains meter reading to 0, intial battery meter reading to 0, where one unit costs 0.013 and the battery capacity is 6.0 units
note: prices for unit are taken from coursework specification

1.Extensions
1.1 JavaDoc documentation

To provide an clear and neat documentation of code, I have decide to document all the classes and their methods using Java-Doc. 
Generated documentation is available at "doc" subfolder, as well all javaDoc tags can be seen in source files.

1.2 DoubleAppliance and eco devices

Inspired by coursework specification I have decided to implement simulation of devices using two types of utility (like dishwasher, which uses both electricity and water).
I additionally have added the eco option, and the possibility to toggle between eco and normal mode in the runtime of the simulation.
Extension doesn't affect basic functionality of the simulation, and it still meets the original coursework specification.

1.2.1 Eco mode implementation
The core of the idea is that of modelling eco mode devices two different Appliance objects are used. One for the normal mode and one for eco mode.
If device is in one of the modes, the second Appliance must be inactive. Switching between modes is setting one Appliance object to be active and the other to be inactive.

1.2.2 Toggling the Appliance's activity
To implement that idea i have added Boolean field "active" in the Appliance class. If the object is active this value is true, otherwise is false. 
Moreover, I added public setter and getter to that field. Appliance is normally initialised as active.
In every subclass of the Appliance (CyclicFixed, CyclicVaries, RandomFixed and RandomVaries) I have modified the timePasses() method.
The associated Meter object is update only when the device is active, otherwise just internal time passing is recorded.
Updating the time allows to activate the appliances of Cyclic* subclasses to be toggled in the middle of their time cycles. 
Knowing that all appliances are normally set as active, this extension doesn't affect normal specification of the coursework.
-hasEco() method

1.2.3 EcoDevice interface
However DoubleAppliance is the only group of devices that has eco mode so far, I have decided to separate eco mode switching from DoubleAppliance class and implement it as interface.
EcoDevice interface provides only one method: setToEco() which takes Boolean value. It turns device into eco mode if supplied Boolean value is true, otherwise it sets into normal mode.

1.2.4 General concept of DoubleAppliance
DoubleApplaince consists of four Appliance objects. Pair of water and electric Appliances for normal mode, and other pair for eco mode. Only one pair is active at one time.
There are two constructors - one taking two Appliances and setting the Appliance without eco mode, where toggle functions doesn't affect device behaviour.
The other one takes four Appliance objects and allows to switch between modes. There is boolean value initialised by constructor, which is a flag whether device has eco mode.
Also, there is another Boolean value for keeping track of current mode of the device.

When a DoubleAppliance is connected to a House, its electric and water appliances are connected to House using addElectricAppliance() and addWaterAppliance(). 
As there can be more than one electric or water devices in DoubleAppliance so the linkage to the House is done using ArrayList of Appliance objects.
I have implemented two getter methods: one returning ArrayList of water appliances and the other one returning ArrayList of electric appliances.

1.2.5 Changes to House class
To work with ArrayLists of Appliance objects, I have created overloaded versions of addElectricDevice() and addWaterDevice(). 
Each of them calls the add...Device() method for each of the elements of the collection.
To toggle all house eco devices between eco and normal mode, I have additionally implemented switchToEco() method. 
To provide working implementation of this I have added new field of the House class - an ArrayList of EcoDevice objects. 
While adding a DoubleAppliance, besides adding it's internal Appliance objects to House, the reference to DoubleAppliance is stored in ArrayList of EcoDevices.
I created two overloaded versions of removeAppliance() method: 
- One taking ArrayList of Appliance objects, to instantly remove multiple Appliances.
Eg. Eco and normal water appliances of some eco doubleAppliance.
- Other one taking reference to DoubleAppliance and removing it's ecoDevice and Appliance instances from the House.
- Overdubbed devices (changes in numAppliances method)

1.2.6 Changes to the parser
As mentioned earlier some new functionality was added to the config file parser part of the project. 
I have splitted the headers and blocks of the config file. If the header specifies the definition of doubleappliance or ecodoubleappliance,
then the  parser fetches accordingly 2 or 4 blocks specifying Appliance objects making appropriate DoubleAppliance. 
If the header specifies normal Appliance ("name" command) then it fetches one block defining normal Appliance object and constructs it.
-name scheme of appliance object making a DoubleAppliance

Saving extension:
Changes to House class:

created getAppliances() method


Changes to parser:
writeBlock()

Changes to appliances:
created getName() method

CyclicFixed:
getUnitsUsage()
getActiveHours()




