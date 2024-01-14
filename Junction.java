
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Junction extends Thread {
    private String junctionName;
    private int greenLightTime;
    private Road[] entryRoads;
    private Road[] exitRoads;
    private Clock clock;
    private int carCounting;
    private int carsRemaining;

    private Map<String, Road> entries;
    private Map<String, Road> exits;

    private String[] sequence;

    // private boolean isAvailable;

    public Junction(String name, int greenTime, int entryRoadsNums, int exitRoadsNums, Clock clock, String[] sequence)
    {
        this.junctionName = name;
        this.greenLightTime = greenTime;
        this.entryRoads = new Road[entryRoadsNums];
        this.exitRoads = new Road[exitRoadsNums];
        this.clock = clock;
        this.carCounting = 0;
        this.carsRemaining = 0;

        this.entries = new HashMap<>();
        this.exits = new HashMap<>();

        this.sequence = sequence;
    }

    public synchronized void setEntry(String name, Road road) 
    {
        entries.put(name, road);
    }

    public synchronized void setExit(String name, Road road) 
    {
        exits.put(name, road);
    }

    public void run()
    {
        while (clock.getRunningTime())
        {
            for (String value : sequence)
            {
                long time = 0;
                long startTime = System.nanoTime(); 

                while (TimeUnit.SECONDS.toMillis(time) < clock.fastTrackPerSeconds(greenLightTime))
                {
                    if (!clock.getRunningTime())
                    {
                        break;
                    }

                    // Operation
                    Road entryRoad = entries.get(value);
                    
                    if (entryRoad != null  && !entryRoad.isRoadEmpty()) {
                        Car removedCar = entryRoad.removeCar();

                        if (removedCar != null) {
                            // Simulating the car passing
                            try {
                                Thread.sleep(clock.fastTrackPerMinutes(12));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // System.out.println("Junction " + junctionName + ": " + carCounting + " cars through from " + removedCar.getDestination());        
                            passCar(removedCar);           
                            carCounting++;     
                            
                            this.carsRemaining = entryRoad.getCarsAmount();
                        }
                    }

                    //Time Calculation
                    long currentTime = System.nanoTime();
                    time = TimeUnit.NANOSECONDS.toSeconds(currentTime - startTime);

                    // if (TimeUnit.SECONDS.toMillis(time) >=  clock.fastTrackPerSeconds(greenLightTime))
                    // {
                    //     System.out.println("Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction " + junctionName + ": " + carCounting + " cars through from " + value + ", " + this.carsRemaining + " cars waiting");
                    //     // System.out.println("THE LIGHT HAS GONE RED FOR " + value);
                    // }
                }       
                if (TimeUnit.SECONDS.toMillis(time) >=  clock.fastTrackPerSeconds(greenLightTime))
                {
                    System.out.println("Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction " + junctionName + ": " + carCounting + " cars through from " + value + ", " + this.carsRemaining + " cars waiting");
                    // System.out.println("THE LIGHT HAS GONE RED FOR " + value);
                }
                // else if (TimeUnit.SECONDS.toMillis(time) >=  clock.fastTrackPerSeconds(greenLightTime)) 
                // {
                //     System.out.println("Time: " + clock.getCurrentMinutes() + "m" + clock.getCurrentSeconds() + "s - Junction " + junctionName + ": " + carCounting + " cars through from " + value + ", " + this.carsRemaining + " cars waiting. GRIDLOCK");
                // }

                carCounting = 0;
                carsRemaining = 0;
            }
            if (!clock.getRunningTime())
            {
                break;
            }
        }
    }      

    private boolean isGreen()
    {
        // int timePassed = clock.getCurrentTime() % (((greenLightTime / 10) * 6) + 1);
        // return timePassed < ((greenLightTime / 10) * 6);

        long elapsedTime = clock.getCurrentTime(); // Assuming getCurrentTime() returns elapsed time in seconds
        long greenLightDuration = clock.fastTrackPerSeconds(greenLightTime);
        

        System.out.println("Fastend Version: " +  greenLightDuration);

        // return elapsedTime < greenLightDuration;
        return true;
    }

    private void passCar(Car car)
    {
        Road exitRoad = getExitRoadForDestination(car.getDestination());
        if (exitRoad != null && !exitRoad.isRoadFull())
        {
            exitRoad.addCar(car);
        }
        else {

        }
    }

    private Road getExitRoadForDestination(String destination)
    {
        if ("A" == junctionName)
        {
            if ("IndustrialPark" == destination) {
                return exits.get("West");
            }
            else {
                return exits.get("North");
            }
        }
        else if ("B" == junctionName)
        {
            if ("ShoppingCentre" == destination) {
                return exits.get("West");
            }
            else {
                return exits.get("South");
            }
        }
        else {
            return null;
        }
    }
}