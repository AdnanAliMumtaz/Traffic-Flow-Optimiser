
public class Car {
    private String destinations;
    private long entryTime;
    private long parkedTime; 

    public Car(String destinations, long entryTime) {
        this.destinations = destinations;
        this.entryTime = entryTime;
    }

    public void parked() {
        parkedTime = System.currentTimeMillis();
    }

    public long getJourneyTime()
    {
        if (parkedTime == 0)
        {
            return 0;
        }
        else
        {
            return parkedTime - entryTime;
        }
    }

    //Getters 
    public String getDestination() {
        return destinations;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public long getParkedTime() {
        return parkedTime;
    }

}
