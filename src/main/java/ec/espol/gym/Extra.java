package ec.espol.gym;

import java.math.BigDecimal;
import java.util.Optional;

/** Extras, their price per member and whether they are premium (R2). */
public enum Extra {
    GROUP_CLASSES(15, false),
    PERSONAL_TRAINING(25, false),
    SPA(30, true),
    SPECIALIZED_PROGRAM(40, true);

    private final BigDecimal price;
    private final boolean premium;

    Extra(int price, boolean premium) {
        this.price = BigDecimal.valueOf(price);
        this.premium = premium;
    }

    public BigDecimal price() {
        return price;
    }

    /** A premium extra is available only with the PREMIUM plan. */
    public boolean isPremium() {
        return premium;
    }

    /**
     * Finds an extra by its exact name.
     *
     * @param name extra name, for example "SPA"
     * @return the extra, or empty if the name is not in the table
     */
    public static Optional<Extra> fromName(String name) {
        for (Extra extra : values()) {
            if (extra.name().equals(name)) {
                return Optional.of(extra);
            }
        }
        return Optional.empty();
    }
}
