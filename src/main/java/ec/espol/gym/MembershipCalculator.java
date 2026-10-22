package ec.espol.gym;

import java.util.List;

/**
 * Calculates the monthly cost of a gym membership.
 * The calculation does no input or output (R8).
 */
public final class MembershipCalculator {

    /** Result returned when the selection is invalid or the user cancels (R7). */
    public static final int INVALID = -1;

    private MembershipCalculator() {
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
        // Skeleton: R1-R7 are implemented in the feature branches.
        return INVALID;
    }
}
