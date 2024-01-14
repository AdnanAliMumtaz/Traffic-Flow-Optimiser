import java.util.concurrent.TimeUnit;

class Clock extends Thread {
    private long currentTime;
    private final long tickDuration;
    private boolean running;
    private long runDuration; // New variable to store the desired run duration

    public Clock(long tickDuration) {
        this.tickDuration = tickDuration;
        this.currentTime = 0;
        this.running = true;
        this.runDuration = 0;
    }

    public void setRunDuration(long runDuration) {
        this.runDuration = runDuration;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();

        while (running && currentTime < runDuration) {
            try {
                TimeUnit.NANOSECONDS.sleep(1000000000); // Sleep for 1 second (nano granularity)
                currentTime = System.nanoTime() - startTime; // Increment simulated time
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public long getCurrentTime() {
        long currentTimeInSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        return currentTimeInSeconds;
    }

    public long fastTrackPerHour(int originalRate) {
        return 3600000 / (originalRate * 10);
    }

    public long fastTrackPerSeconds(long value) {

        //Chaning the Code
        long t = TimeUnit.SECONDS.toMillis((long) (value * 0.1));

        long result = t * 1000;

        return (long) (value * 0.1 * 1000);
    }
    
    public long fastTrackPerMinutes(int value)
    {
        long sleepDuration = 60000 / (value * 10);
        return sleepDuration;        
    }

    public boolean getRunningTime()
    {
        return getCurrentTime() < TimeUnit.NANOSECONDS.toSeconds(runDuration);
    }

    public void stopThread() {
        running = false;
    }

    public void outputElapsedTime() {
        double elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        double elapsedMinutes = elapsedSeconds / 6;

        // Extract minutes and seconds
        int minutes = (int) elapsedMinutes;
        int seconds = (int) ((elapsedMinutes - minutes) * 60);

        System.out.println("Desired Simulated Time: " + TimeUnit.NANOSECONDS.toSeconds(runDuration) + " seconds");

        System.out.println("Actual Elapsed Time: " + minutes + " minutes " + seconds + " seconds");
    }

    public int getCurrentMinutes()
    {
        double elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        double elapsedMinutes = elapsedSeconds / 6;

        // Extract minutes and seconds
        int minutes = (int) elapsedMinutes;
        // int seconds = (int) ((elapsedMinutes - minutes) * 60);

        // System.out.println("Actual Elapsed Time: " + minutes + " minutes " + seconds + " seconds");

        return minutes;
    }

    public int getCurrentSeconds()
    {
        double elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        double elapsedMinutes = elapsedSeconds / 6;

        // Extract minutes and seconds
        int minutes = (int) elapsedMinutes;
        int seconds = (int) ((elapsedMinutes - minutes) * 60);

        // System.out.println("Actual Elapsed Time: " + minutes + " minutes " + seconds + " seconds");

        return seconds;
    }

}