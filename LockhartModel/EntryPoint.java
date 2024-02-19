package LockhartModel;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class EntryPoint extends Thread {
    private String name;
    private int entryRate;
    private Random random;
    private Road road;
    private static final int totalWeight = 100;
    private Clock clock;
    private static AtomicInteger totalCarsGenerated = new AtomicInteger(0);

    public EntryPoint(String name, int entryRate, Road road, Clock clock) {
        this.name = name;
        this.entryRate = entryRate;
        this.road = road;
        this.clock = clock;
        this.random = new Random();
    }

    public void run() {
        while (clock.getRunningTicks()) {
            simulateCarDeparture();

            if (!road.isRoadFull()) { // Checks if there is a space on the road

                // Generates the location
                String destination = generateRandomDestination();

                // Adding the car to the road
                // int minutes = clock.getCurrentMinutes();
                // int seconds = clock.getCurrentSeconds();
                // int time = (minutes * 60) + seconds;

                addCarToRoad(destination);
            }

        }
    }

    // Method to simulate car arrival
    private void simulateCarDeparture() {
        try {
            // Sleep for a duration based on entry rate
            Thread.sleep(clock.fastTrackPerHour(entryRate));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to add car to road
    private void addCarToRoad(String destination) {

        // Creating a car
        long startingTime = System.nanoTime();
        Car car = new Car(destination, startingTime);

        // Adding the car to the road
        road.addCar(car);

        // Increment the number of cars generated
        totalCarsGenerated.incrementAndGet();
    }

    private String generateRandomDestination() {
        int rand = random.nextInt(totalWeight);
        String finalDestination;

        // Assign destination based on weights
        if (rand < 10) { // 10%
            finalDestination = "University";
        } else if (rand < 30) { // 20%
            finalDestination = "Station";
        } else if (rand < 60) { // 30%
            finalDestination = "ShoppingCentre";
        } else { // 40%
            finalDestination = "IndustrialPark";
        }

        return finalDestination;
    }

    // Getter for total number of cars generated
    public static int getTotalCarsGenerated() {
        return totalCarsGenerated.get();
    }
}
