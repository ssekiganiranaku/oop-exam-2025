import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CompanyTest {

    @Test
    public void testScheduleVehicleWithAvailableVehicles() {
        Company company = new Company();
        Vehicle vehicle = new Vehicle();  
        company.addVehicle(vehicle);  

        Location pickup = new Location(10, 20);
        Location destination = new Location(30, 40);

        boolean result = company.scheduleVehicle(pickup, destination);
        assertTrue(result, "A vehicle should be scheduled when available.");
    }

    @Test
    public void testScheduleVehicleWithNoAvailableVehicles() {
        Company company = new Company();
        

        Location pickup = new Location(50, 60);
        Location destination = new Location(70, 80);

        boolean result = company.scheduleVehicle(pickup, destination);
        assertFalse(result, "No vehicle should be scheduled when none are available.");
    }
}