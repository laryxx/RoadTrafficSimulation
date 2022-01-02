import java.util.ArrayList;

public class OuterNode extends DefaultNode{
    public OuterNode(int id, double latitude, double longitude, int group_id, ArrayList<OuterConnection> outer_connections, int graph_id) {
        super(id, latitude, longitude, group_id, outer_connections, graph_id);
    }
}
