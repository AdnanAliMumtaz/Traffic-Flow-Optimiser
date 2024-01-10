
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Junction extends Thread {
    private String junctionName;
    private int greenLightTime;
    private Road[] entryRoads;
    private Road[] exitRoads;
    private Clock clock;
    private int carCounting;

    private Map<String, Road> entries;
    private Map<String, Road> exits;

    public Junction(String name, int greenTime, int entryRoadsNums, int exitRoadsNums, Clock clock)
    {
        this.junctionName = name;
        this.greenLightTime = greenTime;
        this.entryRoads = new Road[entryRoadsNums];
        this.exitRoads = new Road[exitRoadsNums];
        this.clock = clock;
        this.carCounting = 0;

        this.entries = new HashMap<>();
        this.exits = new HashMap<>();
    }

    public synchronized void setEntry(String name, Road road) 
    {
        entries.put(name, road);
    }

    public synchronized void setExit(String name, Road road) 
    {
        exits.put(name, road);
    }

    public void run()
    {
        while (clock.getRunningTime())
        {
            if (isGreen())
            {
                for (Map.Entry<String, Road> entry : entries.entrySet())
                {
                    String entryDirection = entry.getKey();
                    Road entryRoad = entry.getValue();

                    if (entryRoad != null) {
                        Car removedCar = entryRoad.removeCar();
                        if (removedCar != null) {
                            // Simulating the car passing
                            try {
                                // Thread.sleep(1000 * 60 / 12);
                                Thread.sleep(clock.fastTrackPerMinutes(12));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println( junctionName + " has received a car with destination of " + removedCar.getDestination());            
                            passCar(removedCar);                        
                        }
                    }
                }
                

            }
            // else {
            //     System.out.println("Light has gone RED!");
            // }
        }
    }

    private boolean isGreen()
    {
        // int timePassed = clock.getCurrentTime() % (((greenLightTime / 10) * 6) + 1);
        // return timePassed < ((greenLightTime / 10) * 6);

        long elapsedTime = clock.getCurrentTime(); // Assuming getCurrentTime() returns elapsed time in seconds
        long greenLightDuration = TimeUnit.SECONDS.toNanos(clock.fastTrackPerSeconds(greenLightTime));
        
        // System.out.println(elapsedTime + "  " + greenLightDuration);

        // return elapsedTime < greenLightDuration;
        return true;
    }

    private void passCar(Car car)
    {
        Road exitRoad = getExitRoadForDestination(car.getDestination());
        if (exitRoad != null && !exitRoad.isRoadFull())
        {
            exitRoad.addCar(car);
            // /CarPark parking = new CarPark("IndustrialPark", 10, exitRoad);
            // parking.admitCarFromRoad();
            // parking.start();
        }
        else {
            // Handle the case where there is no space on the exit road
            // Log this 
        }
    }

    private Road getExitRoadForDestination(String destination)
    {
        if ("A" == junctionName)
        {
            if ("IndustrialPark" == destination) {
                return exits.get("West");
            }
            else {
                return exits.get("North");
            }
        }
        else if ("B" == junctionName)
        {
            if ("ShoppingCentre" == destination) {
                return exits.get("West");
            }
            else {
                return exits.get("South");
            }
        }
        else {
            return null;
        }
    }
}