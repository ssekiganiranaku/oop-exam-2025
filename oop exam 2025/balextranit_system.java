// =============================================================================
// BALEXTRANIT (U) LTD - Transportation System Implementation
// =============================================================================

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// =============================================================================
// CORE DOMAIN CLASSES
// =============================================================================

/**
 * Represents a passenger in the transportation system
 */
class Passenger {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int passengerId;
    private final String name;
    private final String phoneNumber;
    private final int groupSize;
    
    public Passenger(String name, String phoneNumber, int groupSize) {
        this.passengerId = idCounter.getAndIncrement();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.groupSize = groupSize;
    }
    
    // Getters
    public int getPassengerId() { return passengerId; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getGroupSize() { return groupSize; }
    
    @Override
    public String toString() {
        return String.format("Passenger{id=%d, name='%s', groupSize=%d}", 
                           passengerId, name, groupSize);
    }
}

/**
 * Represents a location with x,y coordinates
 */
class Location {
    private final int x;
    private final int y;
    private final String address;
    
    public Location(int x, int y, String address) {
        this.x = x;
        this.y = y;
        this.address = address;
    }
    
    public Location(int x, int y) {
        this(x, y, String.format("Location(%d,%d)", x, y));
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public String getAddress() { return address; }
    
    public double distanceTo(Location other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
    
    @Override
    public String toString() {
        return String.format("Location{x=%d, y=%d, address='%s'}", x, y, address);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return x == location.x && y == location.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

/**
 * Represents different types of vehicles in the fleet
 */
enum VehicleType {
    TAXI(4),
    SHUTTLE(14);
    
    private final int capacity;
    
    VehicleType(int capacity) {
        this.capacity = capacity;
    }
    
    public int getCapacity() { return capacity; }
}

/**
 * Represents vehicle status
 */
enum VehicleStatus {
    AVAILABLE,
    EN_ROUTE_TO_PICKUP,
    PICKING_UP,
    TRANSPORTING,
    DROPPING_OFF,
    MAINTENANCE
}

/**
 * Represents a vehicle in the fleet
 */
class Vehicle {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int vehicleId;
    private final String licensePlate;
    private final VehicleType type;
    private VehicleStatus status;
    private Location currentLocation;
    private final String driverName;
    private Trip currentTrip;
    
    public Vehicle(String licensePlate, VehicleType type, String driverName, Location initialLocation) {
        this.vehicleId = idCounter.getAndIncrement();
        this.licensePlate = licensePlate;
        this.type = type;
        this.driverName = driverName;
        this.status = VehicleStatus.AVAILABLE;
        this.currentLocation = initialLocation;
    }
    
    // Getters
    public int getVehicleId() { return vehicleId; }
    public String getLicensePlate() { return licensePlate; }
    public VehicleType getType() { return type; }
    public VehicleStatus getStatus() { return status; }
    public Location getCurrentLocation() { return currentLocation; }
    public String getDriverName() { return driverName; }
    public Trip getCurrentTrip() { return currentTrip; }
    
    // Status management
    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
    
    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }
    
    public void assignTrip(Trip trip) {
        this.currentTrip = trip;
        this.status = VehicleStatus.EN_ROUTE_TO_PICKUP;
    }
    
    public void completeTrip() {
        this.currentTrip = null;
        this.status = VehicleStatus.AVAILABLE;
    }
    
    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }
    
    public boolean canAccommodate(int groupSize) {
        return type.getCapacity() >= groupSize;
    }
    
    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, plate='%s', type=%s, status=%s, driver='%s'}", 
                           vehicleId, licensePlate, type, status, driverName);
    }
}

/**
 * Represents a trip from pickup to destination
 */
class Trip {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int tripId;
    private final Passenger passenger;
    private final Location pickupLocation;
    private final Location destinationLocation;
    private final Date requestTime;
    private Date pickupTime;
    private Date dropoffTime;
    private Vehicle assignedVehicle;
    
    public Trip(Passenger passenger, Location pickupLocation, Location destinationLocation) {
        this.tripId = idCounter.getAndIncrement();
        this.passenger = passenger;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.requestTime = new Date();
    }
    
    // Getters
    public int getTripId() { return tripId; }
    public Passenger getPassenger() { return passenger; }
    public Location getPickupLocation() { return pickupLocation; }
    public Location getDestinationLocation() { return destinationLocation; }
    public Date getRequestTime() { return requestTime; }
    public Date getPickupTime() { return pickupTime; }
    public Date getDropoffTime() { return dropoffTime; }
    public Vehicle getAssignedVehicle() { return assignedVehicle; }
    
    // Trip management
    public void assignVehicle(Vehicle vehicle) {
        this.assignedVehicle = vehicle;
    }
    
    public void markPickedUp() {
        this.pickupTime = new Date();
    }
    
    public void markDroppedOff() {
        this.dropoffTime = new Date();
    }
    
    public boolean isCompleted() {
        return dropoffTime != null;
    }
    
