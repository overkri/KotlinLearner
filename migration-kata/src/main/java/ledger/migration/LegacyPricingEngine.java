package ledger.migration;

/**
 * Наследие: тарифный калькулятор на Java. Kotlin-версия должна вести себя
 * побайтово так же, включая целочисленное округление и сообщения об ошибках.
 */
public final class LegacyPricingEngine {

    public long priceCents(int quantity, long unitPriceCents, String tier) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        long subtotal = Math.multiplyExact((long) quantity, unitPriceCents);
        return switch (tier) {
            case "STANDARD" -> subtotal;
            case "SILVER" -> subtotal * 95 / 100;
            case "GOLD" -> subtotal * 90 / 100;
            default -> throw new IllegalArgumentException("unknown tier: " + tier);
        };
    }
}
