import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class JSONreader {
    public static void main(String[] args) throws IOException, ParseException {
        //Every single node where a dynamic object may appear(A highway, a road, e.t.c.)
        //And its relation is to be organised in the node structure(Node and NodeGroup objects
        //which shall all exist in memory while the code is running)
        Object obj = new JSONParser().parse(new FileReader("map.geojson"));
        JSONObject map = (JSONObject) obj;

        JSONArray features = (JSONArray) map.get("features");
        for (int i = 0; i < features.size(); i++) {
            Object current = new Object();
            current = features.get(i);
            JSONObject curr = (JSONObject) features.get(i);
            JSONObject properties = new JSONObject();
            properties = (JSONObject) ((JSONObject) current).get("properties");
            String str = (String) properties.get("highway");
            Random rand = new Random();
            if (str != null) {
                String group_id = (String) curr.get("id");
                group_id = group_id.substring(4);
                int groupId = Integer.parseInt(group_id);
                System.out.println("GROUP ID: " + groupId);
                System.out.println(str);
                //Object geometry = ((JSONObject) current).get("geometry");
                JSONObject geometry = (JSONObject) ((JSONObject) current).get("geometry");
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                for (int j = 0; j < coordinates.size(); j++) {
                    System.out.println(coordinates.get(j));
                    JSONArray lat_and_long = (JSONArray) coordinates.get(j);
                    System.out.println("lat: " + lat_and_long.get(0));
                    //The first and last nodes are outer nodes
                    if (j == 0 || j == coordinates.size() - 1) {
                        int id = rand.nextInt(50000) + 1;
//
                    }
                }
            }
        }
    }

}
