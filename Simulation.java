import java.util.concurrent.TimeUnit;
import java.util.Map;

public class Simulation {
    public static void main(String[] arg) {

        //Asking for the input


        // Reading files
        Configuration config = new Configuration("Task 1 Scenarios/Scenario1.txt");

        //Reading Files Configuration
        int southRate = config.getEntryPointRate("South");
        int eastRate = config.getEntryPointRate("East");
        int northRate = config.getEntryPointRate("North"); 

        // Main Simulation begins here...
        Clock clock = new Clock(TimeUnit.SECONDS.toNanos(1));
        // clock.setRunDuration(TimeUnit.SECONDS.toNanos(12));
        clock.setRunDuration(TimeUnit.MINUTES.toNanos(1));

        // Junction Roads - A
        Road southEntryA = new Road(60);
        Road westIndustrialPark = new Road(15);
        Road northA = new Road(7);

        // Junction Roads - B
        Road southB = new Road(7);
        Road eastEntryB = new Road(30);
        Road northB = new Road(10);

        // Junction Roads - C
        Road northEntryC = new Road(50);
        Road westShoppingCentre = new Road(7);
        Road southC = new Road(10);

        // Junction Roads - D
        Road northD = new Road(10);
        Road northUniversity = new Road(15);
        Road southStation = new Road(15);

        // Entry Points
        EntryPoint southEntry = new EntryPoint("South", southRate, southEntryA, clock);
        EntryPoint eastEntry = new EntryPoint("East", eastRate, eastEntryB, clock);
        EntryPoint northEntry = new EntryPoint("North", northRate, northEntryC, clock);

        // Junctions
        String[] sequenceA = { "South", "North" };
        Junction junctionA = new Junction("A", 60, clock, sequenceA);
        junctionA.setEntry("South", southEntryA);
        junctionA.setEntry("North", northA);

        junctionA.setExit("West", westIndustrialPark);
        junctionA.setExit("North", southB);

        String[] sequenceB = {"North", "East","South"};
        Junction junctionB = new Junction("B", 60, clock, sequenceB);
        junctionB.setEntry("South", southB);
        junctionB.setEntry("East", eastEntryB);
        junctionB.setEntry("North", northB);

        junctionB.setExit("South", northA);
        junctionB.setExit("North", southC);

        String[] sequenceC = {"South", "North"};
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
        CarPark Station = new CarPark("Station", 150, southStation, clock);


        // clock.getReportingOnSpaces(new CarPark[]{IndustrialPark, ShoppingCentre, University, Station});

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


        // CarPark.reportAllParkingSpaces();

        System.out.println("\n");
        System.out.println("Total Number of Cars Created: " + EntryPoint.getCarsGenerated());
        System.out.println("Total Number of Cars Atomic Queued: " + Road.getTotalCarsQueued());
        System.out.println("Total Number of Cars Parked: " + CarPark.getTotalCarsParked());
    }
}
