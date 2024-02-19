package LockhartModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CarPark extends Thread {
    
    // List to store all car parks
    private static final List<CarPark> carParks = new ArrayList<>();
    
    private String name;
    private int capacity;
    private Road entryRoad;
    private Car[] carParkSpaces;
    private int occupiedSpaces;
    private int carsParked;
    private long totalJourneyTime;
    private Clock clock;
    private static AtomicInteger totalCarsParked = new AtomicInteger(0);

    public CarPark(String name, int capacity, Road road, Clock clock) {
        this.name = name;
        this.capacity = capacity;
        this.entryRoad = road;
        this.carParkSpaces = new Car[capacity];
        this.occupiedSpaces = 0;
        this.carsParked = 0;
        this.totalJourneyTime = 0;
        this.clock = clock;

        // Adds every instance to the list
        carParks.add(this);
    }

    public void run() {
        while (clock.getRunningTicks()) {

            // Sleeps to simulate car arrival
            simulateCarArrival();

            // Removing the car from the road
            admitCarFromRoad();
        }
    }

    private void simulateCarArrival() {
        try {
            Thread.sleep(clock.fastTrackPerSeconds(12));
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static List<CarPark> getCarParks() {
        return carParks;
    }

    public synchronized static void reportAllParkingSpaces() {
        for (CarPark carPark : carParks) {
            carPark.reportParkingSpaces();
        }
    }

    public void admitCarFromRoad() {

        if (!isCarParkFull()) // Checks if there is a space in the carpark
        {
            if (!entryRoad.isRoadEmpty())  // Checks if there is a car on road
            {
                // Removes a car from the road
                Car car = entryRoad.removeCar();

                if (car != null) {
                    carParkSpaces[occupiedSpaces++] = car;
                    increment();
                    totalCarsParked.incrementAndGet();

                    car.parked(); // gives a car a timestamp

                    // Adds all of the total journey time
                    totalJourneyTime += car.getTotalJourneyTime();
                }
            }
        }
    }

    private synchronized void increment()
    {
        carsParked++;
    }

    public static int getTotalCarsParked() {
        return totalCarsParked.get();
    }

    // Report parking spaces
    public synchronized void reportParkingSpaces() {
        int availableSpaces = capacity - occupiedSpaces;
        System.out.print(name + ":                     "
                + String.format("%03d", availableSpaces) + " Spaces \n");
    }

     // Check if car park is full
    public boolean isCarParkFull() {
        return occupiedSpaces >= capacity;
    }

    // Get parking name
    public String getParkingName() {
        return name;
    }

    // Report method
    public void report() {
        // Calculate average journey time per car (in nanoseconds)
        long averageJourneyTimePerCar = (carsParked == 0) ? 0 : totalJourneyTime / carsParked;

        // Delay the average journey time by 10 times
        long delayedJourneyTime = averageJourneyTimePerCar * 10;

        // Convert delayed journey time to seconds (assuming totalJourneyTime is in
        // nanoseconds)
        long elapsedSeconds = delayedJourneyTime / 1_000_000_000; // Convert nanoseconds to seconds
        long elapsedMinutes = elapsedSeconds / 60;
        elapsedSeconds = elapsedSeconds % 60; // Calculate remaining seconds after minutes

        System.out.println(name + " " + occupiedSpaces + " Cars parked, average journey time " + elapsedMinutes + "m"
                + elapsedSeconds + "s");
    }
}
