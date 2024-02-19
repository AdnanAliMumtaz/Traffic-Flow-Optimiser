package LockhartModel;

class Clock extends Thread {
    private int runDuration;
    private int ticks;
    private int seconds;
    private int minutes;

    public Clock(int fastMinutes) {
        this.runDuration = (fastMinutes * 6) * 10;
        this.ticks = 0;
        this.seconds = 0;
        this.minutes = 0;
    }

    // public void setSpedUpMinutes(int fastMinutes) {
    //     this.runDuration = (fastMinutes * 6) * 10;
    // }

    @Override
    public void run() {
        while (ticks < runDuration) {
            try {

                // Simulates the 1 seconds in real time - while sleep for 100 miliseconds
                Thread.sleep(100);

                // Increment seconds and update minutes if necessary
                this.seconds = (this.seconds + 1) % 60;
                this.minutes += (this.seconds == 0) ? 1 : 0;

                // Increment tick count
                updateTicks();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            // Check if the current tick is a multiple of 6
            if (getTick() % 600 == 0) {
                System.out.println("Time: " + getCurrentMinutes() + "m ");
                CarPark.reportAllParkingSpaces();
                System.out.print("\n");
            }
        }
    }

    public int getCurrentMinutes() {
        return minutes;
    }

    public int getCurrentSeconds() {
        return this.seconds;
    }

    public void updateTicks() {
        this.ticks++;
    }

    public long fastTrackPerHour(int originalRate) {
        return 3600000 / (originalRate * 10);
    }

    public long fastTrackPerSeconds(long value) {
        return (long) (value * 0.1 * 1000);
    }

    public long fastTrackPerMinutes(int value) {
        long sleepDuration = 60000 / (value * 10);
        return sleepDuration;
    }

    public boolean getRunningTicks() {
        return ticks < runDuration;
    }

    public synchronized int getTick() {
        return ticks;
    }
}