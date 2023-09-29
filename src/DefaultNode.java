import java.util.ArrayList;

public abstract class DefaultNode {
    int id;
    double latitude;
    double longitude;
    String group_id;
    ArrayList<OuterConnection> outer_connections;
    ArrayList<Integer> connections;
    int graph_id;

    public DefaultNode(){

    }

    //OuterNode
    public DefaultNode(int id, double latitude, double longitude, String group_id,
                       ArrayList<OuterConnection> outer_connections, ArrayList<Integer> connections, int graph_id) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.group_id = group_id;
        this.outer_connections = outer_connections;
        this.connections = connections;
        this.graph_id = graph_id;
    }

    //InnerNode
    public DefaultNode(int id, double latitude, double longitude, String group_id, ArrayList<Integer> connections,
                       int graph_id) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.connections = connections;
        this.group_id = group_id;
        this.graph_id = graph_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setGroup_id(String group_id){
        this.group_id = group_id;
    }

    public String getGroup_id(){
        return group_id;
    }

    public void setOuter_connections(ArrayList<OuterConnection> outer_connections) {
        this.outer_connections = outer_connections;
    }

    public ArrayList<OuterConnection> getOuter_connections(){
        return outer_connections;
    }

    public void setGraph_id(int graph_id) {
        this.graph_id = graph_id;
    }

    public int getGraph_id() {
        return graph_id;
    }

    public void setConnections(ArrayList<Integer> connections) {
        this.connections = connections;
    }

    public ArrayList<Integer> getConnections() {
        return connections;
    }
}
