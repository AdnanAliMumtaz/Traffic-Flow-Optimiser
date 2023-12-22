
// It is a shared memory space - Circular Buffer 
public class Road {
    private Car[] roadBuffer;
    private int capacity;
    private int front;
    private int rear;
    private int size;
    
    public Road(int capacity)
    {
        this.roadBuffer = new Car[capacity];
        this.capacity = capacity;
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    public synchronized boolean addCar(Car car)
    {
        // Check if the space is available
        if (isRoadFull())
        {
            return false;
        }

        // Add the car and increase the rear index with size
        roadBuffer[rear] = car;
        rear = (rear+ 1) % capacity;
        size++;

        return true;
    }

    // Testing function
    public void checkCar(String destination)
    {
        if (roadBuffer[rear-1].getDestination() == destination)
        {
            System.out.println("The car has arrived on the road: " + destination );
        }
    }

    public synchronized Car removeCar()
    {
        // Check if it is empty
        if (isRoadEmpty())
        {
            return null;
        }

        // Remove car, and 
        Car removedCar = roadBuffer[front];
        roadBuffer[front] = null;
        front = (front + 1) % capacity;
        size--;

        return removedCar;
    }

    public synchronized boolean isRoadFull()
    {
        return size >= capacity;
    }

    public synchronized boolean isRoadEmpty()
    {
        return size <= 0;
    }

    public synchronized Car[] getCars()
    {
        return roadBuffer;
    }
}
