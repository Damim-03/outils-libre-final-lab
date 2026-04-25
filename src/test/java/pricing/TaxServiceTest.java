package pricing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaxServiceTest {

    @Test
    void testDefaultTaxRate() {
        TaxService service = new TaxService();
        assertEquals(0.19, service.getTaxRate(), 0.01);
    }

    @Test
    void testCalculateTax() {
        TaxService service = new TaxService();
        double result = service.calculateTax(100.0);
        assertEquals(19.0, result, 0.01);
    }

    @Test
    void testCustomTaxRate() {
        TaxService service = new TaxService(0.10);
        double result = service.calculateTax(100.0);
        assertEquals(10.0, result, 0.01);
    }

    @Test
    void testNegativeAmountThrowsException() {
        TaxService service = new TaxService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.calculateTax(-100.0);
        });
    }
}