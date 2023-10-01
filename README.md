<h1>Traffic flow simulation project</h1>

----

<h2>Program architecture</h2> 
<h3> Module 1 - Defining the simulation </h3>
  Module 1  is responsible for defining all the data that is needed to run the
  simulation. It fetches the map on which the user must select the coordinates
  for start and end generation rules, define frequency of car generation on
  various generation rules as well as select which types of cars should appear
  where. User may select any region of the map to use, as long as it has roads
  on it.
  <br> <br>
  The map is fetched from OpenStreetMap during the application’s runtime, 
  therefore allowing the user to select any place available on the service
  to run the simulation on. When the user has defined all the data necessary
  to run the simulation, they may press the "Start" button, which launches
  the process for getting the map file from the aforementioned web service
  for later use in the next module.
  <br> <br>
  The application uses a number of temporary files for communication between 
  modules, the first module does not take any preliminary data in, but
  generates 3 files to be passed on to the next module. Two of them are the
  map files in ".osm" and ".geojson" format that contain all the road information
  of the selected map area. The other file is a configuration file that
  contains all the generations rules defined by the user as well as simulation
  properties.
  When user launches the first Module, there is some data that needs to be
  defined. This subsection describes the purpose of the defined parameters
  and their functioning. Such data can be divided into two categories - Car
  generation rules as a collection and general data regarding the simulation
  itself. The latter category contains such data as selected map bounds, 
  duration and daytime. Selected map bounds must be stored for the purposes
  of making requests to OpenStreetMap in order to retrieve map files or
  get the map-view as a component of the User Interface in the 3rd module.
  Daytime is not used by the second module, but is a feature to be considered 
  in the event of further development. Duration represents the amount
  of time (in minutes) for which the simulation would be carried out. Every
  second of the simulation is divided into 10 frames. Every set of settings
  has a name and hash id to include the possibility of future re-use. <br> <br>
  Generation rules contain many attributes, and some of them may be 
  self-explanatory by name. Every generation rule has a name and a hash id that
  the 2nd module uses to refer to, and the start/end group id refer to an id
  which is used to refer to a collection of nodes as a part of osm/geojson map
  file structure. The rest are defined by the user. Start latitude and longitude
  as well as end latitude and longitude attributes represent the start and end
  point coordinates of a generation rule respectively. Vans, trucks and sedans
  are boolean values that define which types of cars shall be spawned from
  a particular location. Intensity is a numerical value from 1-5 that 
  represents the frequency of car generation. Module 1 User Interface is as it 
  is demonstrated below:
  <br> <br>
  ![Image](Readme_images\module1UI.png)
  <br><br>

  <h3>Module 2 - Traffic flow simulation</h3>
  Module 2 is responsible for running the simulation itself. It is a "backend" 
  module, which means that the user must use the GUI for displaying
  the simulation module to analyze the generated results. The data from the
  previous module is taken and processed, and once that is done the simulation 
  can start. The results are also stored in 2 temporary files in ".csv" and
  ".json" formats to allow the further parts of the program to demonstrate the
  results. 
  <br> <br>
  The imported map files contain road data in a particular structure, which
  must be transformed before any sort of path-finding or navigation can be
  done. This section of the second application module does precisely that.
  Each node in the initial structure represents a single coordinate on the map - 
  such coordinates are grouped up to define custom map elements such
  as roads, forest areas, buildings and many other things. For this application,
  however, only road nodes are relevant, so they are the only ones
  imported from OpenStreetMap. The map processing component of the
  Simulation data generation module transforms this data into its own 
  system capturing road connections, making it applicable for navigation and
  path-finding.
  <br> <br>
  Each node is its own object stored in memory while the application is running, 
  and the nodes are grouped to represent roads. Each nodes has its
  own collection of nodes that it is connected to allow navigating between
  multiple roads.
  The Breadth-First-Search path-finding algorithm is used to find a path 
  between the start and end node each time a new car is generated. This path is
  then stored for a cat to follow during the traffic flow simulation.
  <br> <br>
  Each second of the simulation is divided into 10 frames, and with each
  frame the positions of all active cars are updated. Each car comes from a
  specific generation rule, meaning that multiple cars will always have the
  same start and end point, however roughly half of the cars would have a
  mid-point assigned to them on their path which they must reach first before
  continuing to the final node. The Breadth-First-Search algorithm, or
  the BFS algorithm as it will be referred to further in this paper always 
  returns the shortest possible route for the cars. The division of some cars
  heading straight to the goal and others having a random, normally a lot
  longer path is done to ensure some cars have random and unique paths and
  to demonstrate that the path-finding and navigation do work as multiple
  cars will reach their goal and de-spawn.
  The maximum amount of cars that can exist at once is 50 - this ensures
  that the growth of program execution time proportional to increase in 
  simulation duration must is linear.
  <br> <br>
  In the author’s solution, cars do not interact with each other in any way,
  omitting traffic rules when it comes to intersections, pedestrian crossings
  and other traffic rules. There is no car collision detection. An important
  assumption must be done when analyzing the results of such a simulation - 
  each car behaves as if the roads are completely unregulated by traffic
  rules and are empty. However, road turns and basic physics of acceleration
  and deceleration are taken into account - Cars do detect future turns and
  manage their speed according to the situation.
  <br> <br>
  This module handles most of the inter-module communication, reading
  from the 3 files passed on from the first module, processing this data,
  and saving the simulation results into two files containing the same data:
  "framesVisual.csv" and "frames.json". They contain the same data, but in
  different formats. The structure of "framesVisual.csv" file is as it is 
  demonstrated in the figure below:
  <br> <br>
  ![Image](Readme_images\framesVis.png) 
  <br><br>

  <h3>Module 3 - Traffic flow animation</h3>
  Module 3 is simplistic, with the single purpose allowing user to view
  the simulation results by selecting a specific frame, and then looking at
  positions of all the cars on the map area during that specific time period of
  the simulation. For every frame number there is a collection of cars, all of
  which have positions defined by their latitude and longitude coordinates -
  the applications takes this data from the previously created file and displays
  cars as markers on the map. Module 3 User Interface is as it is demonstrated 
  on the Figure below:
  <br> <br>
  ![Image](Readme_images\DisplayResults.png)
  <br><br>

