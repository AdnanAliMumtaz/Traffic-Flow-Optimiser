import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.imageio.stream.IIOByteBuffer;
import javax.swing.RowFilter.Entry;
import java.util.Map;
import java.security.InvalidKeyException;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Main {
    public static void main(String[] arg) {
        // Reading files
        Configuration config = new Configuration("Task 1 Scenarios/Scenario1.txt");
        Map<String, Integer> entryPoints = config.getEntryPoints();
        Map<String, Integer> junctions = config.getJunctions();
        int southRate = entryPoints.get("south");
        int northRate = entryPoints.get("north");

        // Main Simulation begins here...
        Clock clock = new Clock(TimeUnit.SECONDS.toNanos(1));
        clock.setRunDuration(TimeUnit.SECONDS.toNanos(24));
        // clock.setRunDuration(TimeUnit.MINUTES.toNanos(1));


        // Junction Roads
        // A
        Road southRoadToA = new Road(3);
        Road exitRoadToIndustrialPark = new Road(15);
        Road northRoadToA = new Road(2);

        // B
        Road exitRoadToJunctionB = new Road(5);
        Road eastRoadToB = new Road(5);
        Road northRoadToB = new Road(5);
        Road northRoadToC = new Road(5);

        // C
        Road exitRoadToShoppingCentre = new Road(7);

        // Entry Points
        EntryPoint southEntry = new EntryPoint("South", southRate, southRoadToA, clock);
        // EntryPoint northEntry = new EntryPoint("North", northRate, northRoadToA, clock);

        // Junctions
        String[] aSequence = { "South","North" };
        Junction junctionA = new Junction("A", 60, clock, aSequence);
        junctionA.setEntry("South", southRoadToA);
        junctionA.setEntry("North", northRoadToA);
        junctionA.setExit("West", exitRoadToIndustrialPark);
        junctionA.setExit("North", exitRoadToJunctionB);

        String[] se = {"South"};
        Junction junctionB = new Junction("B", 60, clock, se);
        junctionB.setEntry("South", exitRoadToJunctionB);
        // junctionB.setEntry("East", eastRoadToB);
        // junctionB.setEntry("North", northRoadToB);
        junctionB.setExit("West", exitRoadToShoppingCentre);
        junctionB.setExit("North", northRoadToC);
        // junctionB.setExit("North", northRoadToC);


        // Road northRoadC = new Road(5);
        // String[] cSequence = {"North", "South"};
        // Junction junctionC = new Junction("C", 20, clock, cSequence);
        // junctionC.setEntry("South", northRoadToC);
        // junctionC.setEntry("North", northRoadC);
        // junctionC.setExit("West", exitRoadToShoppingCentre);

        // CarParks - Destination
        CarPark IndustrialPark = new CarPark("IndustrialPark", 1000, exitRoadToIndustrialPark, clock);
        CarPark ShoppingCentre = new CarPark("ShoppingCentre", 400, exitRoadToShoppingCentre, clock);


        // Start threads
        clock.start();
        southEntry.start();
        // northEntry.start();
        junctionA.start();
        junctionB.start();
        // junctionC.start();
        IndustrialPark.start();
        ShoppingCentre.start();


        // Wait for threads to complete
        try {
            clock.join();
            southEntry.join();
            // northEntry.join();
            junctionA.join();
            junctionB.join();
            // junctionC.join();
            IndustrialPark.join();
            ShoppingCentre.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IndustrialPark.report();
        ShoppingCentre.report();



        // Print values
        // clock.outputElapsedTime();
        System.out.println("Cars remaining on road " + southRoadToA.getCarsAmount() + "  Cars remaining on another road " + northRoadToA.getCarsAmount());
        System.out.println("Total Number of Cars Created: " + EntryPoint.getCarsGenerated());
        System.out.println("Total Number of Cars Queued: " + Road.getTotalCarsQueued());
        System.out.println("Total Number of Cars Parked: " + CarPark.getTotalCarsParked());

        // int totalCars = EntryPoint.getCarsGenerated() - CarPark.getTotalCarsParked();

        // System.out.println("Total Number of Cars Queued: " + totalCars);



        // System.out.println("South Entry Production " + southEntry.getCars());
        // System.out.println("North Entry Production " + northEntry.getCars());
        // System.out.println(clock.fastTrackPerHour(550));
    }
}
