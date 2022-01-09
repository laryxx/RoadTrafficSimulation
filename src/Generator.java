import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.locationtech.spatial4j.shape.Point;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void main(String[] args) throws Exception {
        double la1 = 14.3811433;
        double la2 = 14.3810343;
        double la3 = 14.3814057;
        double la4 = 14.3819432;
        double la5 = 14.382381;
        double lo1 = 49.9691511;
        double lo2 = 49.9690489;
        double lo3 = 49.968503;
        double lo4 = 49.967735;
        double lo5 = 49.9671378;
        double distance1 = CalculateDistanceInKilometers(la1, lo1, la2, lo2);
        double distance2 = CalculateDistanceInKilometers(la2, lo2, la3, lo3);
        double distance3 = CalculateDistanceInKilometers(la3, lo3, la4, lo4);
        double distance4 = CalculateDistanceInKilometers(la4, lo4, la5, lo5);
        double Sum = distance1 + distance2 + distance3 + distance4;
        ArrayList<DefaultNode> nodeGroup1List = new ArrayList<DefaultNode>(5);
        InnerNode node1 = new InnerNode(1, la1, lo1, 2, "1", 0);
        InnerNode node2 = new InnerNode(2, la2, lo2, 3, "1", 1);
        InnerNode node3 = new InnerNode(3, la3, lo3, 4, "1", 2);
        InnerNode node4 = new InnerNode(4, la4, lo4, 5, "1", 3);
        EndNode node5 = new EndNode(5, la5, lo5, "1", 4);
        nodeGroup1List.add(node1);
        nodeGroup1List.add(node2);
        nodeGroup1List.add(node3);
        nodeGroup1List.add(node4);
        nodeGroup1List.add(node5);
        NodeGroup nodeGroup1 = new NodeGroup("1", "highway", nodeGroup1List, 60);
        nodeGroup1.setFitting_speed(nodeGroup1.fitting_speed - CalculateSpeedPenaltyByAngle(
                nodeGroup1.getNodes().get(0).latitude, nodeGroup1.getNodes().get(0).longitude,
                nodeGroup1.getNodes().get(nodeGroup1.getNodes().size()-1).latitude,
                nodeGroup1.getNodes().get(nodeGroup1.getNodes().size()-1).longitude, Sum));
        NodeGroups.add(nodeGroup1);
        AllNodes.add(node1);
        AllNodes.add(node2);
        AllNodes.add(node3);
        AllNodes.add(node4);
        AllNodes.add(node5);

        GenerationRulesGUI main = new GenerationRulesGUI(AllNodes);
        main.showEvent();


//        System.out.println("TEST PATH CALCULATION");
//
//                List<Edge> edges = Arrays.asList(new Edge(0, 1, 0, 0),
//                new Edge(1, 2, 0, 0), new Edge(1, 3, 0, 0),
//                new Edge(1, 4, 0, 0), new Edge(2, 11, 0, 0),
//                new Edge(11, 12, 0, 0), new Edge(12, 3, 0, 0),
//                new Edge(3, 12, 0, 0), new Edge(12, 8, 0, 0),
//                new Edge(8, 3, 0, 0), new Edge(3, 8, 0, 0),
//                new Edge(8, 4, 0, 0), new Edge(4, 5, 0, 0),
//                new Edge(5, 6, 0, 0), new Edge(6, 10, 0, 0),
//                new Edge(6, 7, 0, 0), new Edge(7, 6, 0, 0),
//                new Edge(7, 8, 0, 0));
//
//
//        graph = new Graph(edges, edges.size());
//        Stack<Integer> path = CalculateRandomPathMod(graph, graph.adjacency_list.size(), graph.number_of_nodes, 0,  10);
//        System.out.println("The complete path is " + path);


    }


    public static void Start(ArrayList<GenerationRule> input_rules) throws Exception {

//        rules = input_rules;
//
//        System.out.println("\n-------NEW RUN--------\n");
//
//        ArrayList<Edge> edges2 = new ArrayList<>();
//
//        edges2.add(new Edge(0,1,1,2));
//        edges2.add(new Edge(1,2,2,3));
//        edges2.add(new Edge(2,3,3,4));
//        edges2.add(new Edge(3,4,4,5));
//
//        Graph graph2 = new Graph(edges2, 4);
//        graph = graph2;

//        ----------------

//        Stack<Integer> path2 = CalculateRandomPath(graph2, 5, 5, 0,  4);
//        System.out.println("The complete path is " + path2);
//
//        ArrayList<DefaultNode> real_path = getRealPathFromGraphPath(path2);
//        for(int i = 0; i < real_path.size(); i++){
//            System.out.println(real_path.get(i).graph_id + " AND " + real_path.get(i).id);
//        }
//
//        GenerateCar(real_path);

        CreateNodes();
        PopulateGraph();
        printAllNodeGroups();

        //One-time thing
//        double distance = CalculateDistanceInKilometers(1.2055676, 46.3978279, 1.2276719, 46.3961438);
//        double distance2 = CalculateDistanceInKilometers(1.2055676, 46.3978279, 1.2285784, 46.4002829);
//        double distance3 = CalculateDistanceInKilometers(1.2276719, 46.3961438, 1.2276719, 46.3961438);
//        System.out.println("DISTANCE BETWEEN NODEGROUPS1: " + distance*1000);
//        System.out.println("DISTANCE BETWEEN NODEGROUPS2: " + distance2*1000);
//        System.out.println("DISTANCE BETWEEN NODEGROUPS3: " + distance3*1000);
        double unclass_end_lat = NodeGroups.get(NodeGroups.size()-2).Nodes.get(NodeGroups.get(NodeGroups.size()-2).Nodes.size()-1).latitude;
        double unclass_end_long = NodeGroups.get(NodeGroups.size()-2).Nodes.get(NodeGroups.get(NodeGroups.size()-2).Nodes.size()-1).longitude;

        double track_start_lat = NodeGroups.get(NodeGroups.size()-1).Nodes.get(0).latitude;
        double track_start_long = NodeGroups.get(NodeGroups.size()-1).Nodes.get(0).longitude;

        double dist = CalculateDistanceInKilometers(unclass_end_lat, unclass_end_long, track_start_lat, track_start_long);
        System.out.println("Distance: " + dist);
        //StartTimer(10, 0);

        //Manual creation of a graph + populating it with a specific nodegroup nodes.
        System.out.println(node_graph_ids);
//        Stack<Integer> path = CalculateRandomPathMod(graph, graph.adjacency_list.size(), graph.number_of_nodes, 0, 10);
//        ArrayList<DefaultNode> real_path = GetRealPathFromGraphPath(path);
        GenerationRule rule = new GenerationRule(0.3, 0.4, 0.3, GetNodeByGraphId(3), GetNodeByGraphId(40), 5);
        rules.add(rule);
        System.out.println("TOTAL NUMBER OF NODES ---------" + AllNodes.size());
        StartTimer(100, 0);

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
        //TODO
        for(int i = 0; i<group.Nodes.size(); i++){
            if(i == group.Nodes.size()-1){
                //Manual setting
                ArrayList<OuterConnection> connections = new ArrayList<>();
                OuterConnection connection = new OuterConnection(NodeGroups.get(NodeGroups.size()-1).id,
                        NodeGroups.get(NodeGroups.size()-1).Nodes.get(0).id);
                connections.add(connection);
                group.Nodes.get(i).setOuter_connections(connections);
            }
            else {
                group.Nodes.get(i).setConnection_id(group.Nodes.get(i + 1).id);
            }
        }
    }

    public static void PopulateGraph(){
        //TODO
        ArrayList<Edge> edges2 = new ArrayList<>();
        for(int i = 0; i < AllNodes.size(); i++){
            if(AllNodes.get(i) instanceof OuterNode){
                for(int j = 0; j < AllNodes.get(i).outer_connections.size(); j++){
                    edges2.add(new Edge(AllNodes.get(i).graph_id,
                            Objects.requireNonNull(GetNodeById(AllNodes.get(i).outer_connections.get(j).group_connection_node_id)).graph_id,
                            AllNodes.get(i).id, AllNodes.get(i).outer_connections.get(j).group_connection_node_id));
                }
            } else if(AllNodes.get(i) instanceof InnerNode){
                edges2.add(new Edge(AllNodes.get(i).graph_id,
                        Objects.requireNonNull(GetNodeById(AllNodes.get(i).connection_id)).graph_id,
                        AllNodes.get(i).id, AllNodes.get(i).connection_id));
            }
        }
        graph = new Graph(edges2, edges2.size());
    }

    public static void testPrintPath(){

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

//    public static ArrayList<DefaultNode> GetRealPathFromGraphPath(Stack<Integer> graph_path){
//        ArrayList<DefaultNode> path = new ArrayList<>();
//        for(int i = 0; i < graph_path.size(); i++){
//            if(graph_path.contains(AllNodes.get(i).graph_id)){
//                path.add(graph_path.get(AllNodes.get(i).graph_id), AllNodes.get(i));
//            }
//        }
//        return path;
//    }

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

    public static void GenerateCar(ArrayList<DefaultNode> path){
        //ID's should be generate automatically, but for now we'll leave it this way.
        //We also assume that the path has already been calculated.
        Truck truck1 = new Truck(1, 1, 5, path, Intent.ACCELERATE, 0,
                CalculateDistanceInKilometers(path.get(0).latitude, path.get(0).longitude,
                        path.get(path.size()-1).latitude, path.get(path.size()-1).longitude),
                1, 0, 2500, CalculateDistanceInKilometers(path.get(0).latitude,
                path.get(0).longitude, path.get(1).latitude, path.get(1).longitude), 0,
                NodeGroups.get(0).fitting_speed, NodeGroups.get(0).Nodes.size()-1, 0,
                4.0, 14.3811433, 49.9691511, 0);
        Cars.add(truck1);
        //TODO
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
        String type = DecideOnCarType(rule);
        switch (type) {
            case "Truck" -> {
                Random rand = new Random();
                int id = rand.nextInt(1000);
                if (IsIdUnique(id)) {
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
                if (IsIdUnique(id)) {
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
                if (IsIdUnique(id)) {
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

    public static Stack<Integer> CalculateRandomPath(Graph graph, int total_number_of_edges, int total_number_of_nodes, int source_graph_id, int destination_graph_id){
        //to keep track of whether a vertex is discovered or not
        boolean[] discovered = new boolean[total_number_of_edges];
        // To store the complete path between source and destination
        Stack<Integer> path = new Stack<>();
        //First, we find the random point to reach in the graph,
        //before we head out to the destination
        int mid_point_id = GetRandomPointGraphID(total_number_of_nodes);
        //then, we find if a straight path from source to destination exists
        if(CalculatePath(graph, source_graph_id, destination_graph_id, discovered, path)){
            //then, we calculate if the path from source to midpoint AND
            //path from midpoint to destination both exist\
            discovered = new boolean[discovered.length];
            Stack<Integer> path_copy = new Stack<Integer>();
            //path_copy.addAll(path);
            if(CalculatePath(graph, source_graph_id, mid_point_id, discovered, path_copy)){
                discovered = new boolean[discovered.length];
                //Removing the duplicate midpoint in the path
                path_copy.pop();
                if(CalculatePath(graph, mid_point_id, destination_graph_id, discovered, path_copy)){
                    System.out.println("Path exists from vertex " + source_graph_id + " to vertex " + mid_point_id +
                            " to vertex " + destination_graph_id);
                    System.out.println("The complete path is " + path_copy);
                    return path_copy;
                }
                else{
                    return path;
                }
            }
            else{
                return path;
            }
        }
        return CalculateRandomPath(graph, total_number_of_edges, total_number_of_nodes, source_graph_id, GetRandomPointGraphID(total_number_of_nodes));
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
            Point point;
            DefaultNode carLastVisitedNode = getNodeFromID(car.getLast_visited_node_id());
            Pair pair = GetNextNodeInPathIdAndPositionInArr(car.path, car.last_visited_node_id);

            DefaultNode nextNode = getNodeFromID(pair.x);

//            point = DistanceUtils.pointOnBearingRAD(carLastVisitedNode.latitude, carLastVisitedNode.longitude,
//                    DistanceUtils.dist2Radians(car.progress_since_last_node_in_km, 6371.0),
//                    DistanceUtils.toRadians(bearing(carLastVisitedNode.latitude, carLastVisitedNode.longitude,
//                            nextNode.latitude, nextNode.longitude)), new SpatialContext(false), null);

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
        //Every single node where a dynamic object may appear(A highway, a road, e.t.c.)
        //And its relation is to be organised in the node structure(Node and NodeGroup objects
        //which shall all exist in memory while the code is running)
        Object obj = new JSONParser().parse(new FileReader("simple_map.geojson"));
        JSONObject map = (JSONObject) obj;

        JSONArray features = (JSONArray) map.get("features");
        for (int i = 0; i < features.size(); i++) {
            Object current = new Object();
            current = features.get(i);
            JSONObject curr = (JSONObject) features.get(i);
            JSONObject properties = new JSONObject();
            properties = (JSONObject) ((JSONObject) current).get("properties");
            String str = (String) properties.get("highway");
            Random rand = new Random();
            if (str != null) {
                String group_id = (String) curr.get("id");
                //group_id = group_id.substring(4);
                group_id = group_id.replaceAll("[^1-9]", "");
                //int groupId = Integer.parseInt(group_id);
                System.out.println("GROUP ID: " + group_id);
                System.out.println(str);
                ArrayList<DefaultNode> group_nodes = new ArrayList<>();
                NodeGroup group = new NodeGroup(group_id, str, group_nodes, 60);
                //Object geometry = ((JSONObject) current).get("geometry");
                JSONObject geometry = (JSONObject) ((JSONObject) current).get("geometry");
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                for (int j = 0; j < coordinates.size(); j++) {

                    System.out.println(coordinates.get(j));
                    if (coordinates.get(j) instanceof JSONArray && !str.equals("stop")) {
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
                            }
                            else{
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
                            }
                            else{
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
                if(!str.equals("stop")) {
                    NavigateNodeGroup(group);
                    NodeGroups.add(group);
                    AllNodes.addAll(group.Nodes);
                }
                System.out.println("Node count: ______ " + group_nodes.size());
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
        //System.out.println("N: " + rules_total);

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
            frameDetails.put("id", Cars.get(i).id);
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
        try (FileWriter file = new FileWriter("new_frames.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            System.out.println("Write");
            file.write(total.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
