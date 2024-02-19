package LockhartModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Configuration {
    private Map<String, Integer> entryPointsRates;
    private Map<String, Integer> junctionGreenTimes;
    String configFilePath;

    public Configuration(int configFileNumber)
    {
        configFilePath = "LockhartModel/Task 1 Scenarios/Scenario" + configFileNumber + ".txt";

        // Outputing the name of the file
        System.out.println("Configuration File: " + configFilePath);

        // Map intitalisation
        entryPointsRates = new HashMap<>();
        junctionGreenTimes = new HashMap<>();

        // Reading File
        try (BufferedReader contentReader = new BufferedReader(new FileReader(configFilePath)))
        {
            String line;
            String checkStatus = "";
            while ((line = contentReader.readLine()) != null)
            {
                StringTokenizer tokenisedLine = new StringTokenizer(line);

                if (line.startsWith("ENTRYPOINTS"))
                {
                    checkStatus = "ENTRYPOINTS";
                }
                else if (line.startsWith("JUNCTIONS"))
                {
                    checkStatus = "JUNCTIONS";
                }


                if (tokenisedLine.countTokens() == 2 && checkStatus == "ENTRYPOINTS")
                {
                    String entryPoints = tokenisedLine.nextToken().toLowerCase();
                    int rate = Integer.parseInt(tokenisedLine.nextToken());
                    entryPointsRates.put(entryPoints, rate);
                }
                else if (tokenisedLine.countTokens() == 2 && checkStatus == "JUNCTIONS")
                {
                    String junction = tokenisedLine.nextToken().toUpperCase();
                    int greenTime = Integer.parseInt(tokenisedLine.nextToken());
                    junctionGreenTimes.put(junction, greenTime);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    // Gives the entry point rates
    public int getEntryPointRate(String entryPoint) {
        return entryPointsRates.getOrDefault(entryPoint.toLowerCase(), 0);
    }

    // Gives the junction green light times
    public int getJunctionLightTime(String junction)
    {
        return junctionGreenTimes.getOrDefault(junction.toUpperCase(), 0);   
    }
}