    @Override
    public String toString() {
        return String.format("Trip{id=%d, passenger=%s, pickup=%s, destination=%s}", 
                           tripId, passenger.getName(), pickupLocation, destinationLocation);
    }
}

/**
 * Main Company class that manages the entire transportation system
 */
class Company {
    private final String name;
    private final List<Vehicle> fleet;
    private final List<Trip> completedTrips;
    private final List<Trip> activeTrips;
    private final List<Trip> lostFares; // Track requests that couldn't be fulfilled
    private final Random random;
    
    public Company(String name) {
        this.name = name;
        this.fleet = new ArrayList<>();
        this.completedTrips = new ArrayList<>();
        this.activeTrips = new ArrayList<>();
        this.lostFares = new ArrayList<>();
        this.random = new Random();
    }
    
    // Fleet management
    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }
    
    public List<Vehicle> getFleet() {
        return new ArrayList<>(fleet);
    }
    
    public List<Vehicle> getAvailableVehicles() {
        return fleet.stream()
                   .filter(Vehicle::isAvailable)
                   .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Core business method: Schedule a vehicle for pickup
     * Returns true if successful, false if no vehicle available
     */
    public boolean scheduleVehicle(Passenger passenger, Location pickupLocation, Location destinationLocation) {
        // Find available vehicle that can accommodate the group
        Optional<Vehicle> availableVehicle = fleet.stream()
            .filter(v -> v.isAvailable() && v.canAccommodate(passenger.getGroupSize()))
            .findFirst();
        
        if (availableVehicle.isPresent()) {
            Vehicle vehicle = availableVehicle.get();
            Trip trip = new Trip(passenger, pickupLocation, destinationLocation);
            
            // Assign trip to vehicle
            trip.assignVehicle(vehicle);
            vehicle.assignTrip(trip);
            activeTrips.add(trip);
            
            System.out.printf("✓ Trip scheduled: %s assigned to %s%n", trip, vehicle);
            return true;
        } else {
            // No available vehicle - lost fare
            Trip lostTrip = new Trip(passenger, pickupLocation, destinationLocation);
            lostFares.add(lostTrip);
            System.out.printf("✗ Lost fare: No available vehicle for %s%n", passenger);
            return false;
        }
    }
    
    // Driver notification methods
    public void notifyArrivedAtPickup(int vehicleId) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle != null && vehicle.getCurrentTrip() != null) {
            vehicle.setStatus(VehicleStatus.PICKING_UP);
            vehicle.getCurrentTrip().markPickedUp();
            System.out.printf("Driver of %s arrived at pickup location%n", vehicle.getLicensePlate());
        }
    }
    
    public void notifyDroppedOff(int vehicleId) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle != null && vehicle.getCurrentTrip() != null) {
            Trip trip = vehicle.getCurrentTrip();
            trip.markDroppedOff();
            vehicle.setCurrentLocation(trip.getDestinationLocation());
            
            // Move trip from active to completed
            activeTrips.remove(trip);
            completedTrips.add(trip);
            vehicle.completeTrip();
            
            System.out.printf("Trip completed: %s dropped off at %s%n", 
                            trip.getPassenger().getName(), trip.getDestinationLocation());
        }
    }
    
    // Helper methods
    private Vehicle findVehicleById(int vehicleId) {
        return fleet.stream()
                   .filter(v -> v.getVehicleId() == vehicleId)
                   .findFirst()
                   .orElse(null);
    }
    
    // Analytics and reporting
    public int getTotalLostFares() {
        return lostFares.size();
    }
    
    public int getTotalCompletedTrips() {
        return completedTrips.size();
    }
    
    public int getActiveTripsCount() {
        return activeTrips.size();
    }
    
    public List<Trip> getLostFares() {
        return new ArrayList<>(lostFares);
    }
    
    public List<Trip> getCompletedTrips() {
        return new ArrayList<>(completedTrips);
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return String.format("Company{name='%s', fleet=%d vehicles, completed=%d trips, lost=%d fares}", 
                           name, fleet.size(), completedTrips.size(), lostFares.size());
    }
}

/**
 * PassengerSource class as specified in the requirements
 */
class PassengerSource {
    private final Company company;
    private final Random random;
    
    /**
     * Constructor taking a Company object
     */
    public PassengerSource(Company company) {
        this.company = company;
        this.random = new Random();
    }
    
