package pricing;

import java.util.List;

public class PricingEngine {
    private static final double TAX_RATE = 0.19;
    
    public PriceBreakdown calculate(List<Double> prices, List<Integer> quantities,
                                     CustomerType customerType, DiscountCode discountCode) {
        validateInputs(prices, quantities);
        
        double subtotal = calculateSubtotal(prices, quantities);
        double discount = calculateDiscount(subtotal, customerType, discountCode);
        double afterDiscount = subtotal - discount;
        double tax = afterDiscount * TAX_RATE;
        double finalPrice = afterDiscount + tax;
        
        return new PriceBreakdown(subtotal, discount, tax, finalPrice);
    }
    
    private void validateInputs(List<Double> prices, List<Integer> quantities) {
        if (prices == null || quantities == null) {
            throw new IllegalArgumentException("Prices and quantities cannot be null");
        }
        if (prices.size() != quantities.size()) {
            throw new IllegalArgumentException("Prices and quantities must have same size");
        }
    }
    
    private double calculateSubtotal(List<Double> prices, List<Integer> quantities) {
        double subtotal = 0;
        for (int i = 0; i < prices.size(); i++) {
            subtotal += prices.get(i) * quantities.get(i);
        }
        return subtotal;
    }
    
    private double calculateDiscount(double subtotal, CustomerType customerType, DiscountCode code) {
        double discount = subtotal * code.getRate();
        discount += subtotal * customerType.getExtraDiscount();
        return discount;
    }
}