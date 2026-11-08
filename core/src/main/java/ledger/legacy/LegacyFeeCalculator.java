package ledger.legacy;

/**
 * Наследие Java-кода: тарифная сетка, которую нельзя переписывать в первом модуле.
 *
 * <p>Обратите внимание: {@link #describe(String)} возвращает {@code null} для неизвестной
 * валюты. Для Kotlin это platform type {@code String!}, поэтому вызывающий код обязан
 * обработать возможный null.</p>
 */
public final class LegacyFeeCalculator {

    public long feeFor(long amountMinor, String currency) {
        if (amountMinor <= 0) {
            return 0L;
        }
        return switch (currency) {
            case "USD" -> Math.max(30L, amountMinor * 25 / 10_000);
            case "EUR" -> Math.max(25L, amountMinor * 20 / 10_000);
            case "JPY" -> 0L;
            default -> Math.max(50L, amountMinor * 50 / 10_000);
        };
    }

    /** Java-overload без валюты: исторически USD. */
    public long feeFor(long amountMinor) {
        return feeFor(amountMinor, "USD");
    }

    /** @return человекочитаемое название валюты или {@code null}, если валюта неизвестна. */
    public static String describe(String currency) {
        return switch (currency) {
            case "USD" -> "US dollar";
            case "EUR" -> "Euro";
            case "GBP" -> "Pound sterling";
            case "JPY" -> "Yen";
            default -> null;
        };
    }
}
