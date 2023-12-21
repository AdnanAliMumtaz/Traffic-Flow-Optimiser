

public class Junction extends Thread {
    private String name;
    private int numEntryRoutes;
    private int numExitRoutes;
    private boolean[][] trafficLights; // 2D array to store state of traffic lights for entry and exit route
    private int[] greenTimes; // to store the duration of each entry route
    private Road[] entryRoads; // to store the number of entries in each entry route
    private Road[] exitRoads; // to store the number of exits in each entry route

    public Junction(String name, int numEntryRoutes, int numExitRoutes)
    {
        this.name = name;
        this.numEntryRoutes = numEntryRoutes;
        this.numExitRoutes  = numExitRoutes;
        this.trafficLights = new boolean[numEntryRoutes][numExitRoutes];
        this.greenTimes = new int[numEntryRoutes];
        this.entryRoads = new Road[numEntryRoutes];
        this.exitRoads = new Road[numExitRoutes];

        // Initialising the traffic lights and green times
        initialiseTrafficLights();
        initialiseGreenTimes();
    }

    //Thread
    public void run()
    {
        while (true)
        {
            for (int entryRoute = 0; entryRoute < numEntryRoutes; entryRoute++)
            {
                // System.out.println("name");
                if (isGreen(entryRoute))
                {
                    passCars(entryRoute);

                    try {
                        // Thread.sleep(greenTimes[entryRoute] * 1000);
                        int hour = 1000 * 60 * 60;
                        sleep( hour / 550);
                    } 
                    catch (InterruptedException exception)
                    {
                        exception.printStackTrace();
                    }
                }
                // entryRoute++;
            }
        }
    }

    private synchronized void passCars(int entryRoute)
    {
        for (Car car : entryRoads[entryRoute].getCars())
        {
            if (isSpaceAvailable(entryRoute, car.getDestination()))
            {
                System.out.println("Time: " + Clock.getCurrentTime() + " - " + name + ": Car passed from Entry "
                + entryRoute + " to Exit " + car.getDestination());
                
                entryRoads[entryRoute].removeCar(); // First In First Out queue 
            }
        }
    }

    private synchronized boolean isSpaceAvailable(int entryRoute, String desitnation)
    {
        return true;
    }

    private synchronized boolean isGreen(int entryRoute)
    {
        boolean is = false;
        for (int exitRoute = 0; exitRoute < numExitRoutes; exitRoute++)
        {
            if (trafficLights[entryRoute][exitRoute])
            {
                is = true;
            }
        }
        return is;
    }

    public void initialiseTrafficLights()
    {

    }

    public void initialiseGreenTimes()
    {
        
    }

    public void setGreenTimes(int[] greenTimes)
    {
        if (greenTimes.length != numEntryRoutes)
        {
            throw new IllegalArgumentException("Invalid configuration: Green time durations do not match the number of entry routes");
        }

        this.greenTimes = greenTimes;
    }
}
