
import org.cruise.model.Cashier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CashierTests {

    @Test
    public void testCashierCreation() {
        Cashier cashier = new Cashier("John Doe", "+123456789", "Cruise Org", true);

        Assertions.assertNotNull(cashier, "Cashier object should not be null.");
        Assertions.assertEquals("John Doe", cashier.getFullName());
        Assertions.assertEquals("+123456789", cashier.getPhoneNumber());
        Assertions.assertEquals("Cruise Org", cashier.getOrganizationName());
        Assertions.assertTrue(cashier.isShift());
    }

    @Test
    public void testCashierUpdate() {
        Cashier cashier = new Cashier("John Doe", "+123456789", "Cruise Org", true);

        cashier.setFullName("Jane Smith");
        cashier.setPhoneNumber("+987654321");
        cashier.setOrganizationName("Updated Org");
        cashier.setShift(false);

        Assertions.assertEquals("Jane Smith", cashier.getFullName());
        Assertions.assertEquals("+987654321", cashier.getPhoneNumber());
        Assertions.assertEquals("Updated Org", cashier.getOrganizationName());
        Assertions.assertFalse(cashier.isShift());
    }
}
