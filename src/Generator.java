import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import org.json.simple.*;

public class Generator {
    public static ArrayList<Vehicle> Cars = new ArrayList<>();
    public static ArrayList<NodeGroup> NodeGroups = new ArrayList<>();
    public static ArrayList<DefaultNode> AllNodes = new ArrayList<>();
    public static ArrayList<GenerationRule> rules = new ArrayList<GenerationRule>();
    public static JSONArray frames = new JSONArray();
    public static JSONArray cars = new JSONArray();
    public static HashSet<Integer> vehicle_ids = new HashSet<Integer>();
    public static HashSet<Integer> node_ids = new HashSet<>();
    public static HashSet<Integer> node_graph_ids = new HashSet<>();
    public static SimulationProperties properties = new SimulationProperties();
    public static int num = 0;
    public static ArrayList<ArrayList<DefaultNode>> connectorNodeGroups = new ArrayList<>();
    public static ArrayList<Triplet> coordinatePairs = new ArrayList<>();

    public static ArrayList<Pair> coordinateUniqueCRCList = new ArrayList<>();

    public static ArrayList<Pair> OuterNodeIds = new ArrayList<>();

    public static double epochProgress = 0.0;

    public static int currentEpochTimer = 0;

    public static double epochProgressStep = 15.0;

    public static double totalIntensity = 0.0;

    public static ArrayList<String> randomizingList = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        CreateNodes();

        ConfigurationData configurationData = readConfig();
        createRandomizingList(configurationData);
        System.out.println(configurationData.toString());

        printMapInfo();

        processGenerationRulesData(configurationData);

