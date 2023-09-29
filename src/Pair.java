import java.util.ArrayList;

public class Pair {
    int x;
    int y;
    double a;
    int b;
    double latitude;
    double longitude;
    DefaultNode node1;
    DefaultNode node2;
    String hash;
    double chance;
    int id;
    double mark;

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

    public Pair(DefaultNode node1, DefaultNode node2){
        this.node1 = node1;
        this.node2 = node2;
    }

    public Pair(String hash, double chance){
        this.hash = hash;
        this.chance = chance;
    }

    public Pair(int id, double mark){
        this.id = id;
        this.mark = mark;
    }
}


