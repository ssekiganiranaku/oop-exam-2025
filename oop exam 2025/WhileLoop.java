public class PM25TrackerWhileLoop {
    public static void main(String[] args) {
        int day = 1;
        while (day <= 30) {
            double pm25 = Math.random() * 200; 
            System.out.println("Day " + day + ": PM2.5 = " + pm25);
            day++;
        }
    }
}