package pricing;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        PricingEngine engine = new PricingEngine();
        
        if (args.length == 4) {
            runCliMode(args, engine);
        } else if (args.length == 0) {
            runDemoMode(engine);
        } else {
            printUsage();
            System.exit(1);
        }
    }

    private static void runCliMode(String[] args, PricingEngine engine) {
        try {
            List<Double> prices = parsePrices(args[0]);
            List<Integer> quantities = parseQuantities(args[1]);
            CustomerType customerType = parseCustomerType(args[2]);
            DiscountCode discountCode = parseDiscountCode(args[3]);

            PriceBreakdown result = engine.calculate(prices, quantities, customerType, discountCode);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runDemoMode(PricingEngine engine) {
        System.out.println("=== Pricing Engine Demo ===");
        System.out.println("Sample Order: 2x $100 + 1x $50, VIP customer, SAVE10 code\n");
        
        PriceBreakdown result = engine.calculate(
            List.of(100.0, 50.0),
            List.of(2, 1),
            CustomerType.VIP,
            DiscountCode.SAVE10
        );
        
        System.out.println(result);
    }

    private static List<Double> parsePrices(String raw) {
        String cleaned = raw.replaceAll("[\\[\\]\\s]", "");
        if (cleaned.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(cleaned.split(","))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    private static List<Integer> parseQuantities(String raw) {
        String cleaned = raw.replaceAll("[\\[\\]\\s]", "");
        if (cleaned.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(cleaned.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static CustomerType parseCustomerType(String raw) {
        try {
            return CustomerType.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid customer type: " + raw + ". Valid values: REGULAR, VIP"
            );
        }
    }

    private static DiscountCode parseDiscountCode(String raw) {
        if (raw == null || "null".equalsIgnoreCase(raw) || raw.isEmpty()) {
            return DiscountCode.NONE;
        }
        try {
            return DiscountCode.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid discount code: " + raw + ". Valid values: NONE, SAVE5, SAVE10, SAVE20"
            );
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  Demo mode:  java -jar pricing-engine.jar");
        System.out.println("  CLI mode:   java -jar pricing-engine.jar [prices] [quantities] [customerType] [discountCode]");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java -jar pricing-engine.jar \"[100.0,200.0]\" \"[1,2]\" VIP SAVE20");
        System.out.println();
        System.out.println("Customer Types: REGULAR, VIP");
        System.out.println("Discount Codes: NONE, SAVE5, SAVE10, SAVE20");
    }
}