    /**
     * Creates a new Passenger, generates random pickup and destination Location objects
     * with x and y coordinates ranging from 0 to 100, and uses the company to schedule a vehicle.
     * Returns true if the company successfully schedules the pickup and false otherwise.
     */
    public boolean requestPickup() {
        // Create a new passenger with random data
        String[] names = {"John Doe", "Jane Smith", "Alice Johnson", "Bob Wilson", "Carol Brown"};
        String[] phones = {"+256701234567", "+256702345678", "+256703456789", "+256704567890", "+256705678901"};
        
        String name = names[random.nextInt(names.length)];
        String phone = phones[random.nextInt(phones.length)];
        int groupSize = random.nextInt(6) + 1; // 1-6 passengers
        
        Passenger passenger = new Passenger(name, phone, groupSize);
        
        // Generate random pickup location (0-100 range)
        int pickupX = random.nextInt(101);
        int pickupY = random.nextInt(101);
        Location pickupLocation = new Location(pickupX, pickupY, "Pickup " + pickupX + "," + pickupY);
        
        // Generate random destination location (0-100 range)
        int destX = random.nextInt(101);
        int destY = random.nextInt(101);
        Location destinationLocation = new Location(destX, destY, "Destination " + destX + "," + destY);
        
        // Use the company to schedule a vehicle
        return company.scheduleVehicle(passenger, pickupLocation, destinationLocation);
    }
    
    public Company getCompany() {
        return company;
    }
}

// =============================================================================
// JUNIT TESTS FOR COMPANY CLASS
// =============================================================================

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompanyTest {
    private Company company;
    private Vehicle taxi;
    private Vehicle shuttle;
    private Passenger passenger;
    private Location pickupLocation;
    private Location destinationLocation;
    
    @Before
    public void setUp() {
        company = new Company("BALEXTRANIT (U) LTD");
        taxi = new Vehicle("UAB-123A", VehicleType.TAXI, "James Mukasa", new Location(10, 10));
        shuttle = new Vehicle("UAB-456B", VehicleType.SHUTTLE, "Mary Nakato", new Location(20, 20));
        passenger = new Passenger("Test Passenger", "+256701234567", 2);
        pickupLocation = new Location(30, 30, "Kampala Road");
        destinationLocation = new Location(40, 40, "Entebbe Airport");
    }
    
    /**
     * Test 1: Test successful vehicle scheduling when vehicles are available
     */
    @Test
    public void testScheduleVehicle_Success() {
        // Add vehicles to company fleet
        company.addVehicle(taxi);
        company.addVehicle(shuttle);
        
        // Schedule a vehicle for pickup
        boolean result = company.scheduleVehicle(passenger, pickupLocation, destinationLocation);
        
        // Verify successful scheduling
        assertTrue("Vehicle should be scheduled successfully", result);
        assertEquals("Should have 1 active trip", 1, company.getActiveTripsCount());
        assertEquals("Should have 0 lost fares", 0, company.getTotalLostFares());
        
        // Verify vehicle status changed
        assertFalse("Taxi should no longer be available", taxi.isAvailable());
        assertNotNull("Taxi should have assigned trip", taxi.getCurrentTrip());
    }
    
    /**
     * Test 2: Test lost fare when no vehicles are available
     */
    @Test
    public void testScheduleVehicle_NoVehiclesAvailable() {
        // Don't add any vehicles to the fleet
        
        // Attempt to schedule a vehicle
        boolean result = company.scheduleVehicle(passenger, pickupLocation, destinationLocation);
        
        // Verify scheduling failed
        assertFalse("Vehicle scheduling should fail when no vehicles available", result);
        assertEquals("Should have 0 active trips", 0, company.getActiveTripsCount());
        assertEquals("Should have 1 lost fare", 1, company.getTotalLostFares());
    }
}

// =============================================================================
// DEMONSTRATION CLASS
// =============================================================================

/**
 * Demonstration class showing the system in action
 */
public class BalextranltDemo {
    public static void main(String[] args) {
        System.out.println("=== BALEXTRANIT (U) LTD Transportation System Demo ===\n");
        
        // Create company
        Company company = new Company("BALEXTRANIT (U) LTD");
        
        // Add vehicles to fleet
        company.addVehicle(new Vehicle("UAB-123A", VehicleType.TAXI, "James Mukasa", new Location(10, 10)));
        company.addVehicle(new Vehicle("UAB-456B", VehicleType.SHUTTLE, "Mary Nakato", new Location(20, 20)));
        company.addVehicle(new Vehicle("UAB-789C", VehicleType.TAXI, "Peter Ssali", new Location(30, 30)));
        
        System.out.println("Fleet initialized:");
        company.getFleet().forEach(System.out::println);
        System.out.println();
        
        // Create passenger sources
        PassengerSource hotelSource = new PassengerSource(company);
        PassengerSource corporateSource = new PassengerSource(company);
        
        // Simulate pickup requests
        System.out.println("=== Simulating Pickup Requests ===");
        for (int i = 1; i <= 5; i++) {
            System.out.printf("Request %d: ", i);
            boolean success = i % 2 == 0 ? hotelSource.requestPickup() : corporateSource.requestPickup();
            System.out.println();
        }
        
        // Show final statistics
        System.out.println("\n=== Final Statistics ===");
        System.out.println(company);
        System.out.printf("Available vehicles: %d%n", company.getAvailableVehicles().size());
        System.out.printf("Lost fares: %d%n", company.getTotalLostFares());
    }
}