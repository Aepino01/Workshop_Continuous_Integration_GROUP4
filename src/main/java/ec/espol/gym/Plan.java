package ec.espol.gym;

import java.math.BigDecimal;
import java.util.Optional;

/** Membership plans and their price per member (R1). */
public enum Plan {
    BASIC(30),
    STANDARD(45),
    PREMIUM(70);

    private final BigDecimal price;

    Plan(int price) {
        this.price = BigDecimal.valueOf(price);
    }

    public BigDecimal price() {
        return price;
    }

    /**
     * Finds a plan by its exact name.
     *
     * @param name plan name, for example "BASIC"
     * @return the plan, or empty if the name is not in the table
     */
    public static Optional<Plan> fromName(String name) {
        for (Plan plan : values()) {
            if (plan.name().equals(name)) {
                return Optional.of(plan);
            }
        }
        return Optional.empty();
    }
}
