package pricing;

import java.util.List;

/**
 * Service responsible for calculating order subtotal
 */
public class OrderCalculator {
    
    /**
     * Calculates the subtotal by multiplying each price by its quantity
     */
    public double calculateSubtotal(List<Double> prices, List<Integer> quantities) {
        validateInputs(prices, quantities);
        
        double subtotal = 0;
        for (int i = 0; i < prices.size(); i++) {
            subtotal += prices.get(i) * quantities.get(i);
        }
        return subtotal;
    }
    
    private void validateInputs(List<Double> prices, List<Integer> quantities) {
        if (prices == null || quantities == null) {
            throw new IllegalArgumentException("Prices and quantities cannot be null");
        }
        if (prices.size() != quantities.size()) {
            throw new IllegalArgumentException("Prices and quantities must have the same size");
        }
        if (prices.isEmpty()) {
            throw new IllegalArgumentException("Prices list cannot be empty");
        }
    }
}