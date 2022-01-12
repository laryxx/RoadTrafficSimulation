import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import org.json.simple.*;


public class Generator {

    public static ArrayList<Vehicle> Cars = new ArrayList<>();
    public static ArrayList<NodeGroup> NodeGroups = new ArrayList<>();
    public static ArrayList<DefaultNode> AllNodes = new ArrayList<>();
    public static ArrayList<GenerationRule> rules = new ArrayList<GenerationRule>();
    public static JSONArray frames = new JSONArray();
    public static JSONArray cars = new JSONArray();
    public static Graph graph = new Graph();
    public static HashSet<Integer> vehicle_ids = new HashSet<Integer>();
    public static HashSet<Integer> node_ids = new HashSet<>();
    public static HashSet<Integer> node_graph_ids = new HashSet<>();
    public static SimulationProperties properties = new SimulationProperties();
    public static ArrayList<Pair> gen_points_data = new ArrayList<Pair>();

    public static void main(String[] args) throws Exception {

        CreateNodes();
        PopulateGraph();
        printAllNodeGroups();

        ArrayList<DefaultNode> generation_points = new ArrayList<>();
        generation_points = ProcessGenerationPoints();

        for(int i = 0; i < generation_points.size(); i++){
            System.out.println(generation_points.get(i).id);
        }

        System.out.println("GEN Points size: " + generation_points.size());

        GenerationRulesGUI main = new GenerationRulesGUI(generation_points);
        main.showEvent();

    }

    public static void Start(ArrayList<GenerationRule> input_rules, SimulationProperties input_properties) throws Exception {
        properties = input_properties;
//        GenerationRule rule = new GenerationRule(0.3, 0.4, 0.3, GetNodeByGraphId(3), GetNodeByGraphId(40), 5);
//        rules.add(rule);
        rules.addAll(input_rules);
        WriteRulesToJSON();
        System.out.println("TOTAL NUMBER OF NODES ---------" + AllNodes.size());
        StartTimer(CalculateSimulationTime(), 0);
    }

    public static ArrayList<DefaultNode> ProcessGenerationPoints() throws IOException, ParseException {
        //Manual part
        //"../.data/maps/simple_map/generation_points.json"
        Object obj = new JSONParser().parse(new FileReader("../.data/maps/Map1/anim/configs/config1.json"));
        JSONObject map = (JSONObject) obj;
        JSONArray nodes = (JSONArray) map.get("nodes");
        ArrayList<DefaultNode> gen_points = new ArrayList<>();
        System.out.println(nodes.size());
        for(int i = 0; i < nodes.size(); i++) {
            JSONObject object = (JSONObject) nodes.get(i);
            String id = (String) object.get("id");
            String group_id = (String) object.get("group");
            int index = Integer.parseInt((String)object.get("index"));
            DefaultNode node = FindNodeByGroupAndIndex(group_id, index);
            assert node != null;
            Pair gen_id_and_node_id = new Pair(Integer.parseInt(id), node.id);
            gen_points_data.add(gen_id_and_node_id);
            gen_points.add(node);
        }
        return gen_points;
    }

    public static DefaultNode FindNodeByGroupAndIndex(String group_id, int index){
        for(int i = 0; i < NodeGroups.size(); i++){
            if(group_id.equals(NodeGroups.get(i).id)){
                System.out.println("-----GROUP FOUND");
                for(int j = 0; i < NodeGroups.get(i).Nodes.size(); j++){
                    if(j == index){
                        return NodeGroups.get(i).Nodes.get(j);
                    }
                }
            }
        }
        return null;
    }

    public static int ReturnJsonIdByNodeId(int node_id) throws Exception {
        for(int i = 0; i < gen_points_data.size(); i++){
            if(gen_points_data.get(i).y == node_id){
                return gen_points_data.get(i).x;
            }
        }
        throw new Exception("Error finding generation point");
    }

    public static int CalculateSimulationTime(){
        return (properties.end_time.getHour()*3600 + properties.end_time.getMinute()*60) -
                (properties.start_time.getHour()*3600 + properties.start_time.getMinute()*60);
    }

    public static DefaultNode GetNodeByGraphId(int id){
        for(int i = 0; i < AllNodes.size(); i++){
            if(AllNodes.get(i).graph_id == id){
                return AllNodes.get(i);
            }
        }
        return null;
    }

