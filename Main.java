import java.util.Random;
import javax.swing.RowFilter.Entry;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

public class Main{  
    public static void main(String[] arg)
    {
        // Reading files 
        Configuration config = new Configuration("Task 1 Scenarios/Scenario1.txt");
        Map<String, Integer> entryPoints = config.getEntryPoints();
        Map<String, Integer> junctions = config.getJunctions();
        
        // Real code to start here
        int southRate = entryPoints.get("south");

        // Junction A Roads - Suggestions
        // Take name of the roads, 
        Road entryRoadToJunctionA = new Road(60);
        Road entryRoadToJunctionB = new Road(49);
        Road exitRoadToDestination = new Road(15);

        EntryPoint entry = new EntryPoint("South", southRate, entryRoadToJunctionA);
        entry.start();

        Clock clock = new Clock();
        Junction a = new Junction("A", 60, new Road[]{entryRoadToJunctionA}, new Road[]{entryRoadToJunctionB}, clock);
        a.start();

        Junction b = new Junction("B", 60, new Road[]{entryRoadToJunctionB}, new Road[]{exitRoadToDestination}, clock);
        b.start();
    }
}

