// import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CarPark extends Thread {
    private static final List<CarPark> carParks = new ArrayList<>();

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

        carParks.add(this);
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

            // // Check if the current tick is a multiple of 6
            // if (clock.getTick() % 6 == 0) {
            // // reportParkingSpaces();
            // System.out.println(clock.getTick());
            // }
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
        if (!isCarParkFull()) {
            if (!road.isRoadEmpty()) {
                Car car = road.removeCar();
                if (car != null) {
                    carParkSpaces[occupiedSpaces++] = car;
                    car.parked(); // gives a car a timestamp

                    carsParked++;
                    // totalCarsParked++;
                    totalCarsParked.incrementAndGet();
                    totalJourneyTime += car.getJourneyTime();
                }
            }
        }
    }

    public static int getTotalCarsParked() {
        return totalCarsParked.get();
    }

    public void reportParkingSpaces() {
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

        System.out.println(name + " " + occupiedSpaces + " Cars parked, average journey time " + elapsedMinutes + "m"
                + elapsedSeconds + "s");
    }
}
