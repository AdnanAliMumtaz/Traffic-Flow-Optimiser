package LockhartModel;
import java.util.concurrent.atomic.AtomicInteger;
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
    private static AtomicInteger getTotalCarsOnRoad = new AtomicInteger(0);

    public Road(int capacity) {
        this.roadBuffer = new Car[capacity];
        this.capacity = capacity;
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    // Method to add a car to the road
    public boolean addCar(Car car) {
        lock.lock(); // Acquire the lock
        try {
            while (isRoadFull()) { // If road is full, then wait for the consumer to consume
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

            // Increment total cars on the road
            getTotalCarsOnRoad.incrementAndGet();

            // Notify the consumer that there is a car available
            notEmpty.signal();

            return true;
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Method to remove a car from the road
    public Car removeCar() {
        lock.lock(); // Acquire the lock
        try {
            while (isRoadEmpty()) { // If the road is empty, then wait for producer to produce
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Remove the car and update front index and size
            Car removedCar = roadBuffer[front];
            roadBuffer[front] = null;
            front = (front + 1) % capacity;
            size--;

            // Decrement total cars on the road
            getTotalCarsOnRoad.decrementAndGet();

            // Notify the producer that a space is available in the buffer
            notFull.signal();

            return removedCar;
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Method to check if the road is full
    public boolean isRoadFull() {
        return size == capacity;
    }

    // Method to peek at the first car on the road
    public Car peek() {
        Car firstCar = null;
        try {
            lock.lock(); // Acquire mutual exclusion

            if (size > 0) {
                firstCar = roadBuffer[front];
            }

        } finally {
            lock.unlock(); // Release mutual exclusion
        }

        return firstCar;
    }

    // Method to check if the road is empty
    public boolean isRoadEmpty() {
        return size == 0;
    }

    // Method to get the current number of cars on the road
    public int getCarsAmount() {
        return size;
    }

    // Method to get the total number of cars on the road
    public static int getTotalCarsQueued() {
        return getTotalCarsOnRoad.get();
    }
}