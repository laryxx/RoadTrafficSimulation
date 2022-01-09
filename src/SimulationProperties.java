import java.time.LocalTime;

public class SimulationProperties {
    LocalTime start_time;
    LocalTime end_time;
    int fps;

    public SimulationProperties(LocalTime start_time, LocalTime end_time, int fps){
        this.start_time = start_time;
        this.end_time = end_time;
        this.fps = fps;
    }

    public SimulationProperties(){

    }

}
