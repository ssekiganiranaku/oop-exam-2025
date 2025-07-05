/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package t.aqianalyzer;

/**
 *
 * @author samyrgaston5
 */
import java.util.Arrays;
public class AQIAnalyzer {

    public static void main(String[] args) {
        int numReadings = 30;
        int[] aqiReadings = new int[numReadings];
       
        for (int i = 0; i < numReadings; i++) {
            aqiReadings[i] = (int) (Math.random() * 300) + 1;
        }
        
        System.out.println("Generated AQI Readings:");
        for (int aqi : aqiReadings) {
            System.out.print(aqi + " ");
        }
        System.out.println("\n");

        Arrays.sort(aqiReadings); 

        double median;
        if (numReadings % 2 == 0) {
            
            median = (aqiReadings[numReadings / 2 - 1] + aqiReadings[numReadings / 2]) / 2.0;
        } else {           
            median = aqiReadings[numReadings / 2];
        }
        System.out.println("Median AQI: " + median);
    
        int hazardousDays = 0;
        for (int aqi : aqiReadings) {
            if (aqi > 200) {
                hazardousDays++;
            }
        }
        System.out.println("Number of Hazardous Days (AQI > 200): " + hazardousDays);
    }
}
        
    

