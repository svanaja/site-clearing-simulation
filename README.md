# site-clearing-simulation
Before a supervisor can work at a construction site, they are to be trained in how to coordinate with 
a bulldozer operator to clear the site in preparation for building. The training consists of interactive 
sessions with a simulator, which you will write.
Domain Background
The simulated construction site is a rectangular shape, described as a grid of square blocks each 
with equal size. Each square block of the site may be described in one of four different ways:
1. It is plain land, and can be cleared by having the bulldozer pass through it or stop on it. 
There is a fixed amount of fuel consumed in the process.
2. It has rocky ground, so it will consume twice as much fuel as plain land to clear unless it has 
been cleared already by a previous visit, in which case it will consume the same amount of 
fuel as visiting plain land.
3. It contains a tree that can be cleared, so it will consume twice as much fuel as plain land to
clear unless it has been cleared already by a previous visit, in which case it will consume the 
same amount of fuel as visiting plain land. If you attempt to clear a tree by passing through a 
square with a tree that has not yet been cleared instead of stopping exactly on it, the 
bulldozer’s paint will get scratched by the tree branches. There will be a financial penalty 
imposed to account for the time required to repair the damage to the bulldozer.
4. It contains a tree that must be preserved. An attempt to visit or pass through this kind of 
square will end the simulation and also incur an extra cost to pay for legal expenses. The site 
will never have a ring of unremovable trees that surround one or more squares that could 
otherwise be cleared.
The trainee supervisor interacts with the simulator by giving it instructions at the command line. 
Their goal is to clear all of the clearable parts of the construction site and to keep costs to a 
minimum. In the real world the instructions might be sent to a bulldozer operator by radio, or 
perhaps written into a plan for the bulldozer operator to follow. Therefore each command (other 
than quitting the simulation) incurs a cost to pay for the overhead of communicating with the 
bulldozer driver.

**Application Requirements**
_Inputs_:
1. A file containing a site map. This will be specified on the command line when the 
application is started.
2. Commands entered by the trainee on the console during the simulation run, as described 
below under "Operation".
_Outputs_:
1. A list of all the commands that were entered by the trainee.
2. A table providing itemized costs of the clearing operation and a total cost.
_Sequence of Operation_:
1. The application is started with the site map file name provided as a parameter to the 
application.
2. The site map is displayed on the console for the trainee.
3. The trainee enters one command per line on the console. Commands are executed as soon as 
the user presses “Enter” until one of the following simulation ending events occurs:
◦ there is an attempt to navigate beyond the boundaries of the site;
◦ there is an attempt to remove a tree that is protected;
◦ the trainee enters the quit command.
4. The simulation ends and commands are no longer accepted. A list of commands entered and 
an itemised expense report is displayed on the console.
_Rules_:
The site map is defined by a text file with one character per square of the site. Each row must have 
the same number of characters. Plain land is marked with the letter ‘o’, rocky land is marked with 
the letter ‘r’, removable trees are marked with the letter ‘t’, and trees that must be preserved are 
marked with the letter ‘T’. For example, the following describes a site that is 10 squares wide and 5 
squares deep:
ootooooooo
oooooooToo
rrrooooToo
rrrroooooo
rrrrrtoooo
The initial position of the bulldozer will be outside of the site, to the left of the top left (north west) 
square of the site, facing towards the east. The bulldozer will never be blocked (by an unremovable 
tree) from entering the site by driving east.
The available commands are:
• Advance: this command takes a positive integer parameter to define the number of squares 
the bulldozer should move forwards (in whatever direction it is currently facing);
• Left: turn the bulldozer (on the spot) 90 degrees to the left of the direction it is facing;
• Right: turn the bulldozer 90 degrees to the right;
• Quit: end the simulation.

Commands may be abbreviated to their first letter. Commands will be executed whenever the 
"Enter" key is pressed.
An attempt to move beyond the boundaries of the site will end the simulation even if there is 
uncleared land.

Fuel usage is to be accounted for in the following way:
**Activity Fuel Usage**
Clearing plain land - 1 fuel unit
Visiting any land that has already been cleared - 1 fuel unit
Clearing rocky land - 2 fuel units
Clearing land containing a tree - 2 fuel units

The costs of the simulated land clearing operation is to be accounted for in the following way:
**Item Cost**
communication overhead per command sent to bulldozer operator - 1 credit
fuel - 1 credit per fuel unit
uncleared square at end of simulation - 3 credits per square
destruction of protected tree - 10 credits
repairing paint damage to bulldozer clearable tree - 2 credit
