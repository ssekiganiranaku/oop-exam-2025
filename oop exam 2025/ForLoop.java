public class PM25TrackerForLoop {
    public static void main(String[] args) {
        for (int day = 1; day <= 30; day++) {
            double pm25 = Math.random() * 200; 
            System.out.println("Day " + day + ": PM2.5 = " + pm25);
        }
    }
}