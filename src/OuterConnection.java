import java.util.ArrayList;

public class OuterConnection {
    String group_connection_id;
    int group_connection_node_id;

    public OuterConnection(){

    }

    public OuterConnection(String group_connection_id, int group_connection_node_id){
        this.group_connection_id = group_connection_id;
        this.group_connection_node_id = group_connection_node_id;
    }

    public void setGroup_connection_id(String group_connection_id){
        this.group_connection_id = group_connection_id;
    }

    public String getGroup_connection_id(){
        return group_connection_id;
    }

    public void setGroup_connection_node_id(int group_connection_node_id){
        this.group_connection_node_id = group_connection_node_id;
    }

    public int getGroup_connection_node_id(){
        return group_connection_node_id;
    }

}
