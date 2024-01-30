// import java.io.IOException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CarPark extends Thread {
    private String name;
    private int capacity;
    private Road road;
    private Car[] carParkSpaces;
    private int occupiedSpaces;
    private int carsParked;
    private long totalJourneyTime;
    private Clock clock;

    // private static int totalCarsParked = 0;
    private static AtomicInteger totalCarsParked = new AtomicInteger(0);
    // private boolean printedForCurrentMinute;

    public CarPark(String name, int capacity, Road road, Clock clock) {
        this.name = name;
        this.capacity = capacity;
        this.road = road;
        this.carParkSpaces = new Car[capacity];
        this.occupiedSpaces = 0;
        this.carsParked = 0;
        this.totalJourneyTime = 0;
        this.clock = clock;
    }

    public void run() {
        // int iterations = 0;
        while (clock.getRunningTicks()) {
            try {
                Thread.sleep(clock.fastTrackPerSeconds(12));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            // Removing the car from the road
            admitCarFromRoad();
        }
    }

    public void admitCarFromRoad() {
        if (!isCarParkFull()) {
            if (!road.isRoadEmpty()) {
                Car car = road.removeCar();
                if (car != null) {
                carParkSpaces[occupiedSpaces++] = car;
                car.parked(); // gives a car a timestamp
                // System.out.println( name + " has been admitted with destination of " +
                // car.getDestination() + " " + car.getJourneyTime());

                carsParked++;
                // totalCarsParked++;
                totalCarsParked.incrementAndGet();
                totalJourneyTime += car.getJourneyTime();
                }
            }
        }
    }

    // public static int getTotalCarsParked() {
    // return totalCarsParked;
    // }

    public static int getTotalCarsParked() {
        return totalCarsParked.get();
    }

    private void reportParkingSpaces() {
        int availableSpaces = capacity - occupiedSpaces;
        System.out.println("Time: " + clock.getCurrentMinutes() + "m               " + name + ":                     "
                + String.format("%03d", availableSpaces) + " Spaces");
    }

    public synchronized boolean isCarParkFull() {
        return occupiedSpaces >= capacity;
    }

    public String getParkingName() {
        return name;
    }

    public void report() {
        long averageJourneyTime = (carsParked == 0) ? 0 : totalJourneyTime / carsParked;

        int elapsedSeconds = (int) averageJourneyTime;
        int elapsedMinutes = elapsedSeconds / 60;

        // System.out.println( occupiedSpaces + " " + carsParked );

        // int i = 0;
        // for (Car car : carParkSpaces)
        // {
        // if (car != null)
        // {
        // i++;
        // }
        // }

        // System.out.println(i);
        System.out.println(name + " " + occupiedSpaces + " Cars parked, average journey time " + elapsedMinutes + "m"
                + elapsedSeconds + "s");

        // synchronized (this) {
        // long averageJourneyTime = (carsParked == 0) ? 0 : totalJourneyTime /
        // carsParked;

        // int elapsedSeconds = (int) averageJourneyTime;
        // int elapsedMinutes = elapsedSeconds / 60;

        // System.out.println(name + " " + occupiedSpaces + " Cars parked, average
        // journey time " +
        // String.format("%dm%ds", elapsedMinutes, elapsedSeconds));
        // }
    }
}
