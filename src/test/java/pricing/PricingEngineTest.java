package pricing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PricingEngineTest {

    private PricingEngine engine;
    
    @BeforeEach
    void setUp() {
        engine = new PricingEngine();
    }

    @Test
    void testRegularCustomerNoDiscount() {
        List<Double> prices = List.of(100.0, 200.0);
        List<Integer> quantities = List.of(1, 2);
        
        PriceBreakdown result = engine.calculate(
            prices, quantities, 
            CustomerType.REGULAR, 
            DiscountCode.NONE
        );
        
        assertEquals(500.0, result.getSubtotal(), 0.01);
        assertEquals(0.0, result.getDiscount(), 0.01);
        assertEquals(95.0, result.getTax(), 0.01);
        assertEquals(595.0, result.getFinalPrice(), 0.01);
    }

    @Test
    void testSave10Discount() {
        List<Double> prices = List.of(100.0);
        List<Integer> quantities = List.of(1);
        
        PriceBreakdown result = engine.calculate(
            prices, quantities, 
            CustomerType.REGULAR, 
            DiscountCode.SAVE10
        );
        
        assertEquals(107.1, result.getFinalPrice(), 0.01);
    }

    @Test
    void testVipCustomer() {
        List<Double> prices = List.of(100.0);
        List<Integer> quantities = List.of(1);
        
        PriceBreakdown result = engine.calculate(
            prices, quantities, 
            CustomerType.VIP, 
            DiscountCode.NONE
        );
        
        assertEquals(113.05, result.getFinalPrice(), 0.01);
    }

    @Test
    void testVipWithSave20() {
        List<Double> prices = List.of(100.0);
        List<Integer> quantities = List.of(1);
        
        PriceBreakdown result = engine.calculate(
            prices, quantities, 
            CustomerType.VIP, 
            DiscountCode.SAVE20
        );
        
        assertEquals(89.25, result.getFinalPrice(), 0.01);
    }

    @Test
    void testNullInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.calculate(null, null, CustomerType.REGULAR, DiscountCode.NONE);
        });
    }

    @Test
    void testMismatchedSizesThrowsException() {
        List<Double> prices = List.of(100.0, 200.0);
        List<Integer> quantities = List.of(1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            engine.calculate(prices, quantities, CustomerType.REGULAR, DiscountCode.NONE);
        });
    }
}