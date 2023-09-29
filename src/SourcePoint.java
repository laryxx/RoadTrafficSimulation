public class SourcePoint {
    String name;
    double latitude;
    double longitude;
    boolean vans;
    boolean trucks;
    boolean sedans;
    long intensity;
    String hash_id;

    public SourcePoint(){

    }

    public SourcePoint(String name, double latitude, double longitude, boolean vans,  boolean trucks, boolean sedans,
                       long intensity, String hash_id){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vans = vans;
        this.trucks = trucks;
        this.sedans = sedans;
        this.intensity = intensity;
        this.hash_id = hash_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHash_id(String hash_id) {
        this.hash_id = hash_id;
    }

    public void setIntensity(long intensity) {
        this.intensity = intensity;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSedans(boolean sedans) {
        this.sedans = sedans;
    }

    public void setTrucks(boolean trucks) {
        this.trucks = trucks;
    }

    public void setVans(boolean vans) {
        this.vans = vans;
    }

    public String getName() {
        return name;
    }

    public String getHash_id() {
        return hash_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getIntensity() {
        return intensity;
    }

    public boolean isSedans() {
        return sedans;
    }

    public boolean isTrucks() {
        return trucks;
    }

    public boolean isVans() {
        return vans;
    }
}
