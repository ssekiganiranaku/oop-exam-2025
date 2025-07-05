import java.util.Random;

public class PassengerSource {
    private Company company;
    private Random random;

    
    public PassengerSource(Company company) {
        this.company = company;
        this.random = new Random();
    }

    
    public boolean requestPickup() {
        
        Passenger passenger = new Passenger();

        
        Location pickup = new Location(random.nextInt(101), random.nextInt(101));
        Location destination = new Location(random.nextInt(101), random.nextInt(101));

        
        boolean isScheduled = company.scheduleVehicle(pickup, destination);

        return isScheduled;
    }
}