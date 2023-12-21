import java.io.IOException;
import java.util.Random;

// Thread - Need more work
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
                int hour = 1000 * 60 * 60;
                sleep( hour / entryRate);

                // generate a random destination
                String destination = generateRandomDestination();

                // Create a new vechile and add it to the road
                Car car = new Car(destination, 3); // need more work
                road.addCar(car);
            }
        }
        catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }
    }

    private String generateRandomDestination()
    {
        int randomValue = random.nextInt(totalWeight);
        String randomDestination;

        if (randomValue < 10)
        {
            randomDestination = "University";
        }
        else if (randomValue < 30)
        {
            randomDestination = "Station";
        }
        else if (randomValue < 60)
        {
            randomDestination = "ShoppingCentre";
        }
        else {
            randomDestination = "IndustrialPark";
        }



        // int generatedNumber;
        //     if (i < 1)
        //     {
        //         generatedNumber = 0;
        //     }
        //     else if (i < 3)
        //     {
        //         generatedNumber = 1;
        //     }
        //     else if (i < 6)
        //     {
        //         generatedNumber = 2;
        //     }
        //     else{ 
        //         generatedNumber = 3;
        //     }


        return randomDestination;
    }
}
