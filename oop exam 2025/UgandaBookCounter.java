/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package t.aqianalyzer;

/**
 *
 * @author smithhenry
 */
import java.util.Scanner;
public class  UgandaBookCounter {

    public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);

        
        System.out.println("Enter the book description:");
        String description = scanner.nextLine();

        
        String lowerDesc = description.toLowerCase();

        // Word to search
        String target = "uganda";

        int count = 0;
        int index = 0;

        
        while ((index = lowerDesc.indexOf(target, index)) != -1) {
            count++;
            index += target.length(); // move past the current match
        }

        
        System.out.println("The word 'Uganda' appears " + count + " time(s) in the description.");
    }
}
            

        
    

