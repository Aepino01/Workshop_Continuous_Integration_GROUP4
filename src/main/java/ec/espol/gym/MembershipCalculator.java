package ec.espol.gym;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Calculates the monthly cost of a gym membership.
 * The calculation does no input or output (R8).
 */
public final class MembershipCalculator {

    /** Result returned when the selection is invalid or the user cancels (R7). */
    public static final int INVALID = -1;

    /** Minimum number of members (R3). */
    public static final int MIN_MEMBERS = 1;

    private MembershipCalculator() {
    }

    /**
     * R1 and R2. Checks the plan, the extras and the number of members.
     * A selection is invalid when the plan or an extra is not in the table,
     * an extra is repeated, a premium extra is chosen without the PREMIUM plan,
     * or there are fewer than 1 member.
     *
     * @param plan    plan name
     * @param extras  extra names, possibly empty
     * @param members number of members
     * @return true if the selection can be quoted
     */
    public static boolean isValidSelection(String plan, List<String> extras, int members) {
        return parseExtras(plan, extras, members).isPresent();
    }

    /**
     * R3. Subtotal = (plan price + price of the extras) x number of members.
     *
     * @param plan    a valid plan
     * @param extras  valid extras
     * @param members number of members, at least 1
     * @return the subtotal
     */
    public static BigDecimal subtotal(Plan plan, List<Extra> extras, int members) {
        BigDecimal pricePerMember = plan.price();
        for (Extra extra : extras) {
            pricePerMember = pricePerMember.add(extra.price());
        }
        return pricePerMember.multiply(BigDecimal.valueOf(members));
    }

    /**
     * R7. Rounds half up to a whole number. Used only at the end.
     *
     * @param amount amount to round
     * @return the rounded amount
     */
    public static int roundHalfUp(BigDecimal amount) {
        return amount.setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    /**
     * Quotes a membership and returns each step of the calculation.
     *
     * @param plan    plan name
     * @param extras  extra names, possibly empty
     * @param members number of members
     * @return the breakdown, or empty if the selection is invalid
     */
    public static Optional<Quote> quote(String plan, List<String> extras, int members) {
        Optional<List<Extra>> parsedExtras = parseExtras(plan, extras, members);
        if (parsedExtras.isEmpty()) {
            return Optional.empty();
        }
        List<Extra> selected = parsedExtras.get();
        boolean hasPremiumExtra = selected.stream().anyMatch(Extra::isPremium);

        BigDecimal subtotal = subtotal(Plan.fromName(plan).orElseThrow(), selected, members);
        BigDecimal afterGroup = PriceAdjustments.applyGroupDiscount(subtotal, members);
        BigDecimal afterOffer = PriceAdjustments.applySpecialOffer(afterGroup);
        BigDecimal afterSurcharge = PriceAdjustments.applyPremiumSurcharge(afterOffer, hasPremiumExtra);

        return Optional.of(new Quote(
                subtotal,
                subtotal.subtract(afterGroup),
                afterGroup.subtract(afterOffer),
                afterSurcharge.subtract(afterOffer),
                roundHalfUp(afterSurcharge)));
    }

    /**
     * Quotes the monthly total of a membership.
     *
     * @param plan    plan name (BASIC, STANDARD or PREMIUM)
     * @param extras  extra names, possibly empty
     * @param members number of members, at least 1
     * @return the total rounded half up, or {@link #INVALID}
     */
    public static int calculateTotal(String plan, List<String> extras, int members) {
        return quote(plan, extras, members).map(Quote::total).orElse(INVALID);
    }

    private static Optional<List<Extra>> parseExtras(String planName, List<String> extraNames, int members) {
        Optional<Plan> plan = Plan.fromName(planName);
        if (plan.isEmpty() || extraNames == null || members < MIN_MEMBERS) {
            return Optional.empty();
        }
        List<Extra> extras = new ArrayList<>();
        Set<Extra> seen = new HashSet<>();
        for (String name : extraNames) {
            Optional<Extra> extra = Extra.fromName(name);
            if (extra.isEmpty() || !seen.add(extra.get())) {
                return Optional.empty();
            }
            if (extra.get().isPremium() && plan.get() != Plan.PREMIUM) {
                return Optional.empty();
            }
            extras.add(extra.get());
        }
        return Optional.of(extras);
    }
}
