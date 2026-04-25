package pricing;

public class DiscountService {
    
    public double calculateDiscount(double subtotal, CustomerType customerType, DiscountCode discountCode) {
        double codeDiscount = subtotal * discountCode.getRate();
        double customerDiscount = subtotal * customerType.getExtraDiscount();
        return codeDiscount + customerDiscount;
    }
}