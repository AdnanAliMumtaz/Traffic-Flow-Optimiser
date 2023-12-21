
// Not a thread
public class Car {
    private String destinations;
    private long entryTime;
    private long endTime; 

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
