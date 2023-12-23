
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;

public class Junction extends Thread {
    private String junctionName;
    private int greenLightTime;
    private Road[] entryRoads;
    private Road[] exitRoads;
    private Map<Road, CarPark> roadToDestination; //  
    private Clock clock;
    private int carCounting;

    public Junction(String name, int greenTime, Road[] entryRoads, Road[] exitRoads, Clock clock)
    {
        this.junctionName = name;
        this.greenLightTime = greenTime;
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
        this.clock = clock;
        this.carCounting = 0;
    }

    public void run()
    {
        while (true)
        {
            if (isGreen())
            {
                // Removing the car from the road to move to the next road.
                for (Road entryRoad : entryRoads)
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
                        passCar(removedCar);
                        System.out.println( junctionName + " has received a car with destination of " + removedCar.getDestination());
                        
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
        int timePassed = clock.getCurrentTime() % (((greenLightTime / 10) * 6) + 1);
        return timePassed < ((greenLightTime / 10) * 6);
    }

    private void passCar(Car car)
    {
        Road exitRoad = getExitRoadForDestination(car.getDestination());
        if (exitRoad != null && !exitRoad.isRoadFull())
        {
            exitRoad.addCar(car);
            CarPark parking = new CarPark("IndustrialPark", 10, exitRoad);
            // parking.admitCarFromRoad();
            carCounting++;
            parking.start();
        }
        else {
            // Handle the case where there is no space on the exit road
            // Log this 
        }
    }

    private Road getExitRoadForDestination(String destination)
    {
        if ("A".equals(junctionName) && "IndustrialPark".equals(destination))
        {
            return exitRoads[0];
        }
        else {
            return exitRoads[0];
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