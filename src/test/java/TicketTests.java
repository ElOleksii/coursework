import org.cruise.model.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class TicketTests {

    @Test
    public void testTicketCreation() {
        Ticket ticket = new Ticket(1, "Cruise Ship", "Port A", "Port B", new Date(), "Economy", 150.50f);

        Assertions.assertNotNull(ticket, "Ticket object should not be null.");
        Assertions.assertEquals(1, ticket.getTicketId());
        Assertions.assertEquals("Cruise Ship", ticket.getShipName());
        Assertions.assertEquals("Port A", ticket.getWay().getDeparturePort());
        Assertions.assertEquals("Port B", ticket.getWay().getArrivalPort());
        Assertions.assertEquals("Economy", ticket.getCabinClass());
        Assertions.assertEquals(150.50f, ticket.getPrice());
    }

    @Test
    public void testTicketUpdate() {
        Ticket ticket = new Ticket(1, "Cruise Ship", "Port A", "Port B", new Date(), "Economy", 150.50f);

        ticket.setShipName("Updated Ship");
        ticket.getWay().setDeparturePort("Port C");
        ticket.getWay().setArrivalPort("Port D");
        ticket.setCabinClass("Business");
        ticket.setPrice(200.75f);

        Assertions.assertEquals("Updated Ship", ticket.getShipName());
        Assertions.assertEquals("Port C", ticket.getWay().getDeparturePort());
        Assertions.assertEquals("Port D", ticket.getWay().getArrivalPort());
        Assertions.assertEquals("Business", ticket.getCabinClass());
        Assertions.assertEquals(200.75f, ticket.getPrice());
    }
}
