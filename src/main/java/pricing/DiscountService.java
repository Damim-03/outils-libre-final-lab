package pricing;

/**
 * Service responsible for calculating discounts
 * based on discount codes and customer types
 */
public class DiscountService {
    
    /**
     * Calculates the total discount based on discount code and customer type
     */
    public double calculateDiscount(double subtotal, CustomerType customerType, DiscountCode discountCode) {
        double codeDiscount = subtotal * discountCode.getRate();
        double customerDiscount = subtotal * customerType.getExtraDiscount();
        return codeDiscount + customerDiscount;
    }
}