    public static void NavigateNodeGroup(NodeGroup group){
        for(int i = 0; i<group.Nodes.size(); i++){
            if(i == group.Nodes.size()-1){
                //TODO
//                ArrayList<OuterConnection> connections = new ArrayList<>();
//                OuterConnection connection = new OuterConnection(group.id,
//                        group.Nodes.get(0).id);
//                connections.add(connection);
//                group.Nodes.get(i).setOuter_connections(connections);
            }
            else {
                group.Nodes.get(i).setConnection_id(group.Nodes.get(i + 1).id);
            }
        }
        CaptureConnections();
    }

    public static void PopulateGraph(){
        //TODO
        ArrayList<Edge> edges2 = new ArrayList<>();
        for(int i = 0; i < AllNodes.size(); i++){
            if(AllNodes.get(i) instanceof OuterNode){
                if(AllNodes.get(i).outer_connections != null) {
                    for (int j = 0; j < AllNodes.get(i).outer_connections.size(); j++) {
                        edges2.add(new Edge(AllNodes.get(i).graph_id,
                                Objects.requireNonNull(GetNodeById(AllNodes.get(i).outer_connections.get(j).group_connection_node_id)).graph_id,
                                AllNodes.get(i).id, AllNodes.get(i).outer_connections.get(j).group_connection_node_id));
                    }
                }
            } else if(AllNodes.get(i) instanceof InnerNode){
                edges2.add(new Edge(AllNodes.get(i).graph_id,
                        Objects.requireNonNull(GetNodeById(AllNodes.get(i).connection_id)).graph_id,
                        AllNodes.get(i).id, AllNodes.get(i).connection_id));
            }
        }
        graph = new Graph(edges2, edges2.size()+10);
    }

