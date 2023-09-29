import java.lang.reflect.Array;
import java.util.ArrayList;

public class Path {
    ArrayList<DefaultNode> path;
    double totalDistance;
    double avgDistance;
    int assignedCarId;
    ArrayList<Integer> preTurnNodes;

    public Path(ArrayList<DefaultNode> path, int assignedCarId, ArrayList<Integer> preTurnNodes){
        this.path = path;
        this.totalDistance = calculateTotalDistance(path);
        this.avgDistance = this.totalDistance/path.size();
        this.assignedCarId = assignedCarId;
        this.preTurnNodes = preTurnNodes;
    }

    public double calculateTotalDistance(ArrayList<DefaultNode> path){
        double distance = 0.0;
        for(int i = 0; i < path.size()-1; i++){
            double la1 = path.get(i).latitude;
            double lo1 = path.get(i).longitude;
            double la2 = path.get(i+1).latitude;
            double lo2 = path.get(i+1).longitude;
            distance = distance + Generator.CalculateDistanceInMeters(la1, lo1, la2, lo2);
        }
        return distance;
    }

    @Override
    public String toString(){
        return "Number of path nodes: " + path.size() + "\nTotal path distance: " + totalDistance + "m" +
                "\nAverage distance between nodes: " + avgDistance + "m" + "\nNumber of turns: " + preTurnNodes;
    }
}
