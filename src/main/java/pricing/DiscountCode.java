package pricing;

public enum DiscountCode {
    NONE(0.0),
    SAVE5(0.05),
    SAVE10(0.10),
    SAVE20(0.20);
    
    private final double rate;
    
    DiscountCode(double rate) {
        this.rate = rate;
    }
    
    public double getRate() {
        return rate;
    }
}