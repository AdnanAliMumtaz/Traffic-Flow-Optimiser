package LockhartModel;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

class Clock extends Thread {
    private long currentTime;
    // private final long tickDuration;
    private volatile boolean running;
    private int runDuration; // New variable to store the desired run duration
    private int ticks;
    
    public Clock() {
        // this.tickDuration = tickDuration;
        this.currentTime = 0;
        this.running = true;
        // this.runDuration = 0;
        this.ticks = 0;
    }

    public void setRunDurationInMinutest(int runDuration) {
        this.runDuration = runDuration * 60;
    }

    @Override
    public void run() {
        // long startTime = System.nanoTime();

        // while (running && currentTime < runDuration) {
        while (ticks < runDuration) {
            try {
                // TimeUnit.NANOSECONDS.sleep(1000000000); // Sleep for 1 second (nano granularity)
                Thread.sleep(1000); // Sleep for 1 second
                this.ticks++;
                // currentTime = System.nanoTime() - startTime; // Increment simulated time
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            // Check if the current tick is a multiple of 6
            if (getTick() % 60 == 0) {
                System.out.print("Time: " + getCurrentMinutes() + "m               ");
                CarPark.reportAllParkingSpaces();
                System.out.print("\n");
            }

        }
    }

    public synchronized long getCurrentTime() {
        long currentTimeInMilliseconds = TimeUnit.NANOSECONDS.toMillis(currentTime);
        return currentTimeInMilliseconds;
    }

    public synchronized long fastTrackPerHour(int originalRate) {
        return 3600000 / (originalRate * 10);
    }

    public synchronized long fastTrackPerSeconds(long value) {
        // Chaning the Code
        long t = TimeUnit.SECONDS.toMillis((long) (value * 0.1));
        long result = t * 1000;

        return (long) (value * 0.1 * 1000);
    }

    public synchronized long fastTrackPerMinutes(int value) {
        long sleepDuration = 60000 / (value * 10);
        return sleepDuration;
    }

    public synchronized boolean getRunningTicks() {
        // int currentTime = (int) TimeUnit.MILLISECONDS.toSeconds(getCurrentTime());
        // int timePassed = (int) TimeUnit.NANOSECONDS.toSeconds(runDuration);
        // return currentTime < timePassed;

        // return ticks < 360; // 6 minutes
        return ticks < runDuration;
    }

    public synchronized int getTick() {
        // ticks = (int) TimeUnit.MILLISECONDS.toSeconds(getCurrentTime());
        return ticks;
    }

    public synchronized int getCurrentMinutes() {
        // double elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        // double elapsedMinutes = elapsedSeconds / 6;

        // // Extract minutes and seconds
        // int minutes = (int) elapsedMinutes;
        // return minutes;

        return (int) getTick() / 6; // 6 second is one minute
    }

    public int getCurrentSeconds() {
        int currentTick = (int) getTick(); // Each tick is 10 seconds

        int currentMinute = currentTick / 6; // One minute
        // int currentSeconds = ;
        int currentSeconds = (currentTick % 6) * 10; // Calculate seconds within the current minute

        return currentSeconds;
    }
}