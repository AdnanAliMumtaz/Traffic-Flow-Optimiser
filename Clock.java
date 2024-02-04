import java.sql.Time;
import java.util.concurrent.TimeUnit;

class Clock extends Thread {
    private long currentTime;
    private final long tickDuration;
    private volatile boolean running;
    private long runDuration; // New variable to store the desired run duration
    private int ticks;

    public Clock(long tickDuration) {
        this.tickDuration = tickDuration;
        this.currentTime = 0;
        this.running = true;
        this.runDuration = 0;
        this.ticks = 0;
    }

    public synchronized void setRunDuration(long runDuration) {
        this.runDuration = runDuration;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();

        while (running && currentTime < runDuration) {
            try {
                // TimeUnit.NANOSECONDS.sleep(1000000000); // Sleep for 1 second (nano
                // granularity)
                Thread.sleep(1000); // Sleep for 1 second
                currentTime = System.nanoTime() - startTime; // Increment simulated time
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            // Check if the current tick is a multiple of 6
            if (getTick() % 60 == 0) {
                CarPark.reportAllParkingSpaces();
            }

        }
    }

    public synchronized long getCurrentTime() {
        long currentTimeInMilliseconds = TimeUnit.NANOSECONDS.toMillis(currentTime);
        return currentTimeInMilliseconds;
    }

    public synchronized long fastTrackPerHour(int originalRate) {
        return 3600000 / (originalRate * 10);



        // // Given values
        // int totalCars = originalRate;
        // int totalMinutes = 6;

        // // Calculate cars per minute
        // double carsPerMinute = (double) totalCars / totalMinutes;

        // // Calculate cars per second
        // double carsPerSecond = carsPerMinute / 60;

        // // Calculate sleep duration in milliseconds
        // long sleepDurationMillis = (long) (1 / carsPerSecond * 1000);
        // return sleepDurationMillis;
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

    public boolean getRunningTime() {
        return getCurrentTime() < TimeUnit.NANOSECONDS.toMillis(runDuration);
    }

    public boolean getRunningTicks() {
        int currentTime = (int) TimeUnit.MILLISECONDS.toSeconds(getCurrentTime());
        int timePassed = (int) TimeUnit.NANOSECONDS.toSeconds(runDuration);

        return currentTime < timePassed;
    }

    public synchronized int getTick() {
        ticks = (int) TimeUnit.MILLISECONDS.toSeconds(getCurrentTime());
        return ticks;
    }

    public synchronized void stopThread() {
        running = false;
    }

    public synchronized void outputElapsedTime() {
        double elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        double elapsedMinutes = elapsedSeconds / 6;

        // Extract minutes and seconds
        int minutes = (int) elapsedMinutes;
        int seconds = (int) ((elapsedMinutes - minutes) * 60);

        System.out.println("Desired Simulated Time: " + TimeUnit.NANOSECONDS.toSeconds(runDuration) + " seconds");
        System.out.println("Actual Elapsed Time: " + minutes + " minutes " + seconds + " seconds");
    }

    public synchronized int getCurrentMinutes() {
        double elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        double elapsedMinutes = elapsedSeconds / 6;

        // Extract minutes and seconds
        int minutes = (int) elapsedMinutes;
        return minutes;
    }

    public void getReportingOnSpaces(CarPark[] destination) {
        for (CarPark des : destination) {
            des.reportParkingSpaces();
        }
    }

    public synchronized int getCurrentSeconds() {
        double elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        double elapsedMinutes = elapsedSeconds / 6;

        // Extract minutes and seconds
        int minutes = (int) elapsedMinutes;
        int seconds = (int) ((elapsedMinutes - minutes) * 60);

        // System.out.println("Actual Elapsed Time: " + minutes + " minutes " + seconds
        // + " seconds");

        return seconds;
    }
}