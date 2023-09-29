public class GenerationRule {

    String name;
    double startLatitude;
    double startLongitude;
    double endLatitude;
    double endLongitude;
    boolean vans;
    boolean trucks;
    boolean sedans;
    int intensity;
    String hash_id;
    long startGroupId;
    long endGroupId;

    public GenerationRule(String name, double startLatitude, double startLongitude, double endLatitude,
                          double endLongitude, boolean vans, boolean trucks, boolean sedans, int intensity,
                          String hash_id, long startGroupId, long endGroupId){
        this.name = name;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.vans = vans;
        this.trucks = trucks;
        this.sedans = sedans;
        this.intensity = intensity;
        this.hash_id = hash_id;
        this.startGroupId = startGroupId;
        this.endGroupId = endGroupId;
    }

    public GenerationRule(){

    }

    public void setVans(boolean vans) {
        this.vans = vans;
    }

    public void setTrucks(boolean trucks) {
        this.trucks = trucks;
    }

    public void setSedans(boolean sedans) {
        this.sedans = sedans;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void setHash_id(String hash_id) {
        this.hash_id = hash_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndGroupId(long endGroupId) {
        this.endGroupId = endGroupId;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public void setStartGroupId(long startGroupId) {
        this.startGroupId = startGroupId;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getHash_id() {
        return hash_id;
    }

    public String getName() {
        return name;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public int getIntensity() {
        return intensity;
    }

    public long getEndGroupId() {
        return endGroupId;
    }

    public long getStartGroupId() {
        return startGroupId;
    }
}
