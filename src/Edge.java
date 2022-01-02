//borrowed part code
class Edge {
    int source;
    int destination;
    int source_id;
    int destination_id;

    Edge(int src, int dest, int source_id, int destination_id) {
        this.source = src;
        this.destination = dest;
        this.source_id = source_id;
        this.destination_id = destination_id;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getSource() {
        return source;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getDestination() {
        return destination;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setDestination_id(int destination_id) {
        this.destination_id = destination_id;
    }

    public int getDestination_id() {
        return destination_id;
    }
}
