
// import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class EntryPoint extends Thread {
    private String name;
    private int entryRate;
    private Random random;
    private Road road;
    private static final int totalWeight = 100;
    private int carsGeneratedCounter;
    private Clock clock;
    private static AtomicInteger totalCarsGenerated = new AtomicInteger(0);

    public EntryPoint(String name, int entryRate, Road road, Clock clock) {
        this.name = name;
        this.entryRate = entryRate;
        this.road = road;
        this.random = new Random();
        carsGeneratedCounter = 0;
        this.clock = clock;
    }

    public static int getCarsGenerated() {
        return totalCarsGenerated.get();
    }

    public void run() {
        while (clock.getRunningTicks()) {
            try {
                Thread.sleep(clock.fastTrackPerHour(entryRate));
                // Thread.sleep(655);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String destination = generateRandomDestination();
            long time = System.nanoTime();
            // Car car = new Car(destination, time);
            if (!road.isRoadFull()) {
                Car car = new Car(destination, time);
                road.addCar(car);
                totalCarsGenerated.incrementAndGet();
            }

        }
    }

    public synchronized void incrementCounter() {
        carsGeneratedCounter++;
    }

    public synchronized int getCars() {
        return carsGeneratedCounter;
    }

    private String generateRandomDestination() {
        int rand = random.nextInt(totalWeight);
        String finalDestination;

        if (rand < 10) {
            finalDestination = "University";
        } else if (rand < 30) {
            finalDestination = "Station";
        } else if (rand < 60) {
            finalDestination = "ShoppingCentre";
        } else {
            finalDestination = "IndustrialPark";
        }

        return finalDestination;
    }

    // public String getEntryPointName() {
    //     return name;
    // }
}
