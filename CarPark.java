// import java.io.IOException;

import java.util.concurrent.TimeUnit;

public class CarPark extends Thread {
    private String name;
    private int capacity;
    private Road road;
    private Car[] carParkSpaces;
    private int occupiedSpaces;
    private int totalCarsParked;
    private long totalJourneyTime;
    private Clock clock;

    public CarPark(String name, int capacity, Road road, Clock clock)
    {
        this.name = name;
        this.capacity = capacity;
        this.road = road;
        this.carParkSpaces = new Car[capacity];
        this.occupiedSpaces = 0;
        this.totalCarsParked =0;
        this.totalJourneyTime = 0;
        this.clock = clock;
    }

    public void run() {
        // int iterations = 0;
        while (clock.getRunningTime()) {
            //Removing the car from the road
            admitCarFromRoad();

            try {
                // Sleep for 1.2 seconds representing 12 seconds
                // long fastTime = clock.fastTrackSeconds(12);
                Thread.sleep(clock.fastTrackPerSeconds(12));
            }
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    public synchronized void admitCarFromRoad() {
        if (!isCarParkFull()) {
            Car car = road.removeCar();
            if (car != null) {
                carParkSpaces[occupiedSpaces++] = car;
                car.parked(); // gives a car a timestamp
                System.out.println( name + "The car has been admitted with destination of " + car.getDestination());

                totalCarsParked++;
                totalJourneyTime += car.getJourneyTime();
            }
        }
    }

    public synchronized boolean isCarParkFull() {
        return occupiedSpaces >= capacity;
    }

    public String getParkingName()
    {
        return name;
    }

    public void report()
    {
        long averageJourneyTime = (totalCarsParked == 0) ? 0 : totalJourneyTime / totalCarsParked;
        
        long minutes = TimeUnit.MILLISECONDS.toMinutes(averageJourneyTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(averageJourneyTime) % 60;
        
        System.out.println(name + " " + occupiedSpaces + " Cars parked, average journey time " + minutes + "m" + seconds + "s");
    }
}