----

<h2>User manual</h2>
Requirements
1) Running a Windows operating system
2) Being connected to the internet
3) Having Python 3 installed and set as environment variable
4) Having Java Runtime Environment installed
5) Having a copy of the project locally on your device
6) Having a python virtual environment installed
7) Installing the following python packages:
<br>
(Versions are as used by the author)
<br>
geojson==3.0.1 <br>
tqdm==4.65.0 <br>
beautifulsoup4==4.12.0 <br>
requests==2.28.2 <br>
pandas==1.5.3 <br>
Kivy==2.1.0 <br>
kivy-deps.angle==0.3.3 <br>
kivy-deps.glew==0.3.1 <br>
kivy-deps.sdl2==0.4.5 <br>
Kivy-Garden==0.1.5 <br>
kivy-garden.mapview==1.0.6 <br>
kiwisolver==1.4.4 <br>
mapview==1.0.6 <br>
MarkupSafe==2.1.2 <br>
matplotlib==3.7.1 <br>
numpy==1.24.2 <br>
<h3>Running the program</h3>
After completing the list of requirements above, that is, 
copying the project on your Windows local device, initializing a 
python virtual environment and installing the needed packages
all that is left to do run the batch file "ExecuteApplication.bat" - the
application would then launch, executing each module one after another individually.
Alternatively, either of the modules can be ran using the command prompt.

----

<h3>User Interface</h3>
* __Functionality__
* __Implementation__
* __User manual for UI elements__

----

<h3>External elements</h3>
* __kivy__
* __osmtogeojson__
* __OpenStreetMaps__

----

<h3>Implementation approach</h3>
* __Selected languages, frameworks and data sources__
* __Use-cases__
* __Major algorithms description__
  * __Path finding__
  * __Car driving__
  * __File management__
  * __Map data transformation__
* __Internal data structures__
* __Commonly used formats__

----

<h3>Author's note</h3>
* __On future development__
* __Fair use__
* __Recommendations__


