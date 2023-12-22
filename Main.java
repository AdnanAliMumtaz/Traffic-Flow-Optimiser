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
        

        // How am I going to implement a road of networks.
        int southRate = entryPoints.get("south");

        Road road = new Road(10);
        EntryPoint entry = new EntryPoint("South", southRate, road);
        entry.start();

        // try {
        //     entry.join();
        // }
        // catch (InterruptedException e)
        // {
        //     e.printStackTrace();
        // }

        Junction a = new Junction("A", 60, new Road[]{road}, new Road[]{road});
        a.start();

        // CarPark parking = new CarPark("IndustrialPark", 10, road);
        // parking.start();
    }
}

