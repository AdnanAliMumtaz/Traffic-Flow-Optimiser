// import java.io.IOException;
import java.util.Random;


public class EntryPoint extends Thread {
    private String name;
    private int entryRate;
    private Random random;
    private Road road;
    private static final int totalWeight = 100;

    public EntryPoint(String name, int entryRate, Road road)
    {
        this.name = name;
        this.entryRate = entryRate;
        this.road = road;
        this.random = new Random();
    }

    public void run()
    {
        try {
            while (true) {
                // Sleep based on the entry rate (cars per hour)
                int hour = 1000 * 60 * 6; // 6 minutes would represent the 60 minutes
                sleep( hour / entryRate);

                // generate a random destination
                // String destination = generateRandomDestination();
                CarPark destinationParking = generateRandomDestination();
                String destination = destinationParking.getParkingName(); 

                // Create a new car and add it to the road
                long time = System.currentTimeMillis();
                Car car = new Car(destination, time);
                // System.out.println("The car has been generated at EntryPoint with destination of " + car.getDestination());
                road.addCar(car);

                road.checkCar(destination);
            }
        }
        catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }
    }

    // private String generateRandomDestination()
    // {
    //     int randomValue = random.nextInt(totalWeight);
    //     String randomDestination;

    //     if (randomValue < 10) {
    //         randomDestination = "University";
    //     }
    //     else if (randomValue < 30) {
    //         randomDestination = "Station";
    //     }
    //     else if (randomValue < 60) {
    //         randomDestination = "ShoppingCentre";
    //     }
    //     else {
    //         randomDestination = "IndustrialPark";
    //     }

    //     return randomDestination;
    // }

    private CarPark generateRandomDestination()
    {
        int rand = random.nextInt(totalWeight);
        CarPark parking;

        if (rand < 10) {
            parking = new CarPark("University", 100, road);
        }
        else if (rand < 30) {
            parking = new CarPark("Station", 150, road);
        }
        else if (rand < 60) {
            parking = new CarPark("ShoppingCentre", 400, road);
        }
        else {
            parking = new CarPark("IndustrialPark", 1000, road);
        }

        return parking;
    }


    public String getEntryPointName()
    {
        return name;
    }
}
