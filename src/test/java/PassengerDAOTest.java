import org.cruises.model.Passenger;
import org.cruises.service.database.PassengerDAO;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PassengerDAOTest {

    private PassengerDAO dao;
    private Passenger testPassenger;

    @BeforeEach
    void setUp() {
        dao = new PassengerDAO();
        testPassenger = new Passenger();
        testPassenger.setFullName("Test User");
        testPassenger.setPhoneNumber("+380123456789");
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (testPassenger.getId() != 0) {
            dao.delete(testPassenger);
        }
    }

    @Test
    void testSaveAndGetAll() throws SQLException {
        // Save passenger
        boolean saved = dao.save(testPassenger);
        assertTrue(saved, "Passenger не збережено");
        assertTrue(testPassenger.getId() > 0, "Id пасажира не встановлено");

        // Get all passengers and check if saved one is present
        List<Passenger> allPassengers = dao.getAll();
        boolean found = allPassengers.stream()
                .anyMatch(p -> p.getId() == testPassenger.getId() &&
                        p.getFullName().equals(testPassenger.getFullName()) &&
                        p.getPhoneNumber().equals(testPassenger.getPhoneNumber()));
        assertTrue(found, "Збережений пасажир не знайдений у списку");
    }

    @Test
    void testUpdate() throws SQLException {
        dao.save(testPassenger);

        testPassenger.setFullName("Updated User");
        testPassenger.setPhoneNumber("+380987654321");

        boolean updated = dao.update(testPassenger);
        assertTrue(updated, "Оновлення пасажира не вдалося");

        // Перевірка, що зміни відобразилися
        List<Passenger> allPassengers = dao.getAll();
        Passenger updatedPassenger = allPassengers.stream()
                .filter(p -> p.getId() == testPassenger.getId())
                .findFirst()
                .orElse(null);

        assertNotNull(updatedPassenger);
        assertEquals("Updated User", updatedPassenger.getFullName());
        assertEquals("+380987654321", updatedPassenger.getPhoneNumber());
    }

    @Test
    void testDelete() throws SQLException {
        dao.save(testPassenger);

        boolean deleted = dao.delete(testPassenger);
        assertTrue(deleted, "Видалення пасажира не вдалося");

        List<Passenger> allPassengers = dao.getAll();
        boolean found = allPassengers.stream()
                .anyMatch(p -> p.getId() == testPassenger.getId());
        assertFalse(found, "Пасажир не повинен бути у списку після видалення");
    }
}
