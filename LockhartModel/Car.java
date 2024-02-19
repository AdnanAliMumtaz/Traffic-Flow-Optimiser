package LockhartModel;

public class Car {
    private String carDestination;
    private long entryTime;
    private long parkedTime;
    private Clock clock;

    public Car(String destinations, long entryTimeInSeconds) {
        this.carDestination = destinations;
        this.entryTime = entryTimeInSeconds;
        this.parkedTime = 0;
        this.clock = clock;
    }

    public void parked() {
        parkedTime = System.nanoTime();
    }

    public long getJourneyTime()
    {
        return parkedTime - entryTime;
    }

    // Getters
    public String getDestination() {
        return carDestination;
    }

    // Destructor
    public void finalize() 
    {

    }
}
