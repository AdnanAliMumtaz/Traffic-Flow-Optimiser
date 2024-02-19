package LockhartModel;

public class Car {
    private String carDestination;
    private long entryTime;
    private long parkedTime;

    public Car(String destinations, long entryTimeInSeconds) {
        this.carDestination = destinations;
        this.entryTime = entryTimeInSeconds;
        this.parkedTime = 0;
    }

    // Method to mark the car as parked
    public void parked() {
        parkedTime = System.nanoTime();
    }

    // Method to calculate total journey time of the car
    public long getTotalJourneyTime() {
        return parkedTime - entryTime;
    }

    // Getter for car destination
    public String getDestination() {
        return carDestination;
    }

    // Destructor (not implementedd
    public void finalize() {

    }
}
