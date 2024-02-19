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
    private int carsRemaining;
    private Map<String, Road> entries;
    private Map<String, Road> exits;
    private Logger logger;
    private String[] lightSequence;
    private Clock clock;

    private int counter = 0;

    public Junction(String name, int greenTime, Clock clock, String[] sequence) {
        this.name = name;
        this.greenLightTime = greenTime;
        this.clock = clock;
        this.carCounting = 0;
        this.carsRemaining = 0;
        this.entries = new HashMap<>();
        this.exits = new HashMap<>();
        this.lightSequence = sequence;

        // Initialise logger
        logInitialiser();
    }

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

    private static class CustomFormatter extends SimpleFormatter {
        @Override
        public String format(LogRecord record) {
            return record.getMessage() + "\n";
        }
    }

    public void setEntry(String name, Road road) {
        entries.put(name, road);
    }

    public void setExit(String name, Road road) {
        exits.put(name, road);
    }

    public void run() {
        while (clock.getRunningTicks()) {
            try {
                sleep((int) (Math.random() * 5));
            } catch (InterruptedException ex) {
                
            }

            CarMovement();
        }
    }

    private void CarMovement() {
        long sleepingTime = 500;
        boolean islocked = true;
        for (String value : lightSequence) {
            if (!clock.getRunningTicks()) {
                break;
            }

            int start = clock.getTick();
            int end = start + (greenLightTime);
            Road entryRoad = entries.get(value);
            int sleepingTimeInSeconds = 5; 

            while (clock.getTick() < end && clock.getRunningTicks()) { // Green Light Time Loop

                while ((clock.getTick() + sleepingTimeInSeconds) < end && clock.getTick() < end && clock.getRunningTicks()) {

                    try {
                        Thread.sleep(clock.fastTrackPerMinutes(12));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!entryRoad.isRoadEmpty()) {
                        String removedCar = entryRoad.peek().getDestination();
                        Road exitRoad = getExitRoadForDestination(removedCar);
                        if (exitRoad != null && !exitRoad.isRoadFull()) {
                            Car carAdd = entryRoad.removeCar();
                            if (carAdd != null) {
                                exitRoad.addCar(carAdd);
                                carCounting++;
                            }
                        }
                    }
                }
            }

            this.carsRemaining = entryRoad.getCarsAmount();

            logActivity(value, islocked);
            carCounting = 0;
        }
    }


    private void logActivity(String direction, boolean isLocked) {
        String logMessage = "Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction "
                + name + ": " + carCounting + " cars through from " + direction + ", " + this.carsRemaining
                + " cars waiting.";
        if (this.carsRemaining > 0 && isLocked) {
            logMessage += " GRIDLOCK";
        }

        logger.info(logMessage);
    }

    private boolean passCar(Road road, String car) {
        Road exitRoad = getExitRoadForDestination(car);
        if (exitRoad != null && !exitRoad.isRoadFull()) {
            Car carAdd = road.removeCar();
            exitRoad.addCar(carAdd);
            carCounting++;
            return false;
        } else { // if road is full
            return true;
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
}