package pricing;

import java.util.List;

// BAD DESIGN - everything in one class, no separation of concerns
public class PricingEngine_bad {

    public double calc(List<Double> prices, List<Integer> quantities,
                       String customerType, String discountCode) {
        double sub = 0;
        for (int i = 0; i < prices.size(); i++) {
            sub += prices.get(i) * quantities.get(i);
        }

        double disc = 0;
        if (discountCode.equals("SAVE10")) disc = sub * 0.10;
        else if (discountCode.equals("SAVE20")) disc = sub * 0.20;

        if (customerType.equals("VIP")) disc += sub * 0.05;

        double afterDisc = sub - disc;
        double tax = afterDisc * 0.19;
        double total = afterDisc + tax;
        return total;
    }
}