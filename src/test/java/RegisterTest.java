import org.cruises.model.*;
import org.cruises.model.logic.Register;
import org.cruises.utils.DBConnection;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    private static final int TEST_PASSENGER_ID = 150;
    private static final int TEST_CASHIER_ID = 151;
    private static final int TEST_CRUISE_ID = 152;
    private static final int TEST_SHIP_ID = 1;

    private Register register;

    @BeforeEach
    void setUp() throws SQLException {
        register = new Register();

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM ticket WHERE PassengerId = ?")) {
                ps.setInt(1, TEST_PASSENGER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM orders WHERE PassengerId = ?")) {
                ps.setInt(1, TEST_PASSENGER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM passenger WHERE PassengerId = ?")) {
                ps.setInt(1, TEST_PASSENGER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cashier WHERE CashierId = ?")) {
                ps.setInt(1, TEST_CASHIER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cruise WHERE CruiseId = ?")) {
                ps.setInt(1, TEST_CRUISE_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM ship WHERE ShipId = ?")) {
                ps.setInt(1, TEST_SHIP_ID);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO ship (ShipId, Name) VALUES (?, ?)")) {
                ps.setInt(1, TEST_SHIP_ID);
                ps.setString(2, "Test Ship");
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cruise (CruiseId, CruiseName, StartDate, EndDate, BasicPrice, ShipId) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, TEST_CRUISE_ID);
                ps.setString(2, "Test Cruise");
                ps.setDate(3, Date.valueOf("2025-06-10"));
                ps.setDate(4, Date.valueOf("2025-06-15"));
                ps.setFloat(5, 1000.0f);
                ps.setInt(6, TEST_SHIP_ID);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO passenger (PassengerId, FullName) VALUES (?, ?)")) {
                ps.setInt(1, TEST_PASSENGER_ID);
                ps.setString(2, "Test Passenger");
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cashier (CashierId, Name) VALUES (?, ?)")) {
                ps.setInt(1, TEST_CASHIER_ID);
                ps.setString(2, "Test Cashier");
                ps.executeUpdate();
            }
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM ticket WHERE PassengerId = ?")) {
                ps.setInt(1, TEST_PASSENGER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM orders WHERE PassengerId = ?")) {
                ps.setInt(1, TEST_PASSENGER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM passenger WHERE PassengerId = ?")) {
                ps.setInt(1, TEST_PASSENGER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cashier WHERE CashierId = ?")) {
                ps.setInt(1, TEST_CASHIER_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cruise WHERE CruiseId = ?")) {
                ps.setInt(1, TEST_CRUISE_ID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM ship WHERE ShipId = ?")) {
                ps.setInt(1, TEST_SHIP_ID);
                ps.executeUpdate();
            }
        }
    }

    @Test
    void testCreateTicket() throws SQLException {
        Stateroom stateroom = new StubStateroom();
        Cruise cruise = new StubCruise();
        Cashier cashier = new StubCashier();

        register.createTicket(TEST_PASSENGER_ID, TEST_CRUISE_ID, stateroom, cashier, cruise);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM ticket WHERE PassengerId = ?")) {
            ps.setInt(1, TEST_PASSENGER_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Ticket не було створено");
                assertEquals(TEST_CRUISE_ID, rs.getInt("CruiseId"));
                assertEquals(TEST_CASHIER_ID, rs.getInt("CashierId"));
            }
        }

    }

    static class StubStateroom extends Stateroom {
        public StubStateroom() {
            setStateroomId(1);
            setStateroomClass("Luxury");
            setPrice(500);
        }
    }

    static class StubCruise extends Cruise {
        public StubCruise() {
            setCruiseId(TEST_CRUISE_ID);
            setBasicPrice(1000);
            setStartDate(Date.valueOf("2025-06-10"));
            setEndDate(Date.valueOf("2025-06-15"));
        }
    }

    static class StubCashier extends Cashier {
        public StubCashier() {
            setCashierId(TEST_CASHIER_ID);
            setFullName("Test Cashier");
        }
    }
}
