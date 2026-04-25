package pricing;

import java.util.List;

/**
 * Main pricing engine that orchestrates the calculation
 * of order prices using specialized services.
 */
public class PricingEngine {
    
    private final OrderCalculator orderCalculator;
    private final DiscountService discountService;
    private final TaxService taxService;
    
    /**
     * Default constructor that creates default services
     */
    public PricingEngine() {
        this.orderCalculator = new OrderCalculator();
        this.discountService = new DiscountService();
        this.taxService = new TaxService();
    }
    
    /**
     * Constructor with dependency injection (useful for testing)
     */
    public PricingEngine(OrderCalculator orderCalculator,
                         DiscountService discountService,
                         TaxService taxService) {
        this.orderCalculator = orderCalculator;
        this.discountService = discountService;
        this.taxService = taxService;
    }
    
    /**
     * Calculates the complete price breakdown for an order
     */
    public PriceBreakdown calculate(List<Double> prices,
                                     List<Integer> quantities,
                                     CustomerType customerType,
                                     DiscountCode discountCode) {
        
        double subtotal = orderCalculator.calculateSubtotal(prices, quantities);
        double discount = discountService.calculateDiscount(subtotal, customerType, discountCode);
        double afterDiscount = subtotal - discount;
        double tax = taxService.calculateTax(afterDiscount);
        double finalPrice = afterDiscount + tax;
        
        return new PriceBreakdown(subtotal, discount, tax, finalPrice);
    }
}