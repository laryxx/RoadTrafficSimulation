import java.util.ArrayList;

public class InnerNode extends DefaultNode {

    public InnerNode(int id, double latitude, double longitude, String group_id, ArrayList<Integer> connections,
                     int graph_id){
        super(id, latitude, longitude, group_id, connections, graph_id);
    }

}
