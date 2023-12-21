import java.util.Random;
import javax.swing.RowFilter.Entry;
import java.util.Map;
import java.util.Date;

public class Main{  
    public static void main(String[] arg)
    {
        // Reading files 
        Configuration config = new Configuration("Task 1 Scenarios/Scenario1.txt");
        Map<String, Integer> entryPoints = config.getEntryPoints();
        Map<String, Integer> junctions = config.getJunctions();
        

        // How am I going to implement a road of networks.
        int southRate = entryPoints.get("South");

        // Road road = new Road(60);
        // EntryPoint entry = new EntryPoint("South", southRate, road);
        // entry.start();



        // // Junction for the roads
        // Junction junctionA = new Junction("A", 1, 1);  // One entry and one exit route for simplicity

        // // Connect the components
        // junctionA.start();
    }
}