
import org.cruise.model.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PassengerTests {

    @Test
    public void testPassengerCreation() {
        Passenger passenger = new Passenger("Alice Brown", "+123456789", "123 Main St", 1001);

        Assertions.assertNotNull(passenger, "Passenger object should not be null.");
        Assertions.assertEquals("Alice Brown", passenger.getFullName());
        Assertions.assertEquals("+123456789", passenger.getPhoneNumber());
        Assertions.assertEquals("123 Main St", passenger.getAddress());
        Assertions.assertEquals(1001, passenger.getTicketId());
    }

    @Test
    public void testPassengerUpdate() {
        Passenger passenger = new Passenger("Alice Brown", "+123456789", "123 Main St", 1001);

        passenger.setFullName("Bob White");
        passenger.setPhoneNumber("+987654321");
        passenger.setAddress("456 Oak St");
        passenger.setTicketId(1002);

        Assertions.assertEquals("Bob White", passenger.getFullName());
        Assertions.assertEquals("+987654321", passenger.getPhoneNumber());
        Assertions.assertEquals("456 Oak St", passenger.getAddress());
        Assertions.assertEquals(1002, passenger.getTicketId());
    }
}