    public static void WriteRulesToJSON() throws Exception {
        Object obj1 = new JSONParser().parse(new FileReader("../.data/maps/Map1/anim/configs/config1.json"));
        JSONObject map = (JSONObject) obj1;
        JSONArray nodes = (JSONArray) map.get("nodes");

        JSONObject rules_total = new JSONObject();
        JSONArray rules_list = new JSONArray();
        for(int i = 0; i < rules.size(); i++){
            JSONObject obj = new JSONObject();
            obj.put("source", ReturnJsonIdByNodeId(rules.get(i).source_node.id));
            obj.put("destination", ReturnJsonIdByNodeId(rules.get(i).destination_node.id));
            obj.put("intensity", rules.get(i).intensity);
            obj.put("trucks", rules.get(i).trucks);
            obj.put("vans", rules.get(i).vans);
            obj.put("sedans", rules.get(i).sedans);
            rules_list.add(obj);
        }
        rules_total.put("rules", rules_list);
        rules_total.put("nodes", nodes);
        try (FileWriter file = new FileWriter("../.data/maps/Map1/anim/configs/config1.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            System.out.println("WRITE RULES TO CONFIG");
            file.write(rules_total.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printAllNodeGroups(){
        System.out.println("TOTAL " + NodeGroups.size() + " NODEGROUPS");
        for(int i = 0; i < NodeGroups.size(); i++){
            NodeGroup group = NodeGroups.get(i);
            System.out.println("NODE GROUP ID: " + group.id);
            System.out.println("NUMBER OF NODES: " + group.Nodes.size());
            System.out.println("NODE GROUP TYPE: " + group.type);
        }
        printAllNodesInNodeGroup(NodeGroups.get(NodeGroups.size()-1));
        printAllNodesInNodeGroup(NodeGroups.get(NodeGroups.size()-2));
    }

    public static void printAllNodesInNodeGroup(NodeGroup group){
        for(int i = 0; i < group.Nodes.size(); i++){
            System.out.println("Node " + i + "id: " + group.Nodes.get(i).id);
            System.out.println("Type: " + group.Nodes.get(i).toString());
            System.out.println("Latitude: " + group.Nodes.get(i).latitude + "Longitude: " + group.Nodes.get(i).longitude);
            if(i < group.Nodes.size()-1) {
                System.out.println("Next node id: " + group.Nodes.get(i).connection_id);
                System.out.println("Distance to next: " + CalculateDistanceInKilometers(group.Nodes.get(i).latitude,
                        group.Nodes.get(i).longitude, group.Nodes.get(i + 1).latitude, group.Nodes.get(i + 1).longitude) * 100 + "Meters");
            }
        }
    }

    public static void saveRules(ArrayList<GenerationRule> rules1){
        System.out.println("Rules saved");
        rules = rules1;
        System.out.println("Rule size: " + rules.size());
    }

    public static ArrayList<DefaultNode> GetRealPathFromGraphPath(Stack<Integer> graph_path){
        ArrayList<DefaultNode> path = new ArrayList<>();
        for(int i = 0; i < graph_path.size(); i++){
            path.add(GetNodeByGraphId(graph_path.get(i)));
        }
        return path;
    }

    public static int GetRandomPointGraphID(int size){
        //TODO
        //Return a random point for traffic generation
        Random random = new Random();
        return random.nextInt(size);
    }

    public static double CalculateDistanceInKilometers(double la1, double lo1, double la2, double lo2){
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
        return(c * radius_in_km);
    }

    public static double CalculateSpeedPenaltyByAngle(double la1, double lo1, double la2, double lo2, double SumByParts){
        //An approximation
        double coefficient = 20.0/25.0;
        double straight_line_distance = CalculateDistanceInKilometers(la1, lo1, la2, lo2);
        double difference_in_kilometers = SumByParts - straight_line_distance;
        System.out.println("The difference in kilometers is: " + difference_in_kilometers + "..");
        double difference_in_proportion = SumByParts/straight_line_distance;
        System.out.println("The difference in proportion is: " + difference_in_proportion + "..");
        double penalty = (difference_in_proportion-1)*coefficient;
        return (difference_in_proportion-1)*coefficient*100;
    }

    public static boolean IsIdUnique(int id){
        if(vehicle_ids.contains(id)){
            return false;
        }
        return true;
    }

    public static boolean IsNodeIdUnique(int id){
        if(node_ids.contains(id)){
            return false;
        }
        return true;
    }

    public static boolean IsGraphIdUnique(int id){
        if(node_graph_ids.contains(id)){
            return false;
        }
        return true;
    }

    public static void TrueGenerateCar(GenerationRule rule) throws Exception {
        System.out.println("Car gen func called");
        ArrayList<DefaultNode> path = new ArrayList<>();
        Stack<Integer> stack_path = new Stack<Integer>();
        System.out.println("LIST SIZE__" + graph.adjacency_list.size() + " NODES___" + graph.number_of_nodes);
        stack_path = CalculateRandomPathMod(graph, graph.adjacency_list.size(), graph.number_of_nodes, rule.source_node.graph_id, rule.destination_node.graph_id);
        System.out.println("SIZE____" + stack_path.size());
        ArrayList<DefaultNode> real_path = new ArrayList<DefaultNode>();
        real_path = GetRealPathFromGraphPath(stack_path);
        System.out.println("PATH SIZE: " + stack_path.size() + " AND " + real_path.size());
        String type = DecideOnCarType(rule);
        switch (type) {
            case "Truck" -> {
                Random rand = new Random();
                int id = rand.nextInt(1000);
                if (IsIdUnique(id) && real_path.size() > 1) {
                    Truck truck = new Truck(id, rule.source_node.id, rule.destination_node.id, real_path, Intent.ACCELERATE,
                            0, CalculateDistanceInKilometers(real_path.get(0).latitude, real_path.get(0).longitude,
                            real_path.get(real_path.size() - 1).latitude, real_path.get(real_path.size() - 1).longitude),
                            rule.source_node.id, 0, 2500, CalculateDistanceInKilometers(real_path.get(0).latitude,
                            real_path.get(0).longitude, real_path.get(1).latitude, real_path.get(1).longitude),
                            0, 60,
                            Objects.requireNonNull(GetNodeGroupById(rule.source_node.group_id)).Nodes.size() - 1,
                            0, 3.0, rule.source_node.latitude, rule.source_node.longitude,
                            0);
                    Cars.add(truck);
                    JSONObject new_car = new JSONObject();
                    new_car.put("id", truck.id);
                    new_car.put("name", "truck");
                    cars.add(new_car);
                    System.out.println("Generated new car");
                } else {
                    TrueGenerateCar(rule);
                }
            }
            case "Van" -> {
                Random rand = new Random();
                int id = rand.nextInt(1000);
                if (IsIdUnique(id) && real_path.size() > 1) {
                    Van van = new Van(id, rule.source_node.id, rule.destination_node.id, real_path, Intent.ACCELERATE,
                            0, CalculateDistanceInKilometers(real_path.get(0).latitude, real_path.get(0).longitude,
                            real_path.get(real_path.size() - 1).latitude, real_path.get(real_path.size() - 1).longitude),
                            rule.source_node.id, 0, 2500, CalculateDistanceInKilometers(real_path.get(0).latitude,
                            real_path.get(0).longitude, real_path.get(1).latitude, real_path.get(1).longitude),
                            0, 65,
                            Objects.requireNonNull(GetNodeGroupById(rule.source_node.group_id)).Nodes.size() - 1,
                            0, 4.0, rule.source_node.latitude, rule.source_node.longitude,
                            0);
                    Cars.add(van);
                    JSONObject new_car = new JSONObject();
                    new_car.put("id", van.id);
                    new_car.put("name", "van");
                    cars.add(new_car);
                    System.out.println("Generated new car");
                } else {
                    TrueGenerateCar(rule);
                }
            }
            case "Sedan" -> {
                Random rand = new Random();
                int id = rand.nextInt(1000);
                if (IsIdUnique(id) && real_path.size() > 1) {
                    Sedan sedan = new Sedan(id, rule.source_node.id, rule.destination_node.id, real_path, Intent.ACCELERATE,
                            0, CalculateDistanceInKilometers(real_path.get(0).latitude, real_path.get(0).longitude,
                            real_path.get(real_path.size() - 1).latitude, real_path.get(real_path.size() - 1).longitude),
                            rule.source_node.id, 0, 2500, CalculateDistanceInKilometers(real_path.get(0).latitude,
                            real_path.get(0).longitude, real_path.get(1).latitude, real_path.get(1).longitude),
                            0, 70,
                            Objects.requireNonNull(GetNodeGroupById(rule.source_node.group_id)).Nodes.size() - 1,
                            0, 4.5, rule.source_node.latitude, rule.source_node.longitude,
                            0);
                    Cars.add(sedan);
                    JSONObject new_car = new JSONObject();
                    new_car.put("id", sedan.id);
                    new_car.put("name", "sedan");
                    cars.add(new_car);
                    System.out.println("Generated new car");
                } else {
                    TrueGenerateCar(rule);
                }
            }
            case "Error" -> {
                System.out.println("Error generating");
            }
        }
    }

    public static NodeGroup GetNodeGroupById(String id){
        for (int i = 0; i < NodeGroups.size(); i++){
            if (NodeGroups.get(i).id.equals(id)) {
                return NodeGroups.get(i);
            }
        }
        return null;
    }

    public static DefaultNode GetNodeById(int id){
        for(int i = 0; i < AllNodes.size(); i++){
            if (AllNodes.get(i).id == id){
                return AllNodes.get(i);
            }
        }
        return null;
    }

    public static String DecideOnCarType(GenerationRule rule){
        double Sum = rule.sedans + rule.vans + rule.trucks;
        double sedans_chance = (100*rule.sedans)/Sum;
        double vans_chance = (100*rule.vans)/Sum;
        double trucks_chance = (100*rule.trucks)/Sum;
        Random rand = new Random();
        int num = rand.nextInt(100)+1;
        if(num < sedans_chance){
            return "Sedan";
        }
        else if(sedans_chance < num && num < vans_chance){
            return "Van";
        }
        else if(num > vans_chance){
            return "Truck";
        }
        return "Error";
    }

    //Borrowed part code
    public static boolean CalculatePath(Graph graph, int src, int dest,
                                                   boolean[] discovered, Stack<Integer> path){
        // mark the current node as discovered
        discovered[src] = true;
        // include the current node in the path
        path.add(src);
        // if destination vertex is found
        if (src == dest) {
            return true;
        }
        // do for every edge (src, i)
        for (int i : graph.adjacency_list.get(src))
        {
            // if `u` is not yet discovered
            if (!discovered[i])
            {
                // return true if the destination is found
                if (CalculatePath(graph, i, dest, discovered, path)) {
                    return true;
                }
            }
        }
        // backtrack: remove the current node from the path
        path.pop();
        return false;
    }

    public static Stack<Integer> CalculateRandomPathMod(Graph graph, int total_number_of_edges, int total_number_of_nodes,
                                                        int source_graph_id, int destination_graph_id) throws Exception {
        boolean[] discovered = new boolean[total_number_of_edges*2];
        boolean[] discovered2 = new boolean[total_number_of_edges*2];
        Stack<Integer> path = new Stack<>();
        int mid_point_id = GetRandomPointGraphID(total_number_of_nodes);
        if(CalculatePath(graph, source_graph_id, destination_graph_id, discovered, path)){
            discovered = new boolean[discovered.length];
            Stack<Integer> path_copy = new Stack<Integer>();
            Stack<Integer> path_copy_2 = new Stack<Integer>();
            if(CalculatePath(graph, source_graph_id, mid_point_id, discovered, path_copy) &&
                    CalculatePath(graph, mid_point_id, destination_graph_id, discovered2, path_copy_2)){
                //ALL good -
                path_copy.pop();
                Stack<Integer> final_path = new Stack<>();
                final_path = path_copy;
                final_path.addAll(path_copy_2);
                System.out.println("Path exists from vertex " + source_graph_id + " to vertex " + mid_point_id +
                        " to vertex " + destination_graph_id);
                System.out.println("The complete path is " + path_copy);
                return final_path;
            }
            else{
                return CalculateRandomPathMod(graph, total_number_of_edges, total_number_of_nodes, source_graph_id, destination_graph_id);
            }
        }
        System.out.println("ERROR: " + mid_point_id);
        //No idea how this could be happening - even though the input doesn't change CalculatePath sometimes returns false
        return CalculateRandomPathMod(graph, total_number_of_edges, total_number_of_nodes, source_graph_id, mid_point_id);
    }

    public static void StartTimer(int seconds, int frame) throws Exception {
        //TODO
        //Assuming that for every iteration 1/10th of a second passes in a simulation
        for(int i = 0; i <seconds; i++){
            if(i == seconds-1){
                SaveJSON();
                return;
            }
            frame++;
            CalculateAllPositions(Cars, i, frame);
            DefineGeneration(i);
        }
    }

    public static Pair GetNextNodeInPathIdAndPositionInArr(ArrayList<DefaultNode> path, int last_visited_node_id) throws Exception {
        //For now, the complexity would be O(n) - but later a more efficient way to search
        //can be implemented - with use of this:
        //https://stackoverflow.com/questions/558978/most-efficient-way-to-see-if-an-arraylist-contains-an-object-in-java
        int node_position_in_arraylist = 0;
        for(int i = 0; i < path.size()-1; i++){
            if(path.get(i).id == last_visited_node_id){
                //1ST element is node Id, second is its position in array.
                return new Pair(path.get(i+1).id, i+1);
            }
        }
        throw new Exception("Error finding the next node");
    }

    public static void CalculateAllPositions(ArrayList<Vehicle> Cars, int second, int frame) throws Exception {
        for(int i = 0; i < Cars.size(); i++){
            //TODO
            Vehicle car = Cars.get(i);
            //Checking the distance travelled with the current speed
            double distance_travelled = CalculateDistanceTravelled(car.speed);
            //1. How much distance would the car cover with the current speed in 1/10th of a sec)
            car.setProgress_in_km(car.progress_in_km + distance_travelled);
            //Now, we check if the car has passed any nodes
            //Assuming that it is impossible to pass more than 1 node in
            //the given amount of time(currently 1/10 sec)
            if(car.progress_since_last_node_in_km >= car.distance_to_next_node)
            {
                //1.1 Checking if the car has reached it's destination
                //and deleting the car if that is so
                Pair pair = GetNextNodeInPathIdAndPositionInArr(car.path, car.last_visited_node_id);
                if(pair.y == car.path.size()-1){
                    System.out.println("CAR REMOVED ON " + second/10 + "'s SECOND");
                    Cars.remove(car);
                    //SaveJSON();
                }

                //1.1,5 Updating the sum of node distances for future use
                Pair pair2 = GetNextNodeInPathIdAndPositionInArr(car.path, car.last_visited_node_id);

                car.setSum_of_node_distances(car.sum_of_node_distances + CalculateDistanceInKilometers(
                        car.path.get(pair2.y-1).latitude, car.path.get(pair.y-1).longitude,
                        car.path.get(pair2.y).latitude, car.path.get(pair.y).longitude));

                //1.2 Setting the node that was the original next goal as the last visited node
                car.setLast_visited_node_id(pair.x);
                //1.3 Setting the progress from the last node (distance driven from the last visited node)
                //The whole distance - distance from start node to last visited node
                car.setProgress_since_last_node_in_km(car.progress_in_km - car.sum_of_node_distances);


                //1.4 Updating car distance to next node
                car.setDistance_to_next_node(CalculateDistanceInKilometers(car.path.get(pair.y).latitude,
                        car.path.get(pair.y).longitude, car.path.get(pair.y+1).latitude,
                        car.path.get(pair.y+1).longitude));

                //1.5 Checking if the node_group has changed and altering the fitting_speed
                //and group_nodes_left counter if so
                if(car.group_nodes_left == 1){
                    //Node group has changed, now fitting speed needs to be changed.
                    //So we search for the Current Nodegroup and extract information
                    //about the fitting speed and amount of nodes from there
                    //Later, Composite Id's for all nodes may be implemented.
                    //So that we'll know both node id and group node id just
                    //by one node_id that would look like that: 34_15
                    //For now, we'll just search for the nodegroup by id
                    Pair another_pair = GetFittingSpeedAndNodeCountByNodeGroupId(car.path.get(pair.y+1).group_id);
                    car.setGroup_nodes_left(another_pair.b);
                    car.setFitting_speed(another_pair.a);
                }
                car.setGroup_nodes_left(car.group_nodes_left-1);
            }
            else if(car.progress_since_last_node_in_km < car.distance_to_next_node){
                //1.1 Updating progress in distance since last node
                car.setProgress_since_last_node_in_km(car.progress_since_last_node_in_km + distance_travelled);
            }
            //Then, if Intent is to accelerate increase the speed by an appropriate value
            //If Intent is to brake, the speed shall be decreased.
            //If intent is to wait, speed is 0 and it holds still.
            //If the intent is to keep the speed, keep it
            if(car.speed >= car.fitting_speed){
                car.setIntent(Intent.KEEP);
            }
            else {
                car.setSpeed(ManageSpeed(car.intent, car.speed, car.fitting_speed, car.acceleration_rate));
            }
            //At last, we update the latitude and longitude parameters:
            DefaultNode carLastVisitedNode = getNodeFromID(car.getLast_visited_node_id());
            Pair pair = GetNextNodeInPathIdAndPositionInArr(car.path, car.last_visited_node_id);

            DefaultNode nextNode = getNodeFromID(pair.x);

            Pair pair3 = CalculatePointByDistanceAndBearing(carLastVisitedNode.latitude, carLastVisitedNode.longitude,
                    car.progress_since_last_node_in_km/6371.0,
                    Math.toRadians(bearing(carLastVisitedNode.latitude, carLastVisitedNode.longitude,
                            nextNode.latitude, nextNode.longitude)));

            car.setLatitude(pair3.latitude);
            car.setLongitude(pair3.longitude);

            car.setAngle(bearing(carLastVisitedNode.latitude, carLastVisitedNode.longitude,
                    nextNode.latitude, nextNode.longitude));

            //HERE
            //WriteToJSON(car, second);

            System.out.println("CAR LAT: " + pair3.latitude + " CAR LONG: " + pair3.longitude);
            //The last node parameter should be checked and the distance in km from the last visited node calculated.
            System.out.println("Last visited node: " + car.last_visited_node_id);
            System.out.println("Group nodes left: " + car.group_nodes_left);
            System.out.println("Distance to the next node is: " + (car.distance_to_next_node - car.progress_since_last_node_in_km));
            System.out.println("Initial distance to next node is: " + car.distance_to_next_node);
            System.out.println("Total Distance travelled: " + car.progress_in_km + " km\n");
            System.out.println("Sum of node distances: " + car.sum_of_node_distances);
        }
        createPositionReport(second, frame);
    }

    public static DefaultNode getNodeFromID(int id) throws Exception {
        for(int i = 0; i < AllNodes.size(); i++){
            if(AllNodes.get(i).id == id){
                return AllNodes.get(i);
            }
        }
        throw new Exception();
    }

    public static double ManageSpeed(Intent intent, double speed, double fitting_speed, double acceleration_rate){
        //The acceleration rate would be constant for now - 4m/s^2
        //So int 1/10s the the car would accelerate by (4/10)m/s
        double converted_acceleration_rate = acceleration_rate*0.36;
       if(intent == Intent.ACCELERATE){
           //(4/10)m/s^2 = 1.44 km/h/s
           return speed+converted_acceleration_rate;
       }
       else{
           return speed;
       }
    }

    public static Pair GetFittingSpeedAndNodeCountByNodeGroupId(String node_group_id) throws Exception {
        //For now, the complexity here would also be O(n),
        //but this implementation is subject for a change
        for(int i = 0; i < NodeGroups.size()-1; i++){
            if (NodeGroups.get(i).id.equals(node_group_id)) {
                return new Pair(NodeGroups.get(i).fitting_speed, NodeGroups.get(i).Nodes.size()-1);
            }
        }
        throw new Exception("Error when searching for a node group");
    }

    public static double CalculateDistanceTravelled(double speed){
        //Speed parameter is in km/h
        //1St - calculate speed in km/s
        //By dividing the speed value by 3.6 we get m/s
        double converted_speed = ((double) speed/3.6)/1000;
        System.out.println("Current speed: " + speed + " km/h");
        //Now, to calculate distance travelled in 1/10th of a sec
        return converted_speed/10;
    }

    public static void CreateNodes() throws IOException, ParseException {
        //"../.data/maps/simple_map/simple_map.geojson"
        //Every single node where a dynamic object may appear(A highway, a road, e.t.c.)
        //And its relation is to be organised in the node structure(Node and NodeGroup objects
        //which shall all exist in memory while the code is running)
        Object obj = new JSONParser().parse(new FileReader("../.data/maps/Map1/map.geojson"));
        JSONObject map = (JSONObject) obj;

        JSONArray features = (JSONArray) map.get("features");
        for (int i = 0; i < features.size(); i++) {
            Object current = features.get(i);
            JSONObject curr = (JSONObject) features.get(i);
            JSONObject properties = (JSONObject) curr.get("properties");
            System.out.println(properties);
            String str = (String) properties.get("highway");
            Random rand = new Random();
            System.out.println(str);
            if(str != null) {
                if (str.equals("motorway") || str.equals("trunk") || str.equals("primary") || str.equals("secondary") ||
                        str.equals("tertiary") || str.equals("unclassified") || str.equals("residential") ||
                str.equals("track") || str.equals("road")) {
                    String group_id = (String) curr.get("id");
                    //group_id = group_id.substring(4);
                    group_id = group_id.replaceAll("[^0-9]", "");
                    //int groupId = Integer.parseInt(group_id);
                    System.out.println("GROUP ID: " + group_id);
                    System.out.println("---------------" + str);
                    ArrayList<DefaultNode> group_nodes = new ArrayList<>();
                    NodeGroup group = new NodeGroup(group_id, str, group_nodes, 60);
                    //Object geometry = ((JSONObject) current).get("geometry");
                    JSONObject geometry = (JSONObject) ((JSONObject) current).get("geometry");
                    JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                    for (int j = 0; j < coordinates.size(); j++) {

                        System.out.println(coordinates.get(j));
                        if (coordinates.get(j) instanceof JSONArray) {
                            JSONArray lat_and_long = (JSONArray) coordinates.get(j);

                            System.out.println("lat: " + lat_and_long.get(0));
                            //The first and last nodes are outer nodes
                            //CONNECTIONS AND GRAPH IDS are at first unpopulated
                            if (j == coordinates.size() - 1) {
                                int id = rand.nextInt(50000) + 1;
                                int graph_id = node_graph_ids.size();
                                if (IsNodeIdUnique(id)) {
                                    OuterNode node = new OuterNode(id, (double) lat_and_long.get(0),
                                            (double) lat_and_long.get(1), group_id, null, graph_id);
                                    node_ids.add(id);
                                    node_graph_ids.add(graph_id);
                                    group_nodes.add(node);
                                    //AllNodes.add(node);
                                } else {
                                    OuterNode node = new OuterNode(rand.nextInt(50000) + 1,
                                            (double) lat_and_long.get(0), (double) lat_and_long.get(1), group_id,
                                            null, graph_id);
                                    node_ids.add(id);
                                    node_graph_ids.add(graph_id);
                                    group_nodes.add(node);
                                    //AllNodes.add(node);
                                }
                            }
                            //Other nodes shall be inner nodes
                            else {
                                int id = rand.nextInt(50000) + 1;
                                int graph_id = node_graph_ids.size();
                                if (IsNodeIdUnique(id)) {
                                    InnerNode node = new InnerNode(id, (double) lat_and_long.get(0), (double) lat_and_long.get(1), 0, group_id, graph_id);
                                    node_ids.add(id);
                                    node_graph_ids.add(graph_id);
                                    group_nodes.add(node);
                                    //AllNodes.add(node);
                                } else {
                                    InnerNode node = new InnerNode(id, (double) lat_and_long.get(0), (double) lat_and_long.get(1), 0, group_id, graph_id);
                                    node_ids.add(id);
                                    node_graph_ids.add(graph_id);
                                    group_nodes.add(node);
                                    //AllNodes.add(node);
                                }
                            }
                        }
                    }
                    group.setNodes(group_nodes);
                    NavigateNodeGroup(group);
                    NodeGroups.add(group);
                    AllNodes.addAll(group.Nodes);
                    System.out.println("Node count: ______ " + group_nodes.size());
                }
            }
        }
    }


    //borrowed method
    protected static double bearing(double lat1, double lon1, double lat2, double lon2){
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(lon2 - lon1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    //borrowed method
    public static Pair CalculatePointByDistanceAndBearing(double lat1, double lon1, double dist, double brng){
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) + Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
        double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1), Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
        double lon2 = lon1 + a;
        lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;
        return new Pair(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    public static void DefineGeneration(double second) throws Exception {
        int rules_total = rules.size();

        rules.sort(Comparator.comparing(GenerationRule::getIntensity));
        Collections.reverse(rules);

        if (second%1 == 0){
            System.out.println("Second: " + second/10);
            System.out.println("Cars generated: " + Cars.size());
            System.out.println("Rules size: " + rules_total);
            //We have to generate n cars. What we do is select a rule
            //and pass it to another method for processing n times
            Random rand = new Random();
            int n = rand.nextInt(rules_total)+1;
            //System.out.println("generated number: " + n);
            for(int i = 0; i <=n; i++){
                System.out.println("I'm here");
                GenerationRule rule = selectRule();
                TrueGenerateCar(rule);
            }
        }
    }

    public static GenerationRule selectRule(){
        //We select a number between 1 and 10 BUT
        //It tends to be lower(Meaning that the
        // index is smaller and a rule with a bigger intensity is more likely to be chosen)
        if(rules.size() > 1) {
            int num = (int) (Math.pow(Math.floor(Math.random() * 10), 2)) / (100 / rules.size());
            return rules.get(num);
        }
        else{
            return rules.get(0);
        }
    }

    public static void WriteToJSON(Vehicle car, int second){
        JSONObject frameDetails = new JSONObject();

        frameDetails.put("car", car.id);
        frameDetails.put("lat", car.latitude);
        frameDetails.put("lon", car.longitude);
        frameDetails.put("angle", car.angle);

        JSONArray positions = new JSONArray();
        //positions.add("frame: " + second);
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
        for(int i = 0; i < Cars.size(); i++){
            JSONObject frameDetails = new JSONObject();
            frameDetails.put("type", Cars.get(i).toString());
            frameDetails.put("lat", Cars.get(i).latitude);
            frameDetails.put("lon", Cars.get(i).longitude);
            frameDetails.put("angle", Cars.get(i).angle);
            positions.add(frameDetails);
        }
        JSONObject object = new JSONObject();
        object.put("positions", positions);
        JSONObject frame = new JSONObject();
        frame.put("frame", frame_number);
        frame.put("positions", positions);
        frames.add(frame);
    }

    public static void CaptureConnections(){
        for(int i = 0; i < NodeGroups.size(); i++){
            Pair coordinates = new Pair(NodeGroups.get(i).Nodes.get(NodeGroups.get(i).Nodes.size()-1).latitude,
                    NodeGroups.get(i).Nodes.get(NodeGroups.get(i).Nodes.size()-1).longitude);
            ArrayList<OuterConnection> outer_conn = new ArrayList<OuterConnection>();
            //outer_conn = NodeGroups.get(i).Nodes.get(NodeGroups.get(i).Nodes.size()-1).outer_connections;
            //Now we check fot connections
            for(int j = 0; j < AllNodes.size(); j++){
                if(AllNodes.get(j) instanceof InnerNode && AllNodes.get(j).latitude == coordinates.latitude &&
                        AllNodes.get(j).longitude == coordinates.longitude){
                    outer_conn.add(new OuterConnection(AllNodes.get(j).group_id, AllNodes.get(j).id));
                    System.out.println("Connection added");
                }
            }
            NodeGroups.get(i).Nodes.get(NodeGroups.get(i).Nodes.size()-1).setOuter_connections(outer_conn);
        }
    }
    

    public static void SaveJSON(){
        //"../.data/maps/simple_map/anim/frames.json"
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
        try (FileWriter file = new FileWriter("../.data/maps/Map1/anim/out/frames.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            System.out.println("Write");
            file.write(total.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
