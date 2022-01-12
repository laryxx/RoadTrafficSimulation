# RoadTrafficSimulation
Module 2 of the traffic simulation project

Input data
-	A map given in GeoJSON format
-	JSON file with selected generation points

GUI
Graphical user interface present in the program allows the user to define traffic generation rules for the simulation by specifying a pair of points which would serve as 
source and destination, as well as the intensity of the generation and a chance of spawning for each car type. Apart from that, it also provides the functionality of 
defining the simulation properties, such as start and end time.

Overall algorithm description
After receiving the input from the first module, and then from the user, the program creates its internal data structures necessary for path finding and car driving and 
proceeds its execution by calculating the position of every vehicle on the map and randomly generating new vehicles for every given frame of time.

Path finding
For path finding, an internal data structure – directed unweighted graph is created. It consists of edges that each define two connected nodes by their id’s. 
When given a source and a destination represented by node id’s, the program provides the functionality of a random path search using the Depth-First Search algorithm, 
firstly navigating the path to a randomly selected point on the map, and then navigating from that point to the destination.

Car Driving
For car driving, an internal data structure consisting of Node, Node group and Vehicle objects is created – for every given timeframe of the simulation, 
each car’s information is updated, thus allowing to trace the car movement.

Traffic generation
When generating random traffic, the generation rules with greater intensity are prioritized. When a certain generation rule is ‘picked up’ by the program, 
it then proceeds to choose the car type depending on the car spawn probabilities specified by the user. The newly generated car is then assigned a path and starts its way.
Communication and module results
Communication with module 1 is mostly GUI-based, after receival of the generation points specified by the user in module 1 GUI, module 2 allows the user to 
view a given pair of points using the module 1 map viewer. The results of the simulation are stored in a single JSON file that contains physical latitude and 
longitude coordinates of each car for every time frame, which is given for the other module to process and render.

-----------

Specification of use cases
Use case #1	Running the simulation
Pre-condition	Generation points are specified, simulation properties are specified
Post-condition	The output file is generated
Basic path	Specify generation points -> Specify simulation properties -> Start
Alternative path	Specify generation points -> View some generation points -> Remove some generation points -> Specify simulation properties -> Start
  
Use case #2	Specifying generation rules
Pre-condition	File containing possible generation points is present and processed
Post-condition	The generation rules are stored
Basic path	Module 1 -> Specify Generation rules
Alternative path	

Use case #3	Viewing a pair of points on the map
Pre-condition	Module 1 is running; module 2 GUI is active and ‘view on the map’ button on 1st GUI window is pressed
Post-condition	Points are highlighted 
Basic path	Module 1 -> Specify Generation rules
Alternative path	Press ‘Add rule’ button and press view on the map on the 2nd GUI window

-----------

Architecture of the program
Node-related classes
OuterNode (id, latitude, longitude, node group id, list of outer connections, graph id) – represents a node that connects one node group (road) to another.
InnerNode (id, latitude, longitude, connection id, group id, graph id) – represents a node that only has a connection to another node within a given node group.
NodeGroup (id, type, list of nodes, fitting speed) – represents a road as a collection of nodes.
DefaultNode abstract class – contains the above constructors.

Vehicle-related classes
Truck, Van, Sedan (id, start node id, destination node id, path, intent, speed, path distance in kilometers, last visited node id, progress in kilometers, 
weight in kilograms, distance to next node, progress since last node in kilometers, fitting speed, group nodes left, sum of node distances, acceleration rate, 
latitude, longitude, angle) – represents a vehicle.
Vehicle abstract class – contains the above constructor.

Graph-related classes
Edge (source, destination, source id, destination id) – represents an edge in a graph and contains both graph id connections and node id connections.
Graph (edges, edge count) – represents a graph.

GUI-related classes
GenerationRulesGUI – used for receiving input regarding generation rules from the user.
ViewRulesGUI – used for viewing and/or removing already existing generation rules
StartGUI – used for receiving the input regarding simulation properties and starting the program

Main class
Generator – with usage of all other classes, the generator handles all the functionality.

-----------

Internal data structures
Map as a collection of nodes grouped by node groups
Every node is an object, nodes are grouped to form roads and roads are connected using ‘outer nodes’ as instances of OuterNode object. 
Together, a map is formed allowing to navigate the car movement. Every node has a node id and a graph id – node id is used for ‘car driving’ 
and graph id is used for path finding.

Map as an unweighted directed graph
A collection of edges forms a graph, since edges contain both node and graph ids we can easily calculate the path using the DFS algorithm.

-----------

External data formats
The only external data formats are JSONObject and JSONArray from json-simple library – they are needed to parse JSON and construct the JSON output file.

-----------

UI elements
There are 3 windows, first is used for defining traffic generation rules, in the second one the user may remove already defined rules and the third window is used for 
setting the simulation properties and starting the module program. At any given time, the user may click ‘View on the map’ to see the chosen pair of points 
highlighted on the module 1 map viewer

