public class Pair {
    int x;
    int y;
    double a;
    int b;
    double latitude;
    double longitude;

    public Pair(){

    }

    public Pair(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Pair(double a, int b){
        this.a = a;
        this.b = b;
    }

    public Pair(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
