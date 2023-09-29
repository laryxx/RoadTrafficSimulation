import java.util.ArrayList;

public class Sedan extends Vehicle{

    public Sedan(int id, ArrayList<DefaultNode> path, int lastVisitedNodeIndexInPath, String intent, double speed,
                 double fitting_speed, double acceleration_rate, double latitude, double longitude,
                 double progress_in_m, double path_distance_in_m, ArrayList<Pair> distanceData,
                 ArrayList<Integer> preTurnNodes, double deceleration_rate, double angle){
        super(id, path, lastVisitedNodeIndexInPath, intent, speed, fitting_speed, acceleration_rate, latitude,
                longitude, progress_in_m, path_distance_in_m, distanceData, preTurnNodes, deceleration_rate, angle);
    }

    @Override
    public String toString() {
        return "Sedan";
    }

}
