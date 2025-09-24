import org.cruises.model.Cruise;
import org.cruises.model.Stateroom;
import org.cruises.model.logic.Calc;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CalcTest {

    @Test
    void testCalculateTotalPrice() {
        Calc calc = new Calc();

        Stateroom stateroom = new Stateroom();
        stateroom.setPrice(500);

        Cruise cruise = new Cruise();
        cruise.setStartDate(Date.valueOf("2025-06-10"));
        cruise.setEndDate(Date.valueOf("2025-06-15"));
        cruise.setBasicPrice(1000);

        // Тривалість круїзу: 5 днів (15-10)
        double expected = 5 * 500 + 1000;  // 2500 + 1000 = 3500
        double actual = calc.calculateTotalPrice(stateroom, cruise);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    void testCalculateTotalPriceMinimumOneDay() {
        Calc calc = new Calc();

        Stateroom stateroom = new Stateroom();
        stateroom.setPrice(400);

        Cruise cruise = new Cruise();
        cruise.setStartDate(Date.valueOf("2025-06-10"));
        cruise.setEndDate(Date.valueOf("2025-06-10")); // одна дата = 0 днів різниці, мінімум 1 день має бути
        cruise.setBasicPrice(1000);

        double expected = 1 * 400 + 1000;  // 400 + 1000 = 1400
        double actual = calc.calculateTotalPrice(stateroom, cruise);

        assertEquals(expected, actual, 0.001);
    }
}
