package pricing;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrderCalculatorTest {

    OrderCalculator calculator = new OrderCalculator();

    @Test
    void testCalculateSubtotalSingleItem() {
        double result = calculator.calculateSubtotal(List.of(100.0), List.of(1));
        assertEquals(100.0, result, 0.01);
    }

    @Test
    void testCalculateSubtotalMultipleItems() {
        double result = calculator.calculateSubtotal(
            List.of(100.0, 200.0, 50.0),
            List.of(2, 1, 4)
        );
        // 200 + 200 + 200 = 600
        assertEquals(600.0, result, 0.01);
    }

    @Test
    void testNullPricesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateSubtotal(null, List.of(1));
        });
    }

    @Test
    void testEmptyListThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateSubtotal(List.of(), List.of());
        });
    }
}