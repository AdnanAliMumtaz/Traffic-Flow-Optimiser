
// Not a thread
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
        boolean isRoad = true;
        if (isFull())
        {
            isRoad = false;
        }

        roadBuffer[rear] = car;
        rear = (rear++) % capacity;
        size++;

        System.out.println("The car has arrived on the road: " + car.getDestination() );

        return isRoad;
    }

    public synchronized Car removeCar()
    {
        if (isEmpty())
        {
            return null;
        }

        Car removedCar = roadBuffer[front];
        roadBuffer[front] = null;
        front = (front++) % capacity;
        size--;

        return removedCar;
    }

    public synchronized boolean isFull()
    {
        return size == capacity;
    }

    public synchronized boolean isEmpty()
    {
        return size == 0;
    }

    public synchronized Car[] getCars()
    {
        return roadBuffer;
    }
}
