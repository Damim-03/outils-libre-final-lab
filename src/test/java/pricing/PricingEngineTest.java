package pricing;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PricingEngineTest {

    PricingEngine engine = new PricingEngine();

    @Test
    void testRegularCustomerNoDiscount() {
        List<Double> prices = List.of(100.0, 200.0);
        List<Integer> quantities = List.of(1, 2);
        // subtotal = 500, tax 19% = 95, total = 595
        double result = engine.calc(prices, quantities, "REGULAR", "NONE");
        assertEquals(595.0, result, 0.01);
    }

    @Test
    void testSave10Discount() {
        List<Double> prices = List.of(100.0);
        List<Integer> quantities = List.of(1);
        // subtotal=100, disc=10, after=90, tax=17.1, total=107.1
        double result = engine.calc(prices, quantities, "REGULAR", "SAVE10");
        assertEquals(107.1, result, 0.01);
    }

    @Test
    void testVipCustomer() {
        List<Double> prices = List.of(100.0);
        List<Integer> quantities = List.of(1);
        // subtotal=100, disc=5%(VIP)=5, after=95, tax=18.05, total=113.05
        double result = engine.calc(prices, quantities, "VIP", "NONE");
        assertEquals(113.05, result, 0.01);
    }

    @Test
    void testVipWithSave20() {
        List<Double> prices = List.of(100.0);
        List<Integer> quantities = List.of(1);
        // disc = 20%+5% = 25, after=75, tax=14.25, total=89.25
        double result = engine.calc(prices, quantities, "VIP", "SAVE20");
        assertEquals(89.25, result, 0.01);
    }
}