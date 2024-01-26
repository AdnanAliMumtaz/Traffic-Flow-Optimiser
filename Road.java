import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Road {
    private Car[] roadBuffer;
    private int capacity;
    private int front;
    private int rear;
    private int size;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private int count;

    private static int getTotalCarsOnRoad = 0;
    private static int totalCars = 0;

    public Road(int capacity) {
        this.roadBuffer = new Car[capacity];
        this.capacity = capacity;
        this.front = 0;
        this.rear = 0;
        this.size = 0;

        this.count = 0;
    }

    public boolean addCar(Car car) {
        lock.lock();
        try {
            while (isRoadFull()) {
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Add the car and increase the rear index with size
            roadBuffer[rear] = car;
            rear = (rear + 1) % capacity;
            size++;
            count++;

            // Just testing
            // checkCar(car.getDestination());

            getTotalCarsOnRoad++;
            // totalCars++;
            

            // Notify the consumer that there is a car available
            notEmpty.signal();

            return true;
        } finally {
            lock.unlock();
        }
    }

    public Car removeCar() {
        lock.lock();
        try {
            while (isRoadEmpty()) { // If the road is empty
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Remove car
            Car removedCar = roadBuffer[front];
            roadBuffer[front] = null;
            front = (front + 1) % capacity;
            size--;

            getTotalCarsOnRoad--;
            // totalCars--;

            // Notify the producer that a space is available in the buffer
            notFull.signal();

            return removedCar;
        } finally {
            lock.unlock();
        }
    }

    public int getTotalCount()
    {
        return count;
    }

    public boolean isRoadFull() {
        return size == capacity;
    }

    public boolean isRoadEmpty() {
        return size == 0;
    }

    public Car[] getCars() {
        return roadBuffer.clone();
    }

    public Car peekCar() {
        lock.lock();
        try {
            if (isRoadEmpty()) {
                return null; // Road is empty, nothing to peek
            }

            // Peek at the first car without removing it
            return roadBuffer[front];
        } finally {
            lock.unlock();
        }
    }

    public static int getTotalCarsQueued()
    {
        return getTotalCarsOnRoad;
    }

    public synchronized int getCarsAmount() {
        // return roadBuffer.length;
        return size;
    }

    // Testing function
    public void checkCar(String destination) {

        System.out.println("The car has arrived on the road: " + destination);
    }
}
