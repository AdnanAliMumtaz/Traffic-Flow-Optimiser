
// It is a shared memory space - Circular Buffer 
public class Road {
    private Car[] roadBuffer;
    private int capacity;
    private int front;
    private int rear;
    private int size;
    private boolean valueSet = false;
    
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
        while (size == capacity) {
            // when the road is full
            try {wait(); } catch(InterruptedException e) {}
        }
    
        // Check if the space is available
        if (isRoadFull())
        {
            return false;
        }

        // Add the car and increase the rear index with size
        roadBuffer[rear] = car;
        rear = (rear+ 1) % capacity;
        size++;

        // Just testing
        checkCar(car.getDestination());
        
        // when atleast one item is available
        if (size > 0)
        {
            // when atleast one item available.
            notifyAll();
        }

        return true;
    }

    // Testing function
    public void checkCar(String destination)
    {
        if (rear-1 >= 0 && rear-1 < capacity)
        {
            if (roadBuffer[rear-1].getDestination() == destination)
            {
                System.out.println("The car has arrived on the road: " + destination );
            }
        }
    }

    public synchronized Car removeCar()
    {
        while (size == 0){
            // when road is empty
            try {wait(); } catch(Exception e) {}
        }

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

        // valueSet = false;
        if (size < capacity) {
            // Notify the producer that a space is available in the buffer
            notifyAll();
        }

        return removedCar;
    }

    public synchronized boolean isRoadFull()
    {
        return size == capacity;
    }

    public synchronized boolean isRoadEmpty()
    {
        return size == 0;
    }

    public synchronized Car[] getCars()
    {
        return roadBuffer;
    }
}
