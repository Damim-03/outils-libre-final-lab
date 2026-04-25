package pricing;

public enum CustomerType {
    REGULAR(0.0),
    VIP(0.05);
    
    private final double extraDiscount;
    
    CustomerType(double extraDiscount) {
        this.extraDiscount = extraDiscount;
    }
    
    public double getExtraDiscount() {
        return extraDiscount;
    }
}