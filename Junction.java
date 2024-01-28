import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Junction extends Thread {
    private String junctionName;
    private int greenLightTime;
    private Clock clock;
    private int carCounting;
    private int carsRemaining;
    private Map<String, Road> entries;
    private Map<String, Road> exits;
    private Logger logger;

    private String[] sequence;

    public Junction(String name, int greenTime, Clock clock, String[] sequence) {
        this.junctionName = name;
        this.greenLightTime = greenTime;
        this.clock = clock;
        this.carCounting = 0;
        this.carsRemaining = 0;

        this.entries = new HashMap<>();
        this.exits = new HashMap<>();

        this.sequence = sequence;

        // Initialise logger
        this.logger = Logger.getLogger(Junction.class.getName() + "." + junctionName);
        try {
            FileHandler fileHandler = new FileHandler(junctionName + "_log.txt", true);
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

    public synchronized void setEntry(String name, Road road) {
        entries.put(name, road);
    }

    public synchronized void setExit(String name, Road road) {
        exits.put(name, road);
    }

    public void run() {
        while (clock.getRunningTicks()) {
            CarMovement();
        }
    }

    private synchronized void CarMovement() {
        for (String value : sequence) {

            if (!clock.getRunningTicks()) {
                break;
            }

            int start = clock.getTick();
            int end = start + (greenLightTime / 10);

            Road entryRoad = entries.get(value);

            boolean islocked = false;
            while (clock.getTick() < end && clock.getRunningTicks()) // Green light for entry
            {
                // Operation
                if (!entryRoad.isRoadEmpty()) {
                    try {
                        Thread.sleep(clock.fastTrackPerMinutes(12));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Car removedCar = entryRoad.removeCar();
                    islocked = passCar(removedCar);
                    carCounting++;
                }
            }
            this.carsRemaining = entryRoad.getCarsAmount();
            // System.out.println("Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction "
            //         + junctionName + ": " + carCounting + " cars through from " + value + ", " + this.carsRemaining
            //         + " cars waiting");
            logActivity(value, islocked);

            carCounting = 0;
        }
    }

    private synchronized void logActivity(String direction, boolean isLocked) {
        String logMessage = "Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction "
                + junctionName + ": " + carCounting + " cars through from " + direction + ", " + this.carsRemaining
                + " cars waiting.";
    
        if (this.carsRemaining > 0 && isLocked) {
            logMessage += " GRIDLOCK";
        }
    
        logger.info(logMessage);
    }

    private boolean passCar(Car car) {
        Road exitRoad = getExitRoadForDestination(car.getDestination());
        // if (exitRoad != null && !exitRoad.isRoadFull()) {
        if (!exitRoad.isRoadFull()) {
            exitRoad.addCar(car);
            return true;
        }
        else { // if road is full
            return false;
        }
    }

    private Road getExitRoadForDestination(String destination) {
        if ("A" == junctionName) {
            if ("IndustrialPark" == destination) {
                return exits.get("West");
            } else {
                return exits.get("North");
            }
        } else if ("B" == junctionName) {
            if ("ShoppingCentre" == destination) {
                return exits.get("West");
            } else {
                return exits.get("North");
            }
        } else {
            return null;
        }

        // if ("A" == junctionName)
        // {
        // if ("IndustrialPark" == destination) {
        // return exits.get("West");
        // } else {
        // return exits.get("North");
        // }
        // }
        // else if ("B" == junctionName)
        // {
        // if ("IndustrialPark" == destination)
        // {
        // return exits.get("South");
        // }
        // else {
        // return exits.get("North");
        // }
        // }
        // else if ("C" == junctionName)
        // {
        // if ("IndustrialPark" == destination)
        // {
        // return exits.get("South");
        // }
        // else if ("ShoppingCentre" == destination)
        // {
        // return exits.get("West");
        // }
        // else {
        // return exits.get("North");
        // }
        // }
        // else if ("D" == junctionName)
        // {
        // if ("University" == destination)
        // {
        // return exits.get("North");
        // }
        // else {
        // return exits.get("South");
        // }
        // }
        // else {
        // return null;
        // }
    }
}