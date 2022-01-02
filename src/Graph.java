import java.util.*;

//Code from internet
public class Graph {

    List<List<Integer>> adjacency_list = new ArrayList<>();
    int number_of_nodes;

    //Graph Constructor
    public Graph(List<Edge> edges, int edge_count)
    {
        // adjacency list memory allocation
        for (int i = 0; i < edge_count; i++)
            adjacency_list.add(i, new ArrayList<>());

        // add edges to the graph
        for (Edge edge : edges)
        {
            // allocate new node in adjacency List from src to dest
            adjacency_list.get(edge.source).add(edge.destination);
        }
        this.number_of_nodes = GetNumberOfNodes(edges);
    }

    public Graph(){

    }

    public int GetNumberOfNodes(List<Edge> edges){
        HashSet<Integer> set = new HashSet<>();
        for(int i = 0; i < edges.size(); i++){
            System.out.println("Add set: " + edges.get(i).source + " " + edges.get(i).destination);
            set.add(edges.get(i).source);
            set.add(edges.get(i).destination);
        }
        return set.size();
    }

}
