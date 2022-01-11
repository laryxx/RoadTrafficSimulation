import java.util.ArrayList;

public abstract class Vehicle {
int id;
int start_node_id;
int destination_node_id;
ArrayList<DefaultNode> path;
Intent intent;
double speed;
double path_distance_in_km;
int last_visited_node_id;
double progress_in_km;
double distance_to_next_node;
double progress_since_last_node_in_km;
double fitting_speed;
double sum_of_node_distances;
double latitude;
double longitude;
double angle;
//For public transport
//TODO
int loop_reset_node_id;

//For accurate physics
//TODO
int weight_in_kg;

int group_nodes_left;
//In m/s^2
double acceleration_rate;

    public Vehicle() {

    }

    //Any vehicle
    public Vehicle(int id, int start_mode_id, int destination_node_id, ArrayList<DefaultNode> path, Intent intent,
                   double speed, double path_distance_in_km, int last_visited_node_id, double progress_in_km,
                   int weight_in_kg, double distance_to_next_node, double progress_since_last_node_in_km,
                   double fitting_speed, int group_nodes_left, double sum_of_node_distances, double acceleration_rate,
                   double latitude, double longitude, double angle) {
        this.id = id;
        this.start_node_id = start_mode_id;
        this.destination_node_id = destination_node_id;
        this.path = path;
        this.intent = intent;
        this.speed = speed;
        this.path_distance_in_km = path_distance_in_km;
        this.last_visited_node_id = last_visited_node_id;
        this.progress_in_km = progress_in_km;
        this.weight_in_kg = weight_in_kg;
        this.distance_to_next_node = distance_to_next_node;
        this.progress_since_last_node_in_km = progress_since_last_node_in_km;
        this.fitting_speed = fitting_speed;
        this.group_nodes_left = group_nodes_left;
        this.sum_of_node_distances = sum_of_node_distances;
        this.acceleration_rate = acceleration_rate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.angle = angle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setStart_node_id(int start_node_id) {
        this.start_node_id = start_node_id;
    }

    public int getStart_node_id(){
        return start_node_id;
    }

    public void setDestination_node_id(int destination_node_id) {
        this.destination_node_id = destination_node_id;
    }

    public int getDestination_node_id(){
        return destination_node_id;
    }

    public void setPath(ArrayList<DefaultNode> path) {
        this.path = path;
    }

    public ArrayList<DefaultNode> getPath(){
        return  path;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent(){
        return intent;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setPath_distance_in_km(double path_distance_in_km) {
        this.path_distance_in_km = path_distance_in_km;
    }

    public double getPath_distance_in_km() {
        return path_distance_in_km;
    }

    public void setLast_visited_node_id(int last_visited_node_id) {
        this.last_visited_node_id = last_visited_node_id;
    }

    public int getLast_visited_node_id() {
        return last_visited_node_id;
    }

    public void setProgress_in_km(double progress_in_km) {
        this.progress_in_km = progress_in_km;
    }

    public double getProgress_in_km() {
        return progress_in_km;
    }

    public void setWeight_in_kg(int weight_in_kg) {
        this.weight_in_kg = weight_in_kg;
    }

    public int getWeight_in_kg() {
        return weight_in_kg;
    }

    public void setDistance_to_next_node(double distance_to_next_node) {
        this.distance_to_next_node = distance_to_next_node;
    }

    public double getDistance_to_next_node() {
        return distance_to_next_node;
    }

    public void setProgress_since_last_node_in_km(double progress_since_last_node_in_km) {
        this.progress_since_last_node_in_km = progress_since_last_node_in_km;
    }

    public double getProgress_since_last_node_in_km() {
        return progress_since_last_node_in_km;
    }

    public void setLoop_reset_node_id(int loop_reset_node_id) {
        this.loop_reset_node_id = loop_reset_node_id;
    }

    public int getLoop_reset_node_id() {
        return loop_reset_node_id;
    }

    public void setFitting_speed(double fitting_speed) {
        this.fitting_speed = fitting_speed;
    }

    public double getFitting_speed() {
        return fitting_speed;
    }

    public void setGroup_nodes_left(int group_nodes_left) {
        this.group_nodes_left = group_nodes_left;
    }

    public int getGroup_nodes_left() {
        return group_nodes_left;
    }

    public void setSum_of_node_distances(double sum_of_node_distances) {
        this.sum_of_node_distances = sum_of_node_distances;
    }

    public double getSum_of_node_distances() {
        return sum_of_node_distances;
    }

    public void setAcceleration_rate(double acceleration_rate) {
        this.acceleration_rate = acceleration_rate;
    }

    public double getAcceleration_rate() {
        return acceleration_rate;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
