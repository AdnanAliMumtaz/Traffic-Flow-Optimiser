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

        // Junction Roads - A
        Road southEntryA = new Road(6);
        Road westIndustrialPark = new Road(15);
        Road northA = new Road(2);

        // Junction Roads - B
        Road southB = new Road(5);
        Road eastEntryB = new Road(5);
        Road northB = new Road(5);

        // Junction Roads - C
        Road northEntryC = new Road(5);
        Road westShoppingCentre = new Road(7);
        Road southC = new Road(4);

        // Junction Roads - D
        Road northD = new Road(4);
        Road northUniversity = new Road(5);
        Road southStation = new Road(6);

        // Entry Points
        EntryPoint southEntry = new EntryPoint("South", southRate, southEntryA, clock);
        EntryPoint eastEntry = new EntryPoint("East", northRate, eastEntryB, clock);
        EntryPoint northEntry = new EntryPoint("North", northRate, northEntryC, clock);

        // Junctions
        String[] sequenceA = { "South", "North" };
        Junction junctionA = new Junction("A", 60, clock, sequenceA);
        junctionA.setEntry("South", southEntryA);
        junctionA.setEntry("North", northA);
        junctionA.setExit("West", westIndustrialPark);
        junctionA.setExit("North", southB);

        String[] sequenceB = { "South", "East", "North"};
        Junction junctionB = new Junction("B", 60, clock, sequenceB);
        junctionB.setEntry("South", southB);
        junctionB.setEntry("East", eastEntryB);
        junctionB.setEntry("North", northB);
        junctionB.setExit("North", northA);
        junctionB.setExit("South", southC);

        String[] sequenceC = {"North", "South"};
        Junction junctionC = new Junction("C", 30, clock, sequenceC);
        junctionC.setEntry("North", northEntryC);
        junctionC.setEntry("South", southC);

        junctionC.setExit("West", westShoppingCentre);
        junctionC.setExit("South", southB);
        junctionC.setExit("North", northD);

        String[] sequenceD = {"North"};
        Junction junctionD = new Junction("D", 30, clock, sequenceD);
        junctionD.setEntry("North", northD);
        junctionD.setExit("North", northUniversity);
        junctionD.setExit("South", southStation);

        // CarParks - Destination
        CarPark IndustrialPark = new CarPark("IndustrialPark", 1000, westIndustrialPark, clock);
        CarPark ShoppingCentre = new CarPark("ShoppingCentre", 400, westShoppingCentre, clock);
        CarPark University = new CarPark("University", 100, northUniversity, clock);
        CarPark Station = new CarPark("Station", northRate, southStation, clock);

        // Start threads
        clock.start();
        southEntry.start();
        eastEntry.start();
        northEntry.start();
        junctionA.start();
        junctionB.start();
        junctionC.start();
        junctionD.start();
        IndustrialPark.start();
        ShoppingCentre.start();
        University.start();
        Station.start();

        // Wait for threads to complete
        try {
            clock.join();
            southEntry.join();
            eastEntry.join();
            northEntry.join();
            junctionA.join();
            junctionB.join();
            junctionC.join();
            junctionD.join();
            IndustrialPark.join();
            ShoppingCentre.join();
            University.join();
            Station.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IndustrialPark.report();
        ShoppingCentre.report();
        University.report();
        Station.report();

        System.out.println("\n");
        System.out.println("Total Number of Cars Created: " + EntryPoint.getCarsGenerated());
        System.out.println("Total Number of Cars Atomic Queued: " + Road.getTotalCarsQueued());
        System.out.println("Total Number of Cars Parked: " + CarPark.getTotalCarsParked());
    }
}
