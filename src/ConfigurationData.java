import java.util.ArrayList;

public class ConfigurationData {
    String name;
    String hash;
    String daytimeSetting;
    double bottom_left_lat;
    double bottom_left_long;
    double top_right_lat;
    double top_right_long;
    long duration;
//    ArrayList<SourcePoint> sourcePoints;
    ArrayList<GenerationRule> generationRules;

    public ConfigurationData(){

    }

    public ConfigurationData(String name, String hash, String daytimeSetting, double bottom_left_lat,
                             double bottom_left_long, double top_right_lat, double top_right_long, long duration,
                             ArrayList<GenerationRule> generationRules){
        this.name = name;
        this.hash = hash;
        this.daytimeSetting = daytimeSetting;
        this.bottom_left_lat = bottom_left_lat;
        this.bottom_left_long = bottom_left_long;
        this.top_right_lat = top_right_lat;
        this.top_right_long = top_right_long;
        this.duration = duration;
        this.generationRules = generationRules;
    }

    public String sourcePointsToString(){
        StringBuilder points = new StringBuilder();
        for(int i = 0; i < generationRules.size(); i++){
            String point = "--------=Point " + (i+1) + "=-------- \n" +
                    "Name: " + generationRules.get(i).name + ", \n" +
                    "Start Latitude: " + generationRules.get(i).startLatitude + ", \n" +
                    "Start Longitude: " + generationRules.get(i).startLongitude + ", \n" +
                    "End Latitude: " + generationRules.get(i).endLatitude + ", \n" +
                    "End Longitude: " + generationRules.get(i).endLongitude + ", \n" +
                    "Start Group id: " + generationRules.get(i).startGroupId + ", \n" +
                    "End Group id: " + generationRules.get(i).endGroupId + ", \n" +
                    "Vans: " + generationRules.get(i).vans + ", \n" +
                    "Trucks: " + generationRules.get(i).trucks + ", \n" +
                    "Sedans: " + generationRules.get(i).sedans + ", \n" +
                    "Intensity: " + generationRules.get(i).intensity + ", \n" +
                    "Hash: " + generationRules.get(i).hash_id + ", \n";
            points.append(point);
        }

        return points.toString();
    }

    @Override
    public String toString() {
        return "--------=Configuration settings=--------\n" +
                "Name: " + name + ", \n" +
                "Hash: " + hash + ", \n" +
                "Daytime: " + daytimeSetting + ", \n" +
                "Bottom-left latitude: " + bottom_left_lat + ", \n" +
                "Bottom-left longitude: " + bottom_left_long + ", \n" +
                "Top-right latitude: " + top_right_lat + ", \n" +
                "Top-right longitude: " + top_right_long + ", \n" +
                "Duration: " + duration + ", \n" + sourcePointsToString();
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ArrayList<GenerationRule> getGenerationRules() {
        return generationRules;
    }

    public void setGenerationRules(ArrayList<GenerationRule> generationRules) {
        this.generationRules = generationRules;
    }

    public void setBottom_left_lat(double bottom_left_lat) {
        this.bottom_left_lat = bottom_left_lat;
    }

    public void setBottom_left_long(double bottom_left_long) {
        this.bottom_left_long = bottom_left_long;
    }

    public void setDaytimeSetting(String daytimeSetting) {
        this.daytimeSetting = daytimeSetting;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTop_right_lat(double top_right_lat) {
        this.top_right_lat = top_right_lat;
    }

    public void setTop_right_long(double top_right_long) {
        this.top_right_long = top_right_long;
    }


    public String getDaytimeSetting() {
        return daytimeSetting;
    }

    public String getHash() {
        return hash;
    }

    public String getName() {
        return name;
    }

    public double getBottom_left_lat() {
        return bottom_left_lat;
    }

    public double getBottom_left_long() {
        return bottom_left_long;
    }

    public double getTop_right_lat() {
        return top_right_lat;
    }

    public double getTop_right_long() {
        return top_right_long;
    }

    public long getDuration() {
        return duration;
    }
}
