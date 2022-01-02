public class GenerationRule {

    double trucks;
    double vans;
    double sedans;
    DefaultNode source_node;
    DefaultNode destination_node;
    int intensity;

    public GenerationRule(double trucks, double vans, double sedans, DefaultNode source_node,
                          DefaultNode destination_node, int intensity){
        this.trucks = trucks;
        this.vans = vans;
        this.sedans = sedans;
        this.source_node = source_node;
        this.destination_node = destination_node;
        this.intensity = intensity;
    }

    public int getIntensity() {
        return intensity;
    }
}