        StartTimer(configurationData, 0);
    }

    //This method prints all existing nodes
    public static void printAllNodes(){
        for (DefaultNode allNode : AllNodes) {
            System.out.println("Latitude: " + allNode.latitude);
            System.out.println("Longitude : " + allNode.longitude);
            if (allNode instanceof InnerNode) {
                System.out.println("InnerNode");
                if (allNode.connections.size() == 0) {
                    System.out.println("0 Connection Inner node found");
                }
                System.out.println("Number of connections (Inner node): " + allNode.connections.size());
            } else {
                System.out.println("OuterNode");
                if (allNode.connections.size() == 0) {
                    System.out.println("0 Connection Outer node found");
                }
                int numberOfConnections = allNode.connections.size() + allNode.outer_connections.size();
                System.out.println("Number of connections (Outer node): " + numberOfConnections);
            }
        }
    }

    //This method prints general imported map info
    public static void printMapInfo(){
        System.out.println("Map setting complete.\n");
        System.out.println("Total number of nodes: " + AllNodes.size());
        System.out.println("Total number of connector nodes: " + getNumberOfOuterNodes());
        System.out.println("Total number of non-connector nodes: " + getNumberOfInnerNodes());
    }

    //This method returns the number of found outer/connector nodes
    public static int getNumberOfOuterNodes(){
        int number = 0;
        for (DefaultNode allNode : AllNodes) {
            if (allNode instanceof OuterNode) {
                number++;
            }
        }
        return number;
    }

    //This method returns the number of found inner/non-connector nodes
    public static int getNumberOfInnerNodes(){
        int number = 0;
        for (DefaultNode allNode : AllNodes) {
            if (allNode instanceof InnerNode) {
                number++;
            }
        }
        return number;
    }

    //This method check uniqueness of a vehicle id
    public static boolean IsIdUnique(int id){
        return !vehicle_ids.contains(id);
    }

    //This method check uniqueness of a node id
    public static boolean IsNodeIdUnique(int id){
        return !node_ids.contains(id);
    }



    //This method calculates "total intensity" from the multitude of generation rules defined by the user
    //It is later used when deciding on generation point for a car
    public static void processGenerationRulesData(ConfigurationData configurationData){
        for(int i = 0; i < configurationData.generationRules.size(); i++){
            totalIntensity = totalIntensity + configurationData.generationRules.get(i).intensity;
        }
    }

    //This method creates a list which will be used to randomly select a generation rule to execute
    public static void createRandomizingList(ConfigurationData configurationData){
        for(int i = 0; i < configurationData.generationRules.size(); i++){
            for(int j = 0; j <= configurationData.generationRules.get(i).intensity; j++){
                randomizingList.add(configurationData.generationRules.get(i).hash_id);
            }
        }
    }

    //This method reads the file created by module 1 which stores user-defined settings for the generation
    public static ConfigurationData readConfig() {
        try{
            Object object = new JSONParser().parse(new FileReader("files\\config.json"));
            JSONObject map = (JSONObject) object;
            JSONObject data = (JSONObject) map.get("data");

            //Identifiers
            JSONObject identifiers = (JSONObject) data.get("identifiers");
            String identifiers_name = (String) identifiers.get("name");
            String identifiers_hash = (String) identifiers.get("hash");

            //Simulation settings
            JSONObject simulation_settings = (JSONObject) data.get("simulation_settings");
            String simulation_settings_daytime = (String) simulation_settings.get("daytime");
            double simulation_settings_bottom_left_lat = (double) simulation_settings.get("bottom_left_lat");
            double simulation_settings_bottom_left_long = (double) simulation_settings.get("bottom_left_long");
            double simulation_settings_top_right_lat = (double) simulation_settings.get("top_right_lat");
            double simulation_settings_top_right_long = (double) simulation_settings.get("top_right_long");
            long duration = (long) simulation_settings.get("duration");

            //Generation rules
            JSONArray generationRules = (JSONArray) data.get("generation_rules");
            ArrayList<GenerationRule> generationRules1 = new ArrayList<>();
            for (Object rule : generationRules) {
                JSONObject generation_rule = (JSONObject) rule;
                String generation_rule_name = (String) generation_rule.get("name");
                double generation_rule_start_latitude = (double) generation_rule.get("start_lat");
                double generation_rule_start_longitude = (double) generation_rule.get("start_long");
                double generation_rule_end_latitude = (double) generation_rule.get("end_lat");
                double generation_rule_end_longitude = (double) generation_rule.get("end_long");
                boolean vans = (boolean) generation_rule.get("vans");
                boolean trucks = (boolean) generation_rule.get("trucks");
                boolean sedans = (boolean) generation_rule.get("sedans");
                long intensity1 = (long) generation_rule.get("intensity");
                int intensity = Math.toIntExact(intensity1);
                String hash_id = (String) generation_rule.get("hash_id");
                String startGroupId1 = (String) generation_rule.get("start_group_id");
                int startGroupId = Integer.parseInt(startGroupId1);
                String endGroupId1 = (String) generation_rule.get("end_group_id");
                int endGroupId = Integer.parseInt(endGroupId1);
                GenerationRule generationRule = new GenerationRule(generation_rule_name, generation_rule_start_latitude,
                        generation_rule_start_longitude, generation_rule_end_latitude, generation_rule_end_longitude,
                        vans, trucks, sedans, intensity, hash_id, startGroupId, endGroupId);
                generationRules1.add(generationRule);
            }

            return new ConfigurationData(identifiers_name, identifiers_hash,
                    simulation_settings_daytime, simulation_settings_bottom_left_lat,
                    simulation_settings_bottom_left_long, simulation_settings_top_right_lat,
                    simulation_settings_top_right_long, duration, generationRules1);

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ConfigurationData();
    }

    //This nodes navigates/assigns connections inside a single node group, assuming every street is two-way
    //TODO - must be modified when implementing two-way/one-way streets
    public static void NavigateNodeGroup(NodeGroup group){
        for(int i = 0; i < group.Nodes.size(); i++){
            if(i == group.Nodes.size()-1){
                //For last node in group
                ArrayList<Integer> connections = new ArrayList<>();
                connections.add(group.Nodes.get(i - 1).id);
                group.Nodes.get(i).setConnections(connections);
            }
            else if(i == 0){
                //For first node in group
                ArrayList<Integer> connections = new ArrayList<>();
                connections.add(group.Nodes.get(i + 1).id);
                group.Nodes.get(i).setConnections(connections);
            }
            else {
                //For all nodes in-between
                ArrayList<Integer> connections = new ArrayList<>();
                connections.add(group.Nodes.get(i + 1).id);
                connections.add(group.Nodes.get(i - 1).id);
                group.Nodes.get(i).setConnections(connections);
            }
        }
    }

    //This method calculates distance in meters between two (lat,long) points using the Haversine formula
    public static double CalculateDistanceInMeters(double la1, double lo1, double la2, double lo2){
        double radius_in_km = 6371;
        //Converting to radians
        double la_radians1 = Math.toRadians(la1);
        double lo_radians1 = Math.toRadians(lo1);
        double la_radians2 = Math.toRadians(la2);
        double lo_radians2 = Math.toRadians(lo2);
        //Calculating the difference
        double diff_la = la_radians2 - la_radians1;
        double diff_lo = lo_radians2 - lo_radians1;
        //Applying the formula to get the result in km
        double a = Math.pow(Math.sin(diff_la / 2), 2)
                + Math.cos(la_radians1) * Math.cos(la_radians2)
                * Math.pow(Math.sin(diff_lo / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return(c * radius_in_km)*1000;
    }

    //Given 3 (lat,long) points, this method calculates whether they form a turn or not
    public static boolean isTurnIncoming(double la1, double lo1, double la2, double lo2, double la3, double lo3){
        double distance1 = CalculateDistanceInMeters(la1, lo1, la3, lo3);
        double distance2 = CalculateDistanceInMeters(la1, lo1, la2, lo2) +
                CalculateDistanceInMeters(la2, lo2, la3, lo3);
        double difference = distance2/distance1;
        return difference > 1.1;
    }

    //For a given car path, this method calculates indexes of every pre-turn node
    public static ArrayList<Integer> GetPreTurnNodeIndexes(ArrayList<DefaultNode> path){
        ArrayList<Integer> preTurnNodes = new ArrayList<>();
        for(int i = 1; i < path.size()-2; i++){
            if(isTurnIncoming(path.get(i-1).latitude, path.get(i-1).longitude, path.get(i).latitude,
                    path.get(i).longitude, path.get(i+1).latitude, path.get(i+1).longitude)){
                preTurnNodes.add(i-1);
            }
        }
        return preTurnNodes;
    }

    //This method returns all connections for a given node (outer and regular)
    public static ArrayList<Integer> getAllConnections(DefaultNode node){
        ArrayList<Integer> connections = new ArrayList<>();
        if (node instanceof InnerNode){
            connections = node.getConnections();
        }
        else{
            connections = node.getConnections();
            for(int i = 0; i < node.outer_connections.size(); i++){
                connections.add(node.outer_connections.get(i).group_connection_node_id);
            }
        }
        return connections;
    }

    //This method finds the shortest path between two nodes using by calling the BFS algorithm method
    public static ArrayList<DefaultNode> GeneratePath(DefaultNode start_node, DefaultNode end_node){
        //If there is no possible path, the start node will remain the same but the end node will be assigned randomly
        ArrayList<DefaultNode> path = new ArrayList<>();
        path = breadthFirstSearch(start_node, end_node);
        if(path.size() < 2){
            System.out.println("No path can be found between the defined points, therefore assigning random path");
            for(int i = 0; i < AllNodes.size()-1; i++){
                Random random = new Random();
                int randomIndex = random.nextInt(AllNodes.size()-1);
                if(AllNodes.get(randomIndex) instanceof InnerNode){
                    return breadthFirstSearch(start_node, AllNodes.get(randomIndex));
                }
                else{
                    GeneratePath(start_node, end_node);
                }
            }
        }
        return path;
    }

    //This method performs Breadth-First-Search to find the shortest possible path between two nodes
    public static ArrayList<DefaultNode> breadthFirstSearch(DefaultNode source, DefaultNode target) {
        Queue<DefaultNode> queue = new LinkedList<>();
        ArrayList<DefaultNode> path = new ArrayList<>();
        queue.add(source);

        // A map to store the parent node for each visited node (to reconstruct the path later)
        java.util.HashMap<DefaultNode, DefaultNode> parentMap = new java.util.HashMap<>();
        parentMap.put(source, null);

        // Perform BFS
        while (!queue.isEmpty()) {
            DefaultNode current = queue.poll();
            if (current.equals(target)) {
                break; // Found the target node
            }

            ArrayList<Integer> connections = getAllConnections(current);
            for (Integer nodeId : connections) {
                DefaultNode adjacentNode = getNodeById(nodeId);
                if (adjacentNode != null && !parentMap.containsKey(adjacentNode)) {
                    queue.add(adjacentNode);
                    parentMap.put(adjacentNode, current);
                }
            }
        }
        DefaultNode current = target;
        while (current != null) {
            path.add(0, current); // Add each node to the beginning of the list
            current = parentMap.get(current);
        }

        return path;
    }

    //This is the method that generates a car according to defined generation rules
    public static void GenerateCar(DefaultNode start_node, DefaultNode end_node, GenerationRule rule){
        String carType = DecideOnCarType(rule.vans, rule.trucks, rule.sedans);
        ArrayList<DefaultNode> real_path = new ArrayList<>();
        real_path = GeneratePath(start_node, end_node);
        ArrayList<String> ChecksumValues = new ArrayList<>();
        ChecksumValues.add(carType);
        ChecksumValues.add(String.valueOf(Cars.size()));
        ChecksumValues.add(generateRandomString());
        int ChecksumId = calculateCheckSum(ChecksumValues);
        if (IsIdUnique(ChecksumId) && real_path.size() > 1) {
            //To keep track of last visited nodes
            ArrayList<Pair> distanceData = CalculateDistanceData(real_path);
            //For managing turns
            ArrayList<Integer> preTurnNodes = GetPreTurnNodeIndexes(real_path);
            double path_distance_in_m = 0;
            for (Pair distanceDatum : distanceData) {
                path_distance_in_m = path_distance_in_m + distanceDatum.mark;
            }
            switch (carType){
                case "Truck" -> {
                    Truck truck = new Truck(ChecksumId, real_path, 0, "Accelerate", 0, 60, 3.0,
                            real_path.get(0).latitude, real_path.get(0).longitude, 0, path_distance_in_m, distanceData,
                            preTurnNodes, 0.0, 0.0);
                    Cars.add(truck);
                    JSONObject new_car = new JSONObject();
                    new_car.put("id", truck.id);
                    new_car.put("name", "truck");
                    cars.add(new_car);
                }
                case "Van" -> {
                    Van van = new Van(ChecksumId, real_path, 0, "Accelerate", 0, 65, 4.0,
                            real_path.get(0).latitude, real_path.get(0).longitude, 0, path_distance_in_m, distanceData,
                            preTurnNodes, 0.0, 0.0);
                    Cars.add(van);
                    JSONObject new_car = new JSONObject();
                    new_car.put("id", van.id);
                    new_car.put("name", "van");
                    cars.add(new_car);
                }
                case "Sedan" ->{
                    Sedan sedan = new Sedan(ChecksumId, real_path, 0, "Accelerate", 0, 70, 4.5,
                            real_path.get(0).latitude, real_path.get(0).longitude, 0, path_distance_in_m, distanceData,
                            preTurnNodes, 0.0, 0.0);
                    Cars.add(sedan);
                    JSONObject new_car = new JSONObject();
                    new_car.put("id", sedan.id);
                    new_car.put("name", "sedan");
                    cars.add(new_car);
                }
            }
        }
    }

    //This method returns a randomly generated string to ensure random CRC32 id generation
    public static String generateRandomString(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(20);

        for (int i = 0; i < 20; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    //This method returns a node with a matching id
    public static DefaultNode getNodeById(int id){
        for (DefaultNode node : AllNodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    //This method pseudo-randomly selects a car type
    public static String DecideOnCarType(boolean vans, boolean trucks, boolean sedans){
        //If only one is true, method immediately returns
        if(vans && !trucks && !sedans){
            return "Van";
        }
        else if(!vans && trucks && !sedans){
            return "Truck";
        }
        else if(!vans && !trucks && sedans){
            return "Sedan";
        }
        ArrayList<String> randomizerList = new ArrayList<>();
        if(vans){
            randomizerList.add("Van");
        }
        if(trucks){
            randomizerList.add("Truck");
        }
        if(sedans){
            randomizerList.add("Sedan");
        }
        Random rand = new Random();
        int n = rand.nextInt(randomizerList.size());
        return randomizerList.get(n);
    }

    //This method manages the simulation, calling the 'main' method and car generator method when needed
    public static void StartTimer(ConfigurationData configurationData, int frame) throws Exception {
        //Assuming that for every iteration 1/10th of a second passes in a simulation
        int seconds = (int) (configurationData.duration)*60;
        try(FileWriter myWriter1 = new FileWriter("files\\framesVisual.csv", false)) {
            System.out.println("Clearing the results file before writing");
        }
        catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        for(int i = 0; i < seconds*10; i++){
            System.out.println("i: " + i);
            System.out.println("Seconds: " + seconds);
            System.out.println("Cars size: " + Cars.size());
            if(i == seconds-1){
                SaveJSON();
                return;
            }
            frame++;
            CalculateAllPositions(Cars, i, frame);
            //Epoch progress is here to decide when a new car is bound to be generated
            epochProgress = epochProgress + epochProgressStep;
            System.out.println("Epoch Progress: " + epochProgress);
            if(epochProgress >= 100.0){
                Random rand = new Random();
                currentEpochTimer = rand.nextInt(6) + 10;
                epochProgressStep = (100.0 / (double) currentEpochTimer);
                epochProgress = 0.0;
                ExecuteEpochGeneration(configurationData);
            }
        }
    }

    //This method creates a helper collection used for managing car position
    public static ArrayList<Pair> CalculateDistanceData(ArrayList<DefaultNode> path){
        ArrayList<Pair> distanceData = new ArrayList<>();
        double currentCoveredDistance = 0;
        Pair pair1 = new Pair(path.get(0).id, 0.0);
        distanceData.add(pair1);
        for(int i = 0; i < path.size()-1; i++){
            double la1 = path.get(i).latitude;
            double lo1 = path.get(i).longitude;
            double la2 = path.get(i+1).latitude;
            double lo2 = path.get(i+1).longitude;
            double distance = CalculateDistanceInMeters(la1, lo1, la2, lo2);
            currentCoveredDistance = currentCoveredDistance + distance;
            Pair pair = new Pair(path.get(i+1).id, currentCoveredDistance);
            distanceData.add(pair);
        }
        return distanceData;
    }

    //This method returns the distance left to next turn
    public static double getDistanceBeforeNearestTurnNode(Vehicle car){
        //find a minimum number from preTurnNodes
        int minNumber = getMinimumNumber(car.lastVisitedNodeIndexInPath, car.preTurnNodes);
        return CalculateDistanceInMeters(car.latitude, car.longitude, car.path.get(minNumber).latitude,
                car.path.get(minNumber).longitude);
    }

    //This method returns a node before the given turn
    public static int getMinimumNumber(int borderNumber, ArrayList<Integer> preTurnNodes){
        int min = Integer.MAX_VALUE;
        for (Integer preTurnNode : preTurnNodes) {
            if (preTurnNode < min) {
                if (min > borderNumber) {
                    min = preTurnNode;
                }
            }
        }
        return min;
    }

    //This method returns the last visited node index
    public static int getRelevantNodeIndexInPath(Vehicle car) throws Exception {
        if(car.getProgress_in_m() < car.distanceData.get(1).mark){
            return 0;
        }
        for(int i = 1; i < car.distanceData.size(); i++){
            if((car.distanceData.get(i-1).mark == car.distanceData.get(i).mark) && (car.distanceData.get(i-1).mark < car.getProgress_in_m())
            && (car.distanceData.get(i+1).mark) > car.getProgress_in_m()){
                return i;
            }
            else if(car.distanceData.get(i-1).mark < car.getProgress_in_m() && car.getProgress_in_m() < car.distanceData.get(i).mark){
                return i-1;
            }
        }
        return -1;
    }

    //This method returns a distance a car has travelled after last visited node
    public static double getRelevantProgressSinceLastNodeInM(Vehicle car) throws Exception{
        if(car.lastVisitedNodeIndexInPath == 0){
            return car.getProgress_in_m();
        }
        for(int i = 1; i < car.distanceData.size(); i++){
            if(car.distanceData.get(i-1).mark < car.getProgress_in_m() && car.getProgress_in_m() < car.distanceData.get(i).mark){
                double progress = car.getProgress_in_m()-car.distanceData.get(i-1).mark;
                if (progress < 0.0) {
                    throw new Exception("Progress should not be less than 0.0");
                }
                return car.getProgress_in_m()-car.distanceData.get(i).mark;
            }
        }
        throw new Exception("Critical error when locating car position");
    }

    //This method calculates car coordinates using bearing angle, previous coordinates and distance travelled
    public static Pair getRelevantCoordinates(Vehicle car){
        //Instead of doing Haversine, do the bearing calculation
        double latitude1 = Math.toRadians(car.path.get(car.lastVisitedNodeIndexInPath).latitude);
        double longitude1 = Math.toRadians(car.path.get(car.lastVisitedNodeIndexInPath).longitude);

        double bearing = getBearingAngle(car.path.get(car.lastVisitedNodeIndexInPath).latitude,
                car.path.get(car.lastVisitedNodeIndexInPath).longitude,
                car.path.get(car.lastVisitedNodeIndexInPath+1).latitude,
                car.path.get(car.lastVisitedNodeIndexInPath+1).longitude);

        double distanceInMeters = CalculateDistanceInMeters(car.path.get(car.lastVisitedNodeIndexInPath).latitude,
                car.path.get(car.lastVisitedNodeIndexInPath).longitude,
                car.path.get(car.lastVisitedNodeIndexInPath+1).latitude,
                car.path.get(car.lastVisitedNodeIndexInPath+1).longitude);

        double radius = 6371000;

        double newLatitude = Math.asin(Math.sin(latitude1) * Math.cos(distanceInMeters / radius) +
                Math.cos(latitude1) * Math.sin(distanceInMeters / radius) * Math.cos(bearing));
        double newLongitude = longitude1 + Math.atan2(Math.sin(bearing) * Math.sin(distanceInMeters / radius) * Math.cos(latitude1),
                Math.cos(distanceInMeters / radius) - Math.sin(latitude1) * Math.sin(newLatitude));

        // Convert back to degrees
        newLatitude = Math.toDegrees(newLatitude);
        newLongitude = Math.toDegrees(newLongitude);

        return new Pair(newLatitude, newLongitude);
    }

    //Hardcoded speed values, this method returns a car's relevant speed
    public static double getRelevantSpeed(Vehicle car){
        double converted_speed = car.speed/3.6;
        if(car.intent.equals("Accelerate")){
            //Final speed (in m/s) = Initial speed (in m/s) + (Acceleration rate * Time)
            if(car.speed >= 60.0 || 3.6*(converted_speed + (car.acceleration_rate*0.1)) >= 60){
                return 60.0;
            }
            else{
                return 3.6*(converted_speed + (car.acceleration_rate*0.1));
            }
        }
        else if (car.intent.equals("Slow down")){
            if(car.speed <= 30){
                return 30.0;
            }
            else{
                return 3.6*(converted_speed - (car.deceleration_rate*0.1));
            }
        }
        else{
            return car.speed;
        }
    }

    //This method is most important, it handles all car movement and more
    public static void CalculateAllPositions(ArrayList<Vehicle> Cars, int second, int frame) throws Exception {
        for(int i = 0; i < Cars.size(); i++){
            Vehicle car = Cars.get(i);
            if(car.distanceData.get(1).mark == 0.0){
                Cars.remove(car);
                break;
            }
            if(car.path.get(car.path.size()-2).id == car.path.get(car.lastVisitedNodeIndexInPath+1).id){
                System.out.println("Removing car");
                Cars.remove(car);
                break;
            }
            else{
                //Processing the data to catch up on relevant car status
                double distanceTravelled = CalculateDistanceTravelled(car.speed);
                car.setProgress_in_m(car.getProgress_in_m()+distanceTravelled);
                if(getRelevantNodeIndexInPath(car) != -1) {
                    car.setLastVisitedNodeIndexInPath(getRelevantNodeIndexInPath(car));
                }
                else{
                    System.out.println("Error - Removing car");
                    Cars.remove(car);
                    break;
                }
                if(car.lastVisitedNodeIndexInPath == car.path.size()-3){
                    Cars.remove(car);
                }
                car.setProgress_since_last_node_in_m(getRelevantProgressSinceLastNodeInM(car));

                Pair pair = new Pair();
                pair = getRelevantCoordinates(car);
                car.setLatitude(pair.latitude);
                car.setLongitude(pair.longitude);

                car.setAngle(getBearingAngle(car.path.get(car.lastVisitedNodeIndexInPath).latitude,
                        car.path.get(car.lastVisitedNodeIndexInPath).longitude,
                        car.path.get(car.lastVisitedNodeIndexInPath+1).latitude,
                        car.path.get(car.lastVisitedNodeIndexInPath+1).longitude));

                System.out.println("Car " + car.id + " travelled " + distanceTravelled + "m, amounting to " +
                        car.progress_in_m + "m out of " + car.distanceData.get(car.distanceData.size()-1).mark +
                        "m total with last visited node path index of " + car.lastVisitedNodeIndexInPath + " out of " +
                        car.distanceData.size());

                //Preparation for the next frame
                double distanceToNearestTurn = Double.MAX_VALUE;
                if(car.preTurnNodes.size()>0){
                    distanceToNearestTurn = getDistanceBeforeNearestTurnNode(car);
                }
                if(car.brakingDistance+10 > distanceToNearestTurn){
                    car.setIntent("Slow down");
                }
                else{
                    if(car.speed < 60){
                        car.setIntent("Accelerate");
                    }
                    else{
                        car.setIntent("Keep");
                    }
                }
                car.setSpeed(getRelevantSpeed(car));
            }
        }
        createPositionReport(second, frame);
        createPositionReportVisual(second, frame);
    }

    //This method returns a node with a matching id
    public static DefaultNode getNodeFromID(int id) throws Exception {
        for (DefaultNode allNode : AllNodes) {
            if (allNode.id == id) {
                return allNode;
            }
        }
        throw new Exception();
    }

    //This method returns the travelled distance based on speed
    public static double CalculateDistanceTravelled(double speed){
        //Speed parameter is in km/h
        //1St - calculate speed in km/s
        //By dividing the speed value by 3.6 we get m/s
        double converted_speed = ((double) speed/3.6);
        //Now, to calculate distance travelled in 1/10th of a sec
        return converted_speed/10;
    }

    //This method calculates a unique CRC32 value
    public static int calculateCheckSum(ArrayList<String> values){
        CRC32 crc32 = new CRC32();

        for (String variable : values) {
            crc32.update(variable.getBytes());
        }

        return (int) crc32.getValue();
    }

    public static boolean isCoordinateCRC32LatLongUnique(int value){
        for (Pair pair : coordinateUniqueCRCList) {
            if (pair.y == value) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCoordinateCRC32LatLongGroupUnique(int value){
        for (Pair pair : coordinateUniqueCRCList) {
            if (pair.x == value) {
                return false;
            }
        }
        return true;
    }

    public static int getLatLongGroupCRC32IdByLatLongCRC32Id(int value){
        //We get LatLongId, search for match(ONLY ONE MATCH POSSIBLE)
        for (Pair pair : coordinateUniqueCRCList) {
            if (pair.y == value) {
                return pair.x;
            }
        }
        return 0;
    }

    public static boolean isLatLongGroupIdInOuterNodes(int value){
        for (Pair outerNodeId : OuterNodeIds) {
            if (outerNodeId.x == value) {
                return true;
            }
        }
        return false;
    }

    //This method converts the map data into an internal structure of nodes
    public static void CreateNodes() throws Exception {
        //Every single node where a dynamic object may appear(A highway, a road, e.t.c.)
        //And its relation is to be organised in the node structure(Node and NodeGroup objects
        //which shall all exist in memory while the code is running)
        Object obj = new JSONParser().parse(new FileReader("files\\schema.geojson"));
        JSONObject map = (JSONObject) obj;
        JSONArray features = (JSONArray) map.get("features");
        for (Object current : features) {
            JSONObject curr = (JSONObject) current;
            JSONObject properties = (JSONObject) curr.get("properties");
            String str = (String) properties.get("highway");
            if (str != null) {
                if (str.equals("motorway") || str.equals("trunk") || str.equals("primary") || str.equals("secondary") ||
                        str.equals("tertiary") || str.equals("unclassified") || str.equals("residential") ||
                        str.equals("track") || str.equals("road") || str.equals("service") || str.equals("pedestrian") ||
                        str.equals("motorway_link") || str.equals("trunk_link") || str.equals("primary_link")) {
                    String group_id = (String) curr.get("id");
                    group_id = group_id.replaceAll("[^0-9]", "");
                    ArrayList<DefaultNode> group_nodes = new ArrayList<>();
                    NodeGroup group = new NodeGroup(group_id, str, group_nodes, 60);
                    JSONObject geometry = (JSONObject) ((JSONObject) current).get("geometry");
                    JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                    for (Object coordinate : coordinates) {
                        if (coordinate instanceof JSONArray) {
                            //getting lat and long coordinates
                            JSONArray lat_and_long = (JSONArray) coordinate;

                            // Generating a unique checksum id based on lat, long and group id of a node
                            String latitude = lat_and_long.get(0).toString();
                            String longitude = lat_and_long.get(1).toString();
                            ArrayList<String> ChecksumValues = new ArrayList<>();
                            ChecksumValues.add(latitude);
                            ChecksumValues.add(longitude);
                            ChecksumValues.add(group_id);
                            int ChecksumId = calculateCheckSum(ChecksumValues);

                            //Generating another unique id, for only for checking lat, long
                            ArrayList<String> ChecksumValues2 = new ArrayList<>();
                            ChecksumValues2.add(latitude);
                            ChecksumValues2.add(longitude);
                            int ChecksumId2 = calculateCheckSum(ChecksumValues2);

                            //coordinateUniqueCRCList is a list of nodes with unique lat, long coordinates
                            //OuterNodes is a list (transformed to a list of lists later) of nodes with
                            // unique lat,long,group hash values/combinations
                            // but the same lat long coordinates
                            if (isCoordinateCRC32LatLongUnique(ChecksumId2)) {
                                if (isCoordinateCRC32LatLongGroupUnique(ChecksumId)) {
                                    Pair pair = new Pair(ChecksumId, ChecksumId2);
                                    coordinateUniqueCRCList.add(pair);
                                } else {
                                    System.out.println("Critical error - detected 2 nodes from the same group, " +
                                            "with the same coordinates");
                                }
                            } else {
                                Pair pair = new Pair(ChecksumId, ChecksumId2);
                                OuterNodeIds.add(pair);
                                //We also need to add the initial node, after checking that its lat,long,group id is unique
                                int latLongGroupId = getLatLongGroupCRC32IdByLatLongCRC32Id(ChecksumId2);
                                if (!isLatLongGroupIdInOuterNodes(latLongGroupId)) {
                                    Pair pair2 = new Pair(latLongGroupId, ChecksumId2);
                                    OuterNodeIds.add(pair2);
                                }
                            }
                            //graph id = graph size + 1, always
                            int graph_id = node_graph_ids.size();
                            if (!IsNodeIdUnique(ChecksumId)) {
                                System.out.println("Critical error - detected 2 nodes from the same group, " +
                                        "with the same coordinates");
                            } else {
                                //CONNECTIONS AND GRAPH IDS are at first unpopulated
                                ArrayList<Integer> connections = new ArrayList<>();
                                InnerNode node = new InnerNode(ChecksumId, (double) lat_and_long.get(1),
                                        (double) lat_and_long.get(0), group_id, connections, graph_id);
                                node_ids.add(ChecksumId);
                                node_graph_ids.add(graph_id);
                                group_nodes.add(node);
                            }
                        }
                    }
                    group.setNodes(group_nodes);
                    NavigateNodeGroup(group);
                    NodeGroups.add(group);
                    AllNodes.addAll(group.Nodes);
                    System.out.println("Node count: ______ " + group_nodes.size());
                    SetOuterNodes();
                }
            }
        }
    }

    //This method searches for every outer node that was previously found
    public static void SetOuterNodes() throws Exception {
        //This method searches for every outer node that was previously found,
        //using OuterNodeIds collection that contains 2 identifiers - for nodes(0) and coordinates "hash"(1)
        //First step - count unique coordinate pairs to get the number of buckets

        ArrayList<Integer> UniqueLatLongCRC32Ids = new ArrayList<>();
        for (Pair outerNodeId : OuterNodeIds) {
            int coordinateID = outerNodeId.y;
            if (isCoordinateSetUnique(UniqueLatLongCRC32Ids, coordinateID)) {
                UniqueLatLongCRC32Ids.add(coordinateID);
            }
        }

        //After making the check above, we only need to sort OuterNodes to transform it to an array of arrays

        //This is for changing type
        for (Pair outerNodeId : OuterNodeIds) {
            int nodeId = outerNodeId.x;
            transformNodeTypeForAllCollections(nodeId);
        }

        //Now we need to set OuterConnections Attribute which was previously null
        //For each element of ArrayList<Pair>, (Or without creating such data structure) we need to create a search by
        // id the nodes from the same arraylist,
        //create a new arraylist representing connections and after that use the set method
        //String group_connection_id, int group_connection_node_id

        for (Pair outerNodeId : OuterNodeIds) {
            int latLongGroupId = outerNodeId.x;
            int latLongId = outerNodeId.y;
            ArrayList<Integer> connectionLatLongGroupIds = getConnectionIdsByLatLongId(latLongId);
            ArrayList<OuterConnection> outerConnections = getOuterConnectionsByLatLongGroupIds(connectionLatLongGroupIds);
            setOuterConnectionsForNodeForAllCollections(latLongGroupId, outerConnections);
        }

        //Now that we have a collection of buckets with each containing some number of
        //pairs(lat+long+group ID; lat+long ID), we can access the two collections that concern Inner Nodes and
        //transform the found InnerNodes to OuterNodes
        //NodeGroups -> group -> nodes
        //AllNodes -> nodes
    }

    public static ArrayList<OuterConnection> getOuterConnectionsByLatLongGroupIds(ArrayList<Integer> connectionLatLongGroupIds) throws Exception {
        ArrayList<OuterConnection> outerConnections = new ArrayList<>();
        for (int latLongGroupId : connectionLatLongGroupIds) {
            DefaultNode node = getNodeFromID(latLongGroupId);
            OuterConnection outerConnection = new OuterConnection(node.group_id, latLongGroupId);
            outerConnections.add(outerConnection);
        }
        return outerConnections;
    }

    public static void setOuterConnectionsForNodeForAllCollections(
            int nodeLatLongGroupId, ArrayList<OuterConnection> outerConnections) throws Exception {
        DefaultNode node = getNodeFromID(nodeLatLongGroupId);
        ArrayList<Integer> connections = node.getConnections();
        OuterNode outerNode = new OuterNode(node.id, node.latitude, node.longitude, node.group_id, outerConnections,
                connections, node.graph_id);
        replaceNodeInAllNodes(outerNode);
        replaceNodeInNodeGroups(outerNode);
    }

    public static ArrayList<Integer> getConnectionIdsByLatLongId(int latLongId){
        ArrayList<Integer> connections = new ArrayList<>();
        for (Pair outerNodeId : OuterNodeIds) {
            if (outerNodeId.y == latLongId) {
                connections.add(outerNodeId.x);
            }
        }
        return connections;
    }

    public static void transformNodeTypeForAllCollections(int id){
        DefaultNode node = getNodeById(id);
        if (node instanceof InnerNode){
            ArrayList<Integer> connections = node.getConnections();
            OuterNode outerNode = new OuterNode(id, node.latitude, node.longitude, node.group_id, null,
                    connections, node.graph_id);
            int numberOfCorrectionsAllNodes = replaceNodeInAllNodes(outerNode);
            int numberOfCorrectionsNodeGroups = replaceNodeInNodeGroups(outerNode);
            if (numberOfCorrectionsAllNodes != numberOfCorrectionsNodeGroups){
                System.out.println("Critical error - OuterNodes are not synchronised between collections");
            }
        }
    }

    public static int replaceNodeInAllNodes(OuterNode node){
        int number = 0;
        for(int i = 0; i < AllNodes.size(); i++){
            if(AllNodes.get(i).id == node.id){
                AllNodes.set(i, node);
                number++;
            }
        }
        return number;
    }

    public static int replaceNodeInNodeGroups(OuterNode node){
        int number = 0;
        for (NodeGroup nodeGroup : NodeGroups) {
            for (int j = 0; j < nodeGroup.getNodes().size(); j++) {
                if (nodeGroup.getNodes().get(j).id == node.id) {
                    nodeGroup.getNodes().set(j, node);
                    number++;
                }
            }
        }
        return number;
    }

    public static boolean isCoordinateSetUnique(ArrayList<Integer> set, int value){
        for (Integer integer : set) {
            if (integer == value) {
                return false;
            }
        }
        return true;
    }

    //borrowed method
    public static double getBearingAngle(double lat1, double lon1, double lat2, double lon2){
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(lon2 - lon1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    public static void ExecuteEpochGeneration(ConfigurationData configurationData) {
        if(Cars.size() < 50){
            int cars = (int) totalIntensity/2;
            for(int j = 0; j < cars; j++){
                Random rand = new Random();
                int size = randomizingList.size();
                int index = rand.nextInt(size-1);
                String chosenSourcePointHash = randomizingList.get(index);
                GenerationRule rule = getRuleByHash(chosenSourcePointHash, configurationData.generationRules);
                DefaultNode start_node = getNodeByLatLongGroup(rule.startLatitude, rule.startLongitude, rule.startGroupId);
                DefaultNode end_node = getNodeByLatLongGroup(rule.endLatitude, rule.endLongitude, rule.endGroupId);
                GenerateCar(start_node, end_node, rule);
            }
        }
        else{
            System.out.println("Too many cars to generate a new one");
        }
    }

    public static DefaultNode getNodeByLatLongGroup(double latitude, double longitude, long groupId){
        ArrayList<DefaultNode> InnerNodesCollection = new ArrayList<>();
        ArrayList<DefaultNode> OuterNodesCollection = new ArrayList<>();
        for (DefaultNode allNode : AllNodes) {
            if (allNode instanceof InnerNode && String.valueOf(groupId).equals(allNode.group_id)) {
                InnerNodesCollection.add(allNode);
            } else if (allNode instanceof OuterNode && String.valueOf(groupId).equals(allNode.group_id)) {
                OuterNodesCollection.add(allNode);
            }
        }
        for (DefaultNode defaultNode : InnerNodesCollection) {
            double latC = defaultNode.latitude;
            double longC = defaultNode.longitude;
            if (areCoordinatesMatched(latC, longC, latitude, longitude)) {
                return defaultNode;
            }
        }
        for (DefaultNode defaultNode : OuterNodesCollection) {
            double latC = defaultNode.latitude;
            double longC = defaultNode.longitude;
            if (areCoordinatesMatched(latC, longC, latitude, longitude)) {
                return defaultNode;
            }
        }
        return null;
    }

    public static boolean areCoordinatesMatched(double lat1, double long1, double lat2, double long2){
        ArrayList<Double> coordinates = new ArrayList<>();
        coordinates.add(lat1);
        coordinates.add(long1);
        coordinates.add(lat2);
        coordinates.add(long2);
        int minSymbolsNumber = getMinSymbolAmount(coordinates)-1;
        String latCFormatted = getSymbolsBeforeDot(lat1) + "." + getNSymbolsAfterDot(lat1, minSymbolsNumber);
        String longCFormatted = getSymbolsBeforeDot(long1) + "." + getNSymbolsAfterDot(long1, minSymbolsNumber);
        String latitudeMatch = getSymbolsBeforeDot(lat2) + "." + getNSymbolsAfterDot(lat2, minSymbolsNumber);
        String longitudeMatch = getSymbolsBeforeDot(long2) + "." + getNSymbolsAfterDot(long2, minSymbolsNumber);
        return latCFormatted.equals(latitudeMatch) && longCFormatted.equals(longitudeMatch);
    }

    public static int getMinSymbolAmount(ArrayList<Double> coordinates){
        int min = Integer.MAX_VALUE;
        for (Double coordinate : coordinates) {
            String numberString = Double.toString(coordinate);
            int decimalPlaces = numberString.length() - numberString.indexOf('.') - 1;
            if (decimalPlaces < min) {
                min = decimalPlaces;
            }
        }
        return min;
    }

    public static String getSymbolsBeforeDot(double input){
        String value = String.valueOf(input);
        int dotIndex = value.indexOf('.');
        if (dotIndex != -1) {
            return value.substring(0, dotIndex);
        }
        return value;  // If no dot is found, return the original string
    }

    public static String getNSymbolsAfterDot(double number, int n) {
        String numberS = String.valueOf(number);
        String regex = "\\.(\\d{" + n + "})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(numberS);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    public static GenerationRule getRuleByHash(String hash, ArrayList<GenerationRule> rules){
        for (GenerationRule rule : rules) {
            if (hash.equals(rule.hash_id)) {
                return rule;
            }
        }
        return new GenerationRule();
    }

    public static void WriteToJSON(Vehicle car, int second){
        JSONObject frameDetails = new JSONObject();
        frameDetails.put("car", car.id);
        frameDetails.put("lat", car.latitude);
        frameDetails.put("lon", car.longitude);
        frameDetails.put("angle", car.angle);
        JSONArray positions = new JSONArray();
        positions.add(frameDetails);
        JSONObject object = new JSONObject();
        object.put("positions", positions);
        JSONObject frame = new JSONObject();
        frame.put("frame", second);
        frame.put("positions", positions);
        frames.add(frame);
    }

    public static void createPositionReport(int second, int frame_number){
        JSONArray positions = new JSONArray();
        for (Vehicle car : Cars) {
            JSONObject frameDetails = new JSONObject();
            frameDetails.put("type", car.toString());
            frameDetails.put("lat", car.latitude);
            frameDetails.put("lon", car.longitude);
            frameDetails.put("angle", car.angle);
            positions.add(frameDetails);
        }
        JSONObject object = new JSONObject();
        object.put("positions", positions);
        JSONObject frame = new JSONObject();
        frame.put("frame", frame_number);
        frame.put("positions", positions);
        frames.add(frame);
    }

    public static void createPositionReportVisual(int second, int frame_number){
        String color = "";
        String name = "";
        for (Vehicle car : Cars) {
            JSONObject frameDetails = new JSONObject();
            if (car.toString().equals("Truck")) {
                color = "red";
                name = "Truck";
            } else if (car.toString().equals("Sedan")) {
                color = "blue";
                name = "Sedan";
            } else {
                color = "green";
                name = "Van";
            }
            try {
                FileWriter myWriter = new FileWriter("files\\framesVisual.csv", true);
                if (num == 0) {
                    String names = "lat,long,type,frame,angle" + "\n";
                    myWriter.write(names);
                    myWriter.close();
                }
                num++;
                String text = car.longitude + "," + car.latitude + "," + name + "," + frame_number +
                        "," + car.angle + "\n";
                System.out.println(text);
                myWriter.write(text);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public static void SaveJSON(){
        JSONObject total = new JSONObject();
        total.put("framerate", 10);
        JSONArray types = new JSONArray();
        JSONObject type1 = new JSONObject();
        type1.put("name", "truck");
        JSONObject type2 = new JSONObject();
        type2.put("name", "Sedan");
        JSONObject type3 = new JSONObject();
        type3.put("name", "Van");
        types.add(type1);
        types.add(type2);
        types.add(type3);
        total.put("types", types);
        total.put("frames", frames);
        try (FileWriter file = new FileWriter("files\\frames.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            System.out.println("Write");
            file.write(total.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
