package ledger.legacy;

import java.util.HashMap;
import java.util.Map;

/**
 * Java-справочник клиентов. Методы возвращают platform types: в сигнатуре нет
 * null-аннотаций, поэтому Kotlin видит {@code LegacyCustomer!} и {@code String!}.
 */
public final class LegacyCustomerDirectory {

    private final Map<String, LegacyCustomer> customers = new HashMap<>();

    public LegacyCustomerDirectory() {
        customers.put("c-1", new LegacyCustomer("c-1", "Alice", "alice@example.com", "ali"));
        customers.put("c-2", new LegacyCustomer("c-2", "Bob", null, null));
        customers.put("c-3", new LegacyCustomer("c-3", "   ", "carol@example.org", "caz"));
        customers.put("c-4", new LegacyCustomer("c-4", null, "dave@example.net", null));
    }

    /** @return клиент или {@code null}, если id не найден. */
    public LegacyCustomer find(String id) {
        return customers.get(id);
    }

    /** @return nickname или {@code null}, если клиента нет или nickname не задан. */
    public String nicknameOf(String id) {
        LegacyCustomer customer = customers.get(id);
        return customer == null ? null : customer.getNickname();
    }

    /** Исторический метод: отдаёт внутреннюю mutable-мапу. */
    public Map<String, LegacyCustomer> all() {
        return customers;
    }
}
