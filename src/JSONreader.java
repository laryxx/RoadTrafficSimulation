import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JSONreader {
    public static void main(String[] args) throws IOException, ParseException {
//        Object obj = new JSONParser().parse(new FileReader("map.geojson"));
//        JSONObject map = (JSONObject) obj;
//
//        JSONArray features = (JSONArray) map.get("features");
//        for(int i = 0; i < features.size(); i++){
//            Object current = new Object();
//            current = features.get(i);
//            JSONObject properties = new JSONObject();
//            properties = (JSONObject) ((JSONObject) current).get("properties");
//            String str = (String) properties.get("highway");
//            if(str != null) {
//                System.out.println(str);
//                //Object geometry = ((JSONObject) current).get("geometry");
//                JSONObject geometry = (JSONObject) ((JSONObject) current).get("geometry");
//                JSONArray coordinates = (JSONArray) geometry.get("coordinates");
//                for(int j = 0; j < coordinates.size(); j++){
//                    System.out.println(coordinates.get(j));
//                }
//            }
//        }
    }


}
