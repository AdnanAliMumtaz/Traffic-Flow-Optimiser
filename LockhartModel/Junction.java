package LockhartModel;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Junction extends Thread {
    private String name;
    private int greenLightTime;
    private int carCounting;
    private int carRemaining;
    private Map<String, Road> entries;
    private Map<String, Road> exits;
    private Logger logger;
    private String[] lightSequence;
    private Clock clock;
    boolean islocked;

    public Junction(String name, int greenTime, Clock clock, String[] sequence) {
        this.name = name;
        this.greenLightTime = greenTime;
        this.clock = clock;
        this.carCounting = 0;
        this.carRemaining = 0;
        this.entries = new HashMap<>();
        this.exits = new HashMap<>();
        this.lightSequence = sequence;
        this.islocked = false;

        // Initialise logger
        logInitialiser();
    }

    // Logger initialiser
    private void logInitialiser() {
        this.logger = Logger.getLogger(Junction.class.getName() + "." + name);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        try {
            String packageName = getClass().getPackage().getName();
            String filePath = packageName.replace(".", "/") + "/" + name + "_log.txt";
            FileHandler fileHandler = new FileHandler(filePath, false);
            fileHandler.setFormatter(new CustomFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom logger formatter
    private static class CustomFormatter extends SimpleFormatter {
        @Override
        public String format(LogRecord record) {
            return record.getMessage() + "\n";
        }
    }

    // Set the entry roads for junction
    public void setEntry(String name, Road road) {
        entries.put(name, road);
    }

    // Set the exit roads for junction
    public void setExit(String name, Road road) {
        exits.put(name, road);
    }

    public void run() {
        while (clock.getRunningTicks()) {

            // Simulate random delay (to produce accruate timing in log reports)
            try {
                sleep((int) (Math.random() * 5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Handling car movement
            CarMovement();
        }
    }

    private void CarMovement() {

        // Runs through a specified sequence
        for (String value : lightSequence) {
            if (!clock.getRunningTicks()) {
                break;
            }
            
            // Getting the sequence road
            Road entryRoad = entries.get(value);

            // Calculating the green light time
            int start = clock.getTick();
            int end = start + (greenLightTime);
            int sleepingTimeInSeconds = 5;

            // Performing the green light operation 
            while (clock.getTick() < end && clock.getRunningTicks()) // Green light
            {
                moveCarsDuringGreenLight(entryRoad, end, sleepingTimeInSeconds);
            }

            // Sets the remaining cars on the entry Road to write in log file
            this.carRemaining = entryRoad.getCarsAmount();
            logActivity(value, islocked);

            // Resets the count of cars
            carCounting = 0;
        }
    }

    private void moveCarsDuringGreenLight(Road entryRoad, int end, int sleepingTimeInSeconds) {
        while ((clock.getTick() + sleepingTimeInSeconds) < end && clock.getTick() < end && clock.getRunningTicks()) {
            
            // Sleeps to simulate car going
            simulateCarGoing();

            // Checks if there is a car on road to move it to next road
            if (!entryRoad.isRoadEmpty()) {
                moveCarToExitRoad(entryRoad);
            }
        }
    }

    private void moveCarToExitRoad(Road entryRoad) {

        // Gets the destination
        String removedCar = entryRoad.peek().getDestination();
        
        // Gets the exit road
        Road exitRoad = getExitRoadForDestination(removedCar);

        // Checks if the exit has a space
        if (exitRoad != null && !exitRoad.isRoadFull()) {
            Car carAdd = entryRoad.removeCar();
            if (carAdd != null) {

                //Adds car to the road
                exitRoad.addCar(carAdd);
                carCounting++;

                // This is to keep track of a gridlock
                islocked = false;
            }
        } else if (exitRoad.isRoadFull()) {
            // This is to keep track of a gridlock
            islocked = true;
        }
    }

    private void simulateCarGoing()
    {
        // Simulates sleeping for 12 simulated seconds 
        try {
            Thread.sleep(clock.fastTrackPerMinutes(12));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Road getExitRoadForDestination(String destination) {
        switch (name) {
            case "A":
                return ("IndustrialPark" == destination) ? exits.get("West") : exits.get("North");
            case "B":
                return ("IndustrialPark" == destination) ? exits.get("South") : exits.get("North");
            case "C":
                if ("IndustrialPark" == destination)
                    return exits.get("South");
                else if ("ShoppingCentre" == destination)
                    return exits.get("West");
                else
                    return exits.get("North");
            case "D":
                return ("University" == destination) ? exits.get("North") : exits.get("South");
            default:
                return null;
        }
    }

    private synchronized void logActivity(String direction, boolean isLocked) {
        String logMessage = "Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction "
                + name + ": " + carCounting + " cars through from " + direction + ", " + this.carRemaining
                + " cars waiting.";

        if (this.carRemaining > 0 && isLocked) {
            logMessage += " GRIDLOCK";
        }

        logger.info(logMessage);
    }
}