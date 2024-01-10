import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.stream.IIOByteBuffer;
import javax.swing.RowFilter.Entry;
import java.util.Map;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.HashMap;

public class Main{  
    public static void main(String[] arg)
    {
        // Reading files 
        Configuration config = new Configuration("Task 1 Scenarios/Scenario1.txt");
        Map<String, Integer> entryPoints = config.getEntryPoints();
        Map<String, Integer> junctions = config.getJunctions();
        

        // Main Simulation begins here...
        
        // This records the time 
        Clock clock = new Clock(TimeUnit.SECONDS.toNanos(1));
        clock.setRunDuration(TimeUnit.SECONDS.toNanos(15));
        clock.start();

        // Real code to start here
        int southRate = entryPoints.get("south");

        // Junction Roads 
        // A 
        Road entryRoadToJunctionA = new Road(60);
        Road exitRoadToIndustrialPark = new Road(15);
        
        // B
        Road exitRoadToJunctionB = new Road(7);
        Road exitRoadToShoppingCentre = new Road(7);


        // Entry Points 
        EntryPoint southEntry = new EntryPoint("South", southRate, entryRoadToJunctionA, clock);
        southEntry.start();

        // Junctions 
        Junction junctionA = new Junction("A", 5, 1, 2, clock);
        junctionA.setEntry("South", entryRoadToJunctionA);
        junctionA.setEntry("North", exitRoadToIndustrialPark);
        junctionA.setExit("West", exitRoadToIndustrialPark);
        junctionA.setExit("North", exitRoadToJunctionB);
        junctionA.start();
        
        Junction junctionB = new Junction("B", 5, 3, 2, clock);
        junctionB.setEntry("South", exitRoadToJunctionB);
        junctionB.setExit("West", exitRoadToShoppingCentre);
        junctionB.start();        
        
        // Junction junctionC = new Junction("C", 5, 2, 3, clock);
        // Junction junctionD = new Junction("D", 5, 1, 2, clock);

        // CarParks - Destination
        CarPark IndustrialPark = new CarPark("IndustrialPark", 1000, exitRoadToIndustrialPark, clock);
        IndustrialPark.start();

        CarPark ShoppingCentre = new CarPark("ShoppingCentre", 400, exitRoadToShoppingCentre, clock);
        ShoppingCentre.start();


        try {
            IndustrialPark.join();
            // ShoppingCentre.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        IndustrialPark.report();

        // Clock
        try {
            clock.join(); // Wait for the clock thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            
        clock.outputElapsedTime();
        // Improve the Clock
        // Compare the brief with the code
        // Figure out how do i run the run methods 
    }
}

