
public class Car {
    private String destinations;
    private long entryTime;
    private long endTime; // Representing the end times when the car has been parked.

    public Car(String destinations, long entryTime)
    {
        this.destinations = destinations;
        this.entryTime = entryTime;
    }


    public void parked() // - Need more work
    {
        //endTime = Clock.getCurrentTime();
    }


    //Getters 
    public String getDestination()
    {
        return destinations;
    }

    public long getEntryTime()
    {
        return entryTime;
    }

    public long getEndTime()
    {
        return endTime;
    }
}
