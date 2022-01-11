public class GenerationRule {

    double trucks;
    double vans;
    double sedans;
    DefaultNode source_node;
    DefaultNode destination_node;
    int intensity;
    int source_node_json_id;
    int destination_node_json_id;

    public GenerationRule(double trucks, double vans, double sedans, DefaultNode source_node,
                          DefaultNode destination_node, int intensity){
        this.trucks = trucks;
        this.vans = vans;
        this.sedans = sedans;
        this.source_node = source_node;
        this.destination_node = destination_node;
        this.intensity = intensity;
    }

    public GenerationRule(double trucks, double vans, double sedans, int source_node_json_id,
                          int destination_node_json_id, int intensity){
        this.trucks = trucks;
        this.vans = vans;
        this.sedans = sedans;
        this.source_node_json_id = source_node_json_id;
        this.destination_node_json_id = destination_node_json_id;
        this.intensity = intensity;
    }


    public int getIntensity() {
        return intensity;
    }
}
