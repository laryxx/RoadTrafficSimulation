import java.util.ArrayList;

public class OuterNode extends DefaultNode{
    public OuterNode(int id, double latitude, double longitude, String group_id,
                     ArrayList<OuterConnection> outer_connections, ArrayList<Integer> connections, int graph_id) {
        super(id, latitude, longitude, group_id, outer_connections, connections, graph_id);
    }

}
