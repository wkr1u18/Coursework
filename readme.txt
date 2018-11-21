Smart Home Simulator
Author: Wojciech Rozowski (wkr1u18@soton.ac.uk)

0.1 Information for markers:

Completed parts of the coursework: Parts 1-7
Extensions to the coursework: 
1) JavaDoc documentation (which can be found in the doc subfolder)
2) Added the simulation of appliances using both electricity and water. Additionally added the functionality for devices with eco mode. (extension discussed later)

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
