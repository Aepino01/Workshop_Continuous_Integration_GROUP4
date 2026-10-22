package ec.espol.gym;

import java.math.BigDecimal;

/**
 * Discounts and surcharges applied to an amount (R4, R5 and R6).
 * Each function receives an amount and returns a new amount; none of them rounds.
 */
public final class PriceAdjustments {

    /** Members needed for the group discount (R4). */
    public static final int GROUP_MIN_MEMBERS = 2;

    private static final BigDecimal GROUP_DISCOUNT_RATE = new BigDecimal("0.10");
    private static final BigDecimal HIGH_OFFER_THRESHOLD = new BigDecimal("400");
    private static final BigDecimal HIGH_OFFER_AMOUNT = new BigDecimal("50");
    private static final BigDecimal LOW_OFFER_THRESHOLD = new BigDecimal("200");
    private static final BigDecimal LOW_OFFER_AMOUNT = new BigDecimal("20");
    private static final BigDecimal PREMIUM_SURCHARGE_RATE = new BigDecimal("0.15");

    private PriceAdjustments() {
    }

    /**
     * R4. With 2 or more members, the amount gets a 10% discount.
     *
     * @param subtotal amount before the discount
     * @param members  number of members
     * @return the amount after the group discount
     */
    public static BigDecimal applyGroupDiscount(BigDecimal subtotal, int members) {
        if (members < GROUP_MIN_MEMBERS) {
            return subtotal;
        }
        return subtotal.subtract(subtotal.multiply(GROUP_DISCOUNT_RATE));
    }

    /**
     * R5. Greater than 400 subtracts 50; otherwise greater than 200 subtracts 20.
     * The two offers never add up.
     *
     * @param amount amount after the group discount
     * @return the amount after the special offer
     */
    public static BigDecimal applySpecialOffer(BigDecimal amount) {
        if (amount.compareTo(HIGH_OFFER_THRESHOLD) > 0) {
            return amount.subtract(HIGH_OFFER_AMOUNT);
        }
        if (amount.compareTo(LOW_OFFER_THRESHOLD) > 0) {
            return amount.subtract(LOW_OFFER_AMOUNT);
        }
        return amount;
    }

    /**
     * R6. If the selection includes a premium extra, adds 15% to the amount.
     *
     * @param amount          amount after the special offer
     * @param hasPremiumExtra whether at least one premium extra was selected
     * @return the amount after the premium surcharge
     */
    public static BigDecimal applyPremiumSurcharge(BigDecimal amount, boolean hasPremiumExtra) {
        if (!hasPremiumExtra) {
            return amount;
        }
        return amount.add(amount.multiply(PREMIUM_SURCHARGE_RATE));
    }
}
