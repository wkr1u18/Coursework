Smart Home Simulator
Author: Wojciech Rozowski (wkr1u18@soton.ac.uk)

0.1 Information for markers:

Completed parts of the coursework: Parts 1-7
Extensions to the coursework: 
1) JavaDoc documentation (which can be found in the doc subfolder)
2) Added the simulation of appliances using both electricity and water. Additionally added the functionality for devices with eco mode. (extension discussed later)
3) Multiple config files loading, merging them and saving the state of the appliances into the config files
4) Simulation state saving and restoring to simulation state config files

NOTE: All the extensions do not interfere with basic specification of the coursework.

0.2 Command line usage:
To run the simulations, there must be provided a configuration file in format specified in the documentation. 
It is possible to specify exact time span of time span to be simulated, but when it is not specified the default value of one week (7*24 hours) is used.

Valid command line options: 
-i [file] or --input [file] : reads appliance configuration from [file]
-o [file] or --output [file] : saves appliance configuration to [file]
-t [hours] or --time [houes] : runs the simulation for given amount of hours
-m or --merge : merges input appliance configuration files into one output file
-e or --eco : switches devices with eco mode to power saving mode
-n or --normal : switches devices with eco mode to normal mode
-r [file] or --restore [file]: loads simulation state from state config file
-s [file] or --save [file]: saves simulation state to state config file

Example way of invoking the program:
Run the simulation for file config.txt for 1 hour:
java SmartHouseSimulator -i config.txt -t 1

Run the simulation for file config1.txt config2.txt, save the appliances configuration to config.txt, save the output state of simulation to output_state.txt, no initial time specified - default 7*24 hours
java SmartHouseSimulator -i config1.txt -i config2.txt -o config.txt -s output_state.txt

When giving multiple input configuration files, every file name must be followed by -i. So loading configuration files config1.txt and config2.txt would need options "-i config1.txt -i config2.txt"
If no simulation state file is provided, the basic setup of the meters is used (mentioned later).

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

0.5 Simulation state config file format
Also as the config file format, the simulation state files consist of headers and blocks. Every line of the spec must contain two parts: name of the field and the value separated by colon and space.

0.5.1 Headers
1) time: 456 - sets the initial time of the simulation to 456 hours
2) cost: 2137.0 - sets the initial cost of previous simulation to 2137.0
3) meter: water - introduces the block for Meter object for water (can also mainain electric)
4) batterymeter - introduces the block for BatteryMeter object for electric (BatteryMeter cannot work with water)

0.5.2 Blocks
0.5.2.1 Meter block - initialises all the fields of Meter constructor
Example:
unitcost: 1.0
meterreading: 0.0
0.5.2.2 BatteryMeter block - initialises all the fields of overloaded BatteryMeter constructor, which also initialises starting capacity of Battery
unitcost: 2.0
meterreading: 0
batterymeterreading: 37.0
batterycapacity: 8.0
batterybalance: 0

0.5.3 Valid simulation state configuration file example:
time: 456
cost: 2137
meter: water
unitcost: 1.0
meterreading: 0.0
batterymeter: electric
unitcost: 2.0
meterreading: 0
batterymeterreading: 37.0
batterycapacity: 8.0
batterybalance: 0

1.Extensions
1.1 JavaDoc documentation

To provide an clear and neat documentation of code, I have decide to document all the classes and their methods using Java-Doc. 
Generated documentation is available at "doc" subfolder, as well all javaDoc tags can be seen in source files. 
All the methods off the specification have highlighed information about the extension they implement.

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
DoubleAppliance has also hasEco() method, which returns true when it was initalised with eco mode.
Knowing that all appliances are normally set as active, this extension doesn't affect normal specification of the coursework.

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
Moreover I have modified numAppliances method to substract "virtual" devices - eg. if we have one eco mode DoubleAppliance, one normal Double Appliance, then numAppliances would return 6.
Knowing the number of virtual appliances which compose each DoubleAppliance, we can keep track of them and substract their amount from size of ArrayList of Appliance objects stored in House objects.
For the previous examples modified method, now returns two.


1.2.6 Changes to the parser
As mentioned earlier some new functionality was added to the config file parser part of the project. 
I have splitted the headers and blocks of the config file. If the header specifies the definition of doubleappliance or ecodoubleappliance,
then the  parser fetches accordingly 2 or 4 blocks specifying Appliance objects making appropriate DoubleAppliance. 
If the header specifies normal Appliance ("name" command) then it fetches one block defining normal Appliance object and constructs it.

1.2.7 Name scheme of appliance objects making a DoubleAppliance
1) If we have DoubleAppliance called "dishwasher" with eco mode (consisting of 4 virtual appliances) the naming scheme is as follows:
Water part normal mode - #ecodouble@w0
Electric part normal mode - #ecodouble@e0
Water part normal mode - #ecodouble@w1
Electric part normal mode - #ecodouble@e1

2) If we have DoubleAppliance called "dishwasher" without eco mode (consisting of 2 virtual appliances) the naming scheme is as follows:
Water part normal mode - #double@w0
Electric part normal mode - #double@e0

2.0 Configuration saving and merging extension
2.1 Main information
It is now possible to download the state of the House and save it into valid configuration file. To make it possible I needed to add accessor method to different fields of Appliances and House fields.
Also, I have made it possible to read multiple files. So parsing two files and saving them into one output makes it possible to merge them into one config file. When passed -m or --merge option,
simulation isn't started but only data operations on appliances are performed.

2.2 Changes to House class:
- created getAppliances() method which returns stored ArrayList of Appliance objects
- changed numAppliances method to substract virtual overdubbed Apppliance objects coming from DoubleAppliance 

2.3 Changes to parser
- created writeBlock() method which parses one Appliance block and saves into file.
- created saveAppliances(0 which parses all Appliance objects and creates appropriate headers and blocks 

2.4 Changes to appliances:
- added getter methods to main fields of Appliances:
 +getMinConsumption()
 +getMaxConsumption()
 +getFixedConsumption()
 +getName()
 +getOneInN()
 +getUtilityType()

They return Float, Double and Integer objects instead of primitives, as in non-overrided they can return null value, which can be reckognized by output parser.

3.0 Simulation saving and restoring extension
3.1 Overview
I have created SimulationStateManager which can read and output configuration files. I added public accesors to fields of Meters to get their final states at the end of simulation (to save them).
Now it is possible to set starting time of simulation (different time of a day, depending in the moment when simulation was stopped). Main house object is maintained by that class.

3.1 Changes to House class
-added setTime() which sets initial time for all connected Appliances of the day for simulation

3.2 Changes to Appliance class
- added setTime() method which sets the internal clock of given appliance to given time of a day. This method is overrided and implemented in children classes.

3.3 Changes to Meter class
- getMeterReading(), getUnitCost() and getUtilityName() are now public methods (instead of protected visibility, as they were used before only by overrided report() method)

3.4 Changes to BatteryMeter
- added new constructor which also takes initial battery balance
