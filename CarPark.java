// import java.io.IOException;

public class CarPark extends Thread {
    private String name;
    private int capacity;
    private Road road;
    private Car[] carParkSpaces;
    private int occupiedSpaces;

    public CarPark(String name, int capacity, Road road)
    {
        this.name = name;
        this.capacity = capacity;
        this.road = road;
        this.carParkSpaces = new Car[capacity];
        this.occupiedSpaces = 0;
    }

    public void run() {
        while (true) {
            //Removing the car from the road
            admitCarFromRoad();

            try {
                // Sleep for 2 seconds representing 12 seconds
                Thread.sleep(2000);
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
                System.out.println("The car has been admitted with destination of " + car.getDestination());
            }
        }
    }

    public synchronized boolean isCarParkFull() {
        return occupiedSpaces == capacity;
    }

    public String getParkingName()
    {
        return name;
    }
}
