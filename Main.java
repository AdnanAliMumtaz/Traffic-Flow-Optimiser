import java.util.Random;
import javax.swing.RowFilter.Entry;

public class Main{  
    public static void main(String[] arg)
    {
        // Reading files 
        Configuration config = new Configuration("Task 1 Scenarios/Scenario1.txt");

        // Make sure the junction is right 
        Road road = new Road(60);
        EntryPoint entry = new EntryPoint("North", 550, road);
        entry.start();


    }
}