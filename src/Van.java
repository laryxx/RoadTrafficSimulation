import java.util.ArrayList;

public class Van extends Vehicle{

    public Van(int id, int start_node_id, int destination_node_id, ArrayList<DefaultNode> path, Intent intent,
               int speed, double path_distance_in_km, int last_visited_node_id, double progress_in_km,
               int weight_in_kg, double distance_to_next_node, double progress_since_last_node_in_km,
               double fitting_speed, int group_nodes_left, double sum_of_node_distances, double acceleration_rate,
               double latitude, double longitude, double angle){
        super(id, start_node_id, destination_node_id, path, intent, speed, path_distance_in_km, last_visited_node_id,
                progress_in_km, weight_in_kg, distance_to_next_node, progress_since_last_node_in_km, fitting_speed,
                group_nodes_left, sum_of_node_distances, acceleration_rate, latitude, longitude, angle);
    }

}
