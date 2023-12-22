
import java.util.HashMap;
import java.util.Map;

public class Junction extends Thread {
    private String junctionName;
    private int greenLightTime;
    private Road[] entryRoads;
    private Road[] exitRoads;

    public Junction(String name, int greenTime, Road[] entryRoads, Road[] exitRoads)
    {
        this.junctionName = name;
        this.greenLightTime = greenTime;
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
    }

    public void run()
    {
        while (true)
        {
            // Removing the car from the road to move to the next road.
            for (Road entryRoad : entryRoads)
            {
                Car removedCar = entryRoad.removeCar();
                if (removedCar != null) {
                    // Simulating the car passing
                    try {
                        Thread.sleep(1000 * 60 / 12);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }            
                    passCar(removedCar);
                }
            }
        }
    }

    private void passCar(Car car)
    {
        Road exitRoad = getExitRoadForDestination(car.getDestination());
        if (exitRoad != null && !exitRoad.isRoadFull())
        {
            exitRoad.addCar(car);
            CarPark parking = new CarPark("IndustrialPark", 10, exitRoad);
            // parking.admitCarFromRoad();
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
}