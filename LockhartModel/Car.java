package LockhartModel;

public class Car {
    private String destinations;
    private long entryTime;
    private long parkedTime; 

    public Car(String destinations, long entryTime) {
        this.destinations = destinations;
        this.entryTime = entryTime;
        this.parkedTime = 0;
    }

    public void parked() {
        parkedTime = System.nanoTime();
    }

    public double getJourneyTime()
    {
        if (parkedTime == 0)
        {
            return 0;
        }
        else
        {
            long nanoseconds = parkedTime - entryTime;
            // double seconds = nanoseconds / 1_000_000_000.0;
            double seconds = (nanoseconds * 10) / 1_000_000_000.0;
            // System.out.println("Duration in seconds: " + seconds);

            // return parkedTime - entryTime;
            return seconds;
        }
    }

    public long getJourneyTimee() {
        if (parkedTime == 0) {
            return 0;
        } else {
            long nanoseconds = parkedTime - entryTime;
            long seconds = (long) (nanoseconds / 1_000_000_000.0);
            // System.out.println("Duration in seconds: " + seconds);
            return seconds;
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
