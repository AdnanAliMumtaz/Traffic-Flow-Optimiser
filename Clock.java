import java.util.concurrent.TimeUnit;

// public class Clock {
//     private long startTime;
//     private long currentTime;
//     private int ticks;

//     public Clock()
//     {
//         startTime = System.nanoTime();
//         this.currentTime = 0;
//         this.ticks = ticks;
//     }

//     public synchronized void incrementTime()
//     {
//         currentTime++;
//     }

//     private long startTime;
//     private long tickDuration; // The duration of each tick in nanoseconds

//     public Clock(long tickDuration) {
//         this.startTime = System.nanoTime();
//         this.tickDuration = tickDuration;
//     }

//     public long getCurrentTime() {
//         return System.nanoTime() - startTime;
//     }

//     public int getCurrentTick() {
//         return (int) (getCurrentTime() / tickDuration);
//     }

//     public long getTickDuration()
//     {
//         return tickDuration;
//     }

// }

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
        // return currentTime;
        long currentTimeInSeconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
        return currentTimeInSeconds;
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
}

// class HelloWorld {
//     public static void main(String[] args) {
//         Clocking clock = new Clocking(TimeUnit.SECONDS.toNanos(1));
//         clock.setRunDuration(TimeUnit.SECONDS.toNanos(30)); // Set the desired run duration

//         clock.start();

//         try {
//             clock.join(); // Wait for the clock thread to finish
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         clock.outputElapsedTime();
//     }
// }
