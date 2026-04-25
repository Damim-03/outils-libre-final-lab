package pricing;

/**
 * Service responsible for calculating taxes
 */
public class TaxService {
    private static final double DEFAULT_TAX_RATE = 0.19;
    
    private final double taxRate;
    
    public TaxService() {
        this.taxRate = DEFAULT_TAX_RATE;
    }
    
    public TaxService(double taxRate) {
        this.taxRate = taxRate;
    }
    
    /**
     * Calculates tax on the given amount
     */
    public double calculateTax(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        return amount * taxRate;
    }
    
    public double getTaxRate() {
        return taxRate;
    }
}