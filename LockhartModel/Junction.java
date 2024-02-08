package LockhartModel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
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
    private Lock lock;

    public Junction(String name, int greenTime, Clock clock, String[] sequence) {
        this.junctionName = name;
        this.greenLightTime = greenTime;
        this.clock = clock;
        this.carCounting = 0;
        this.carsRemaining = 0;

        this.entries = new HashMap<>();
        this.exits = new HashMap<>();

        this.sequence = sequence;
        this.lock = new ReentrantLock();

        // Initialise logger
        this.logger = Logger.getLogger(Junction.class.getName() + "." + junctionName);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        try {
            String packageName = getClass().getPackage().getName();
            String filePath = packageName.replace(".", "/") + "/" + junctionName + "_log.txt";
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
            CarMovement();
            // lock.lock();
            // try {
            // CarMovement();
            // } catch (Exception e) {
            // e.printStackTrace();
            // } finally {
            // lock.unlock();
            // }
        }

        // This doesn't work, produces the wrong results
        // lock.lock();
        // try {
        // while (clock.getRunningTicks()) {
        // CarMovement();
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // } finally {
        // lock.unlock();
        // }
    }

    private void CarMovement() {
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
                    String removedCar = entryRoad.peek().getDestination(); // Made this a synchornized
                    islocked = passCar(entryRoad, removedCar);
                    carCounting++;

                    try {
                        Thread.sleep(clock.fastTrackPerMinutes(12));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // System.out.println("This is the time for e" + end);

            // String logMessage = "Time: " + clock.getCurrentMinutes() + "m" +
            // clock.getCurrentSeconds() + "s - Junction "
            // + junctionName + ": " + carCounting + " cars through from " + value + ", " +
            // this.carsRemaining
            // + " cars waiting.";

            // String logMessage = "Time: " + clock.getCurrentMinutes() + "m" +
            // clock.getCurrentSeconds() + "s - Junction "
            // + junctionName + ": " + carCounting + " cars through from " + value + ", This
            // was the time for ending " + end + " with my current tick " + clock.getTick();

            // if (this.carsRemaining > 0 && islocked) {
            // logMessage += " GRIDLOCK";
            // }
            // logger.info(logMessage);

            logActivity(value, islocked);
            this.carsRemaining = entryRoad.getCarsAmount();

            carCounting = 0;
        }
    }

    private void logActivity(String direction, boolean isLocked) {
        String logMessage = "Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction "
                + junctionName + ": " + carCounting + " cars through from " + direction + ", " + this.carsRemaining
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
            return false;
        } else { // if road is full
            return true;
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
            if ("IndustrialPark" == destination) {
                return exits.get("South");
                // return exits.get("North");
            } else {
                return exits.get("North");
            }
        } else if ("C" == junctionName) {
            if ("IndustrialPark" == destination) {
                return exits.get("South");
            } else if ("ShoppingCentre" == destination) {
                return exits.get("West");
            } else {
                return exits.get("North");
            }
        } else if ("D" == junctionName) {
            if ("University" == destination) {
                return exits.get("North");
            } else {
                return exits.get("South");
            }
        } else {
            return null;
        }
    }

    // private Road getExitRoadForDestination(String destination) {
    // if (junctionName == null) {
    // return null;
    // }

    // switch (junctionName) {
    // case "A":
    // return "IndustrialPark".equals(destination) ? exits.get("West") :
    // exits.get("North");
    // case "B":
    // return "IndustrialPark".equals(destination) ? exits.get("South") :
    // exits.get("North");
    // case "C":
    // if ("IndustrialPark".equals(destination)) {
    // return exits.get("South");
    // } else if ("ShoppingCentre".equals(destination)) {
    // return exits.get("West");
    // } else {
    // return exits.get("North");
    // }
    // case "D":
    // return "University".equals(destination) ? exits.get("North") :
    // exits.get("South");
    // default:
    // return null;
    // }
    // }

    // private Road getExitRoadForDestination(String destination) {
    // switch (junctionName) {
    // case "A":
    // return ("IndustrialPark" == destination) ? exits.get("West") :
    // exits.get("North");
    // case "B":
    // return ("IndustrialPark" == destination) ? exits.get("South") :
    // exits.get("North");
    // case "C":
    // if ("IndustrialPark" == destination) return exits.get("South");
    // else if ("ShoppingCentre" == destination) return exits.get("West");
    // else return exits.get("North");
    // case "D":
    // return ("University" == destination) ? exits.get("North") :
    // exits.get("South");
    // default:
    // return null;
    // }
    // }
}