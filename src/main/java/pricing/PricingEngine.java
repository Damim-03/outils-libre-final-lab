package pricing;

import java.util.List;

public class PricingEngine {
    
    private final OrderCalculator orderCalculator;
    private final DiscountService discountService;
    private final TaxService taxService;
    
    public PricingEngine() {
        this.orderCalculator = new OrderCalculator();
        this.discountService = new DiscountService();
        this.taxService = new TaxService();
    }
    
    public PricingEngine(OrderCalculator orderCalculator,
                         DiscountService discountService,
                         TaxService taxService) {
        this.orderCalculator = orderCalculator;
        this.discountService = discountService;
        this.taxService = taxService;
    }
    
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