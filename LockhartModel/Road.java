package LockhartModel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

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
    private static AtomicInteger getTotalCarsOnRoad = new AtomicInteger(0);
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

            getTotalCarsOnRoad.incrementAndGet();
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

            getTotalCarsOnRoad.decrementAndGet();
            // totalCars--;

            // Notify the producer that a space is available in the buffer
            notFull.signal();

            return removedCar;
        } finally {
            lock.unlock();
        }
    }

    public static int getTotalCount() {
        return totalCars;
        // return count;
    }

    public boolean isRoadFull() {
        return size == capacity;
    }    

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
    

    public boolean isRoadEmpty() {
        return size == 0;
    }

    public Car[] getCars() {
        return roadBuffer.clone();
    }

    public static int getTotalCarsQueued() {
        // return totalCars;
        return getTotalCarsOnRoad.get() - totalCars;
    }

    public int getCarsAmount() {
        // return roadBuffer.length;
        return size;
    }

    // Testing function
    public void checkCar(String destination) {

        System.out.println("The car has arrived on the road: " + destination);
    }
}