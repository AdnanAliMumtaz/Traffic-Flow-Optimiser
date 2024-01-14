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
        // clock.start();

        // Real code to start here
        int southRate = entryPoints.get("south");

        // Junction Roads 
        // A 
        Road southRoadToA = new Road(3);
        Road exitRoadToIndustrialPark = new Road(15);
        Road northRoadToA = new Road(3);

        // B
        Road exitRoadToJunctionB = new Road(7);
        Road exitRoadToShoppingCentre = new Road(7);


        // Entry Points 
        EntryPoint southEntry = new EntryPoint("South", southRate, southRoadToA, clock);
        // southEntry.start();

        EntryPoint northEntry = new EntryPoint("North", southRate, northRoadToA, clock);
        // northEntry.start();


        // Junctions 
        String[] sequence = {"South", "North"};

        Junction junctionA = new Junction("A", 60, 1, 2, clock, sequence);
        junctionA.setEntry("South", southRoadToA);
        junctionA.setEntry("North", northRoadToA);
        junctionA.setExit("West", exitRoadToIndustrialPark);
        junctionA.setExit("North", exitRoadToJunctionB);
        // junctionA.setSequence(sequence);
        // junctionA.start();
        
        // String[] se = {"South"};
        // Junction junctionB = new Junction("B", 50, 3, 2, clock, se);
        // junctionB.setEntry("South", exitRoadToJunctionB);
        // junctionB.setExit("West", exitRoadToShoppingCentre);
        // junctionB.start();        
        
        // Junction junctionC = new Junction("C", 5, 2, 3, clock);
        // Junction junctionD = new Junction("D", 5, 1, 2, clock);

        // CarParks - Destination
        CarPark IndustrialPark = new CarPark("IndustrialPark", 1000, exitRoadToIndustrialPark, clock);
        // IndustrialPark.start();

        // CarPark ShoppingCentre = new CarPark("ShoppingCentre", 400, exitRoadToShoppingCentre, clock);
        // ShoppingCentre.start();



        // IndustrialPark.report();

        // ShoppingCentre.report();

        // Clock Output
        // Start threads
        clock.start();
        southEntry.start();
        northEntry.start();
        junctionA.start();
        IndustrialPark.start();
        
        // Wait for threads to complete
        try {
            southEntry.join();
            northEntry.join();
            junctionA.join();
            // IndustrialPark.join();
            clock.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IndustrialPark.report();

        // Print values
        clock.outputElapsedTime();
        System.out.println(southEntry.getCars());
        System.out.println(northEntry.getCars());
    }
}

