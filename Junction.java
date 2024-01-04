
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

    public Junction(String name, int greenTime, int entryRoadsNums, int exitRoadsNums, Clock clock)
    {
        this.junctionName = name;
        this.greenLightTime = greenTime;
        this.entryRoads = new Road[entryRoadsNums];
        this.exitRoads = new Road[exitRoadsNums];
        this.clock = clock;
        this.carCounting = 0;
    }

    public void setEntrySouthRoad(Road road)
    {
        // entryRoads[0] = road;
        for (int i = 0; i < entryRoads.length; i++) {
            if (entryRoads[i] == null) {
                entryRoads[i] = road;
                break; // Exit the loop after assigning the road
            }
        }
    }

    public void setEntryEastRoad(Road road)
    {
        for (int i = 0; i < entryRoads.length; i++) {
            if (entryRoads[i] == null) {
                entryRoads[i] = road;
                break; // Exit the loop after assigning the road
            }
        }   
    }

    public void setEntryNorthRoad(Road road)
    {
        for (int i = 0; i < entryRoads.length; i++) {
            if (entryRoads[i] == null) {
                entryRoads[i] = road;
                break; // Exit the loop after assigning the road
            }
        }
    }
    
    public void setExitSouthRoad(Road road)
    {
        for (int i = 0; i < exitRoads.length; i++) {
            if (exitRoads[i] == null) {
                exitRoads[i] = road;
                break; // Exit the loop after assigning the road
            }
        }
    }

    public void setExitEastRoad(Road road)
    {
        for (int i = 0; i < exitRoads.length; i++) {
            if (exitRoads[i] == null) {
                exitRoads[i] = road;
                break; // Exit the loop after assigning the road
            }
        }
    }

    public void setExitNorthRoad(Road road)
    {
        for (int i = 0; i < exitRoads.length; i++) {
            if (exitRoads[i] == null) {
                exitRoads[i] = road;
                break; // Exit the loop after assigning the road
            }
        }
    }

    public void setExitWestRoad(Road road)
    {
        // exitRoads[0] = road;
        for (int i = 0; i < exitRoads.length; i++) {
            if (exitRoads[i] == null) {
                exitRoads[i] = road;
                break; // Exit the loop after assigning the road
            }
        }
    }

    public void run()
    {
        while (clock.getRunningTime())
        {
            if (isGreen())
            {
                // Removing the car from the road to move to the next road.
                for (Road entryRoad : entryRoads)
                {
                    if (entryRoad != null)
                    {
                        Car removedCar = entryRoad.removeCar();
                        if (removedCar != null) {
                            // Simulating the car passing
                            try {
                                // Thread.sleep(1000 * 60 / 12);
                                Thread.sleep(1000 * 12 * 6 / 60);
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
        long greenLightDuration = TimeUnit.SECONDS.toNanos(greenLightTime);
        
        // System.out.println(elapsedTime + "  " + greenLightDuration);

        return elapsedTime < greenLightDuration;
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
        if ("A" == junctionName && "IndustrialPark" == destination)
        {
            return exitRoads[0];
        }
        else if ("A" == junctionName && "ShoppingCentre" == destination)
        {
            return exitRoads[1];
        }
        else if ("B" == junctionName && "ShoppingCentre" == destination)
        {
            return exitRoads[0];
        }
        else {
            return null;
        }
    }

    private void CheckNextDirection(Car car)
    {
        // Only doing the exits routes here
        if (junctionName == "A")
        {
            if (car.getDestination() == "IndustrialPark")
            {
                // Return the exit route to destination (West)
                // CarPark IndustrialParking = new CarPark(junctionName, MAX_PRIORITY, null); 
            }
            else {
                // Return the exit route to Junction B (North)
            }
        }
        else if (junctionName == "B")
        {
            if (car.getDestination() == "IndustrialPark")
            {
                // Return the exit route to Junction A (South)
            }
            else {
                // Return the exit route to Junction C (North)
            }
        }
        else if (junctionName == "C")
        {
            if (car.getDestination() == "ShoppingCentre")
            {
                // Return the exit route to destination (South)
            }
            else if (car.getDestination() == "IndustrialPark") 
            {
                // Return the exit route to destination (West)
            }
            else {
                // Return the exit route to Junction D (North)
            }
        }
        else if (junctionName == "D")
        {
            if (car.getDestination() == "University")
            {
                // Return the exit route to destination (North)
            }
            else {
                // Return the exit route to destination (South)
            }
        }
    }

    
}