public class Triplet {
    int x;
    int y;
    int z;

    double latitude;

    double longitude;

    int id;

    public Triplet(){

    }

    public Triplet(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Triplet(double latitude, double longitude, int id){
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

}
