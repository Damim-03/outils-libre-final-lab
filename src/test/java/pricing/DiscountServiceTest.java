package pricing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountServiceTest {

    DiscountService service = new DiscountService();

    @Test
    void testNoDiscount() {
        double result = service.calculateDiscount(100.0, CustomerType.REGULAR, DiscountCode.NONE);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testSave10Discount() {
        double result = service.calculateDiscount(100.0, CustomerType.REGULAR, DiscountCode.SAVE10);
        assertEquals(10.0, result, 0.01);
    }

    @Test
    void testVipNoCode() {
        double result = service.calculateDiscount(100.0, CustomerType.VIP, DiscountCode.NONE);
        assertEquals(5.0, result, 0.01);
    }

    @Test
    void testVipWithSave20() {
        double result = service.calculateDiscount(100.0, CustomerType.VIP, DiscountCode.SAVE20);
        // 20 + 5 = 25
        assertEquals(25.0, result, 0.01);
    }
}