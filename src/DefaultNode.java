import java.util.ArrayList;

public abstract class DefaultNode {
    int id;
    double latitude;
    double longitude;
    int connection_id;
    int group_id;
    ArrayList<OuterConnection> outer_connections;
    int graph_id;

    public DefaultNode(){

    }

    //OuterNode
    public DefaultNode(int id, double latitude, double longitude, int group_id, ArrayList<OuterConnection> outer_connections, int graph_id) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.group_id = group_id;
        this.outer_connections = outer_connections;
        this.graph_id = graph_id;
    }

    //EndNode
    public DefaultNode(int id, double latitude, double longitude, int group_id, int graph_id) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.group_id = group_id;
        this.graph_id = graph_id;
    }

    //InnerNode
    public DefaultNode(int id, double latitude, double longitude, int connection_id, int group_id, int graph_id) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.connection_id = connection_id;
        this.group_id = group_id;
        this.graph_id = graph_id;
    }

    //LoneNode
    public DefaultNode(int id,  ArrayList<OuterConnection> outer_connections, double latitude, double longitude, int graph_id){
        this.id = id;
        this.outer_connections = outer_connections;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setConnection_id(int connection_id){
        this.connection_id = connection_id;
    }

    public int getConnection_id(){
        return connection_id;
    }

    public void setGroup_id(int group_id){
        this.group_id = group_id;
    }

    public int getGroup_id(){
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
}
