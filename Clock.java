

public class Clock {
    private int currentTime;

    public Clock()
    {
        this.currentTime = 0;
    }

    public synchronized int  getCurrentTime()
    {
        return currentTime;
    }

    public synchronized void incrementTime()
    {
        currentTime++;
    }


    // try {
    //     int simulationDuration = 360; // 1 hour simulated time, each tick represents 10 seconds
    //     for (int i=0; i<simulationDuration; i++)
    //     {
    //         Thread.sleep(1000); // Sleeps one second
    //         incrementTime();
    //     }
    // }
    // catch (InterruptedException exception)
    // {
    //     exception.printStackTrace();
    // }

}
