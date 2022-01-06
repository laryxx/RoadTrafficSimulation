import java.util.ArrayList;

public class NodeGroup {
    int id;
    String type;
    ArrayList<DefaultNode> Nodes;
    double fitting_speed;

    public NodeGroup(int id, String type, ArrayList<DefaultNode> Nodes, double fitting_speed){
        this.id = id;
        this.type = type;
        this.Nodes = Nodes;
        this.fitting_speed = fitting_speed;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setNodes(ArrayList<DefaultNode> nodes) {
        Nodes = nodes;
    }

    public ArrayList<DefaultNode> getNodes(){
        return Nodes;
    }

    public void setFitting_speed(double fitting_speed){
        this.fitting_speed = fitting_speed;
    }

    public double getFitting_speed() {
        return fitting_speed;
    }
}
