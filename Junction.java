
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Junction extends Thread {
    private String junctionName;
    private int greenLightTime;
    private Clock clock;
    private int carCounting;
    private int carsRemaining;

    private Map<String, Road> entries;
    private Map<String, Road> exits;

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
                    passCar(removedCar);
                    carCounting++;
                }
            }
            this.carsRemaining = entryRoad.getCarsAmount();
            System.out.println("Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction "
                    + junctionName + ": " + carCounting + " cars through from " + value + ", " + this.carsRemaining
                    + " cars waiting");
            carCounting = 0;
        }
    }

    private void passCar(Car car) {
        Road exitRoad = getExitRoadForDestination(car.getDestination());        
        // if (exitRoad != null && !exitRoad.isRoadFull()) {
        if (!exitRoad.isRoadFull()) {
            exitRoad.addCar(car);
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
        }  else {
            return null;
        }

        // if ("A" == junctionName)
        // {
        //     if ("IndustrialPark" == destination) {
        //         return exits.get("West");
        //     } else {
        //         return exits.get("North");
        //     }
        // }
        // else if ("B" == junctionName)
        // {
        //     if ("IndustrialPark" == destination)
        //     {
        //         return exits.get("South");
        //     }
        //     else {
        //         return exits.get("North");
        //     }
        // }
        // else if ("C" == junctionName)
        // {
        //     if ("IndustrialPark" == destination)
        //     {
        //         return exits.get("South");
        //     }
        //     else if ("ShoppingCentre" == destination)
        //     {
        //         return exits.get("West");
        //     }
        //     else {
        //         return exits.get("North");
        //     }
        // }
        // else if ("D" == junctionName)
        // {
        //     if ("University" == destination)
        //     {
        //         return exits.get("North");
        //     }
        //     else {
        //         return exits.get("South");
        //     }
        // }
        // else {
        //     return null;
        // }
    }
}