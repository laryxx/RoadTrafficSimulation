<h1>Traffic flow simulation project</h1>

----

<h2>Program architecture</h2> 
<h3> Module 1 - Defining the simulation </h3>
  Module 1 defines essential data for the simulation. It fetches the map from OpenStreetMap, allowing the user to select start and end coordinates, car generation rules, and car types. Any map region with roads can be chosen.
  <br> <br>
  Upon defining the data, users can start the process by clicking 'Start.' This fetches the map file from the web service for use in the next module.
  <br> <br>
  Temporary files facilitate communication between modules. Module 1 generates three files: '.osm' and '.geojson' map files containing road information, and a configuration file with user-defined rules and simulation properties.
  <br> <br>
  Data in Module 1 falls into two categories: car generation rules and simulation details. Simulation details include map bounds, duration (in minutes), and daytime settings for potential future use. Generation rules have attributes like name, hash ID for module reference, and start/end group ID for node collections in map files. Other attributes include start/end coordinates, car types (vans, trucks, sedans), and intensity (1-5 for frequency). Module 1 User Interface is as it is demonstrated below:
  <br> <br>
  <picture>
  <img src="https://raw.githubusercontent.com/laryxx/RoadTrafficSimulation/master/Readme_images/module1UI.png"/>
  </picture>
  <br><br>

  <h3>Module 2 - Traffic flow simulation</h3>
  Module 2 is the simulation backend. It processes data from the previous module and runs the simulation. Results are stored in temporary ".csv" and ".json" files.
  <br> <br>
  Map data from the previous module is transformed to capture road connections for navigation. Each node represents a coordinate. The Breadth-First-Search algorithm finds paths between start and end nodes for cars, updating positions in 10 frames per second.
  <br> <br>
  Cars follow paths, with some taking random routes to demonstrate path-finding. Up to 50 cars can exist simultaneously.
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
  <picture>
  <img src="https://raw.githubusercontent.com/laryxx/RoadTrafficSimulation/master/Readme_images/framesVis.png"/>
  </picture>
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
  <picture>
  <img src="https://raw.githubusercontent.com/laryxx/RoadTrafficSimulation/master/Readme_images/DisplayResults.png"/>
  </picture>
  <br><br>
  <br><br>

----

<h2>User manual</h2>
<h3>Requirements</h3>
1) Running a Windows operating system; <br>
2) Being connected to the internet; <br>
3) Having Python 3 installed and set as environment variable; <br>
4) Having Java Runtime Environment installed; <br>
5) Having a copy of the project locally on your device; <br>
6) Having a python virtual environment installed; <br>
7) [Having osmtogeojson](https://github.com/tyrasd/osmtogeojson) installed and set as environment variable; <br>
8) Installing the following python packages: 
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

<h3>Demonstration - part 1</h3>
This video shows the functioning of module 1:

<video controls>
  <source src="https://raw.githubusercontent.com/laryxx/RoadTrafficSimulation/master/Readme_images/part1.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

<h3>Demonstration - part 2</h3>
This video shows the functioning of module 2:

<video controls>
  <source src="https://raw.githubusercontent.com/laryxx/RoadTrafficSimulation/master/Readme_images/part2.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

----

<h2>User Interface</h2>
<h3>Functionality</h3>
The author's UI requirements were simplicity and functionality. This application has a two-module GUI: one defines simulation properties, and the other displays results. Module 1 features controls for defining generation rules on the left and simulation property settings above. The 'Start' button initiates the second module. The map is interactive, with clickable start and finish markers.
<h3>Implementation</h3>
All the existing Graphical user interface is done using kivy framework with python. It was chosen by the author because it fit the program’s needs better than some other popular libraries for creating any sort of user interface applications in python. It also has a built-in option for displaying an OpenStreetMap map as an element of the user interface which has proven to be very convenient for the purposes of authors' application.
<h3>User manual for UI elements</h3>
The breakdown of UI elements in module 1 is as it is demonstrated below:
<br> <br>
<picture>
<img src="https://raw.githubusercontent.com/laryxx/RoadTrafficSimulation/master/Readme_images/module1UIBreakdown.png"/>
</picture>
<br><br> <br> <br>
The breakdown of UI elements in module 3 is as it is demonstrated below:
<br> <br>
<picture>
<img src="https://raw.githubusercontent.com/laryxx/RoadTrafficSimulation/master/Readme_images/module3UIBreakdown.png"/>
</picture>
<br><br>
----

<h3>External elements</h3>
[Kivy Framework](https://kivy.org/) 
<br>
Kivy is an open-source Python library for developing multitouch applications. We have used Kivy to build the user interface of this program. To learn more about Kivy and to support the project, please visit their [official website](https://kivy.org/). 
<br> <br>
[osmtogeojson](https://github.com/tyrasd/osmtogeojson) 
<br>
osmtogeojson is a Javascript module for converting OSM data (OSM XML or Overpass JSON) to GeoJSON. It works in the browser, nodejs and can also be used as a command line tool. Learn more by visiting the [website](https://tyrasd.github.io/osmtogeojson/) or the [GitHub page](https://github.com/tyrasd/osmtogeojson).
<br> <br>
[OpenStreetMap](https://www.openstreetmap.org/) <br>
OSM is an open-source mapping platform that provides detailed and freely accessible geographic information. To learn more about OpenStreetMap and contribute to their community, please visit their [official website](https://www.openstreetmap.org/). 
<br> 

----


<h2>Author's note</h2>
<h3>On future development</h3>
GUI Enhancement: The graphical user interface (GUI) could be improved both aesthetically and functionally. This includes offering more options for car generation, frame rate control, and potentially automating map selection.

Enhanced Car Routing: Continuous improvements to the car routing algorithm and the internal map representation algorithm are crucial to reducing program crashes, given the vast range of possible test maps.

Realism in Vehicle Movement: A priority for program expansion is enhancing the realism of vehicle movement. This could involve factors like vehicle weight affecting acceleration, different speed zones, importing traffic rules from maps, and more, to create a more realistic simulation.

These enhancements can contribute to the application's functionality and usability.

Aside from third-party components, the author's solution is original and the present license provides the opportunity to fairly use and enhance the program further.




