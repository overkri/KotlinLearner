package ledger.legacy;

/**
 * Запись из legacy-справочника. Часть полей может быть {@code null}:
 * в старой схеме это нормальное состояние, а не ошибка.
 */
public final class LegacyCustomer {

    private final String id;
    private final String name;
    private final String email;
    private final String nickname;

    public LegacyCustomer(String id, String name, String email, String nickname) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
