package LockhartModel;

public class Simulation {
    public static void main(String[] arg) {

        // Check gridlock
        // Check the waiting numbers
        // Check the comments

        // Reading files
        Configuration fileData = new Configuration(1); // Enter the scenario number to run any test from scenarios.

        // EnterPoint Rates
        int southRate = fileData.getEntryPointRate("South");
        int eastRate = fileData.getEntryPointRate("East");
        int northRate = fileData.getEntryPointRate("North");

        // Junction Rates
        int junctionAGreenLight = fileData.getJunctionLightTime("A");
        int junctionBGreenLight = fileData.getJunctionLightTime("B");
        int junctionCGreenLight = fileData.getJunctionLightTime("C");
        int junctionDGreenLight = fileData.getJunctionLightTime("D");

        // Main Simulation begins here...
        int runningMinutes = 60; // Running for the sped up 60 minutes.
        Clock clock = new Clock(runningMinutes);

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

        // Junctions - A Setting up
        String[] sequenceA = { "South", "North" };
        Junction junctionA = new Junction("A", junctionAGreenLight, clock, sequenceA);
        junctionA.setEntry("South", southEntryA);
        junctionA.setEntry("North", northA);
        junctionA.setExit("West", westIndustrialPark);
        junctionA.setExit("North", southB);

        // Junctions - B Setting up
        String[] sequenceB = { "South", "East", "North" };
        Junction junctionB = new Junction("B", junctionBGreenLight, clock, sequenceB);
        junctionB.setEntry("South", southB);
        junctionB.setEntry("East", eastEntryB);
        junctionB.setEntry("North", northB);
        junctionB.setExit("South", northA);
        junctionB.setExit("North", southC);

        // Junctions - C Setting up
        String[] sequenceC = { "North", "South" };
        Junction junctionC = new Junction("C", junctionCGreenLight, clock, sequenceC);
        junctionC.setEntry("North", northEntryC);
        junctionC.setEntry("South", southC);
        junctionC.setExit("West", westShoppingCentre);
        junctionC.setExit("South", southB);
        junctionC.setExit("North", northD);

        // Junctions - D Setting up
        String[] sequenceD = { "North" };
        Junction junctionD = new Junction("D", junctionDGreenLight, clock, sequenceD);
        junctionD.setEntry("North", northD);
        junctionD.setExit("North", northUniversity);
        junctionD.setExit("South", southStation);

        // CarParks
        CarPark IndustrialPark = new CarPark("IndustrialPark", 1000, westIndustrialPark, clock);
        CarPark ShoppingCentre = new CarPark("ShoppingCentre", 400, westShoppingCentre, clock);
        CarPark University = new CarPark("University", 100, northUniversity, clock);
        CarPark Station = new CarPark("Station", 150, southStation, clock);

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

        // Report results
        IndustrialPark.report();
        ShoppingCentre.report();
        University.report();
        Station.report();

        // Final Output
        System.out.println("\n");
        System.out.println("Total Number of Cars Created: " + EntryPoint.getTotalCarsGenerated());
        System.out.println("Total Number of Cars Atomic Queued: " + Road.getTotalCarsQueued());
        System.out.println("Total Number of Cars Parked: " + CarPark.getTotalCarsParked());
    }
}
