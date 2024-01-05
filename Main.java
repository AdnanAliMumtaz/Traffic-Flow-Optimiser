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
        clock.setRunDuration(TimeUnit.SECONDS.toNanos(60));
        clock.start();

        // Real code to start here
        int southRate = entryPoints.get("south");

        // Junction A Roads - Suggestions
        // Take name of the roads, 
        Road entryRoadToJunctionA = new Road(60);
        Road exitRoadToIndustrialPark = new Road(15);
        // Road exitRoadToJunctionB = new Road(7);
        // Road exitRoadToShoppingCentre = new Road(7);

        // Entry Points 
        EntryPoint southEntry = new EntryPoint("South", southRate, entryRoadToJunctionA, clock);
        southEntry.start();

        // Junctions
        Junction junctionA = new Junction("A", 10, 1, 2, clock);
        junctionA.setEntrySouthRoad(entryRoadToJunctionA);
        junctionA.setExitWestRoad(exitRoadToIndustrialPark);
        // junctionA.setExitNorthRoad(exitRoadToJunctionB);
        junctionA.start();

        
        // Junction junctionB = new Junction("B", 10, 3, 2, clock);
        // junctionB.setEntrySouthRoad(exitRoadToJunctionB);
        // junctionB.setExitWestRoad(exitRoadToShoppingCentre);
        // junctionB.start();        

        // CarParks - Destination
        CarPark IndustrialPark = new CarPark("IndustrialPark", 1000, exitRoadToIndustrialPark, clock);
        IndustrialPark.start();

        // CarPark ShoppingCentre = new CarPark("ShoppingCentre", 400, exitRoadToShoppingCentre);
        // ShoppingCentre.start();


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

