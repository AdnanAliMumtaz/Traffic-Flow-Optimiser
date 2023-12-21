import java.io.IOException;

public class CarPark {
    private String name;
    private int capacity;
    private Car[] carParkSpaces;
    private int occupiedSpaces;

    public CarPark(String name, int capacity)
    {
        this.name = name;
        this.capacity = capacity;
        this.carParkSpaces = new Car[capacity];
        this.occupiedSpaces = 0;
    }

    public void run()
    {
        while (true)
        {
            removeAvailableCars();
            
            try {
                // Sleep for 12 seconds
                Thread.sleep(12000);

            }
            catch (InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    public synchronized void admitCar(Car car)
    {
        if (!isFull())
        {
            carParkSpaces[occupiedSpaces++] = car;
            car.parked(); // Indicates the car has been parked
        }
    }

    private synchronized void removeAvailableCars()
    {
        for (int i=0; i< occupiedSpaces; i++)
        {
            carParkSpaces[i] = null;
        }
        occupiedSpaces = 0;
    }

    public synchronized boolean isFull()
    {
        return occupiedSpaces == capacity;
    }
}
