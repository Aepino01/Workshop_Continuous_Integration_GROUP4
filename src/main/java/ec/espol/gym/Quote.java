package ec.espol.gym;

import java.math.BigDecimal;

/**
 * Breakdown of a valid quote, so the command line can show each step (R8).
 *
 * @param subtotal         (plan price + extras) x members (R3)
 * @param groupDiscount    amount subtracted by the group discount (R4)
 * @param specialOffer     amount subtracted by the special offer (R5)
 * @param premiumSurcharge amount added by the premium surcharge (R6)
 * @param total            final amount rounded half up (R7)
 */
public record Quote(
        BigDecimal subtotal,
        BigDecimal groupDiscount,
        BigDecimal specialOffer,
        BigDecimal premiumSurcharge,
        int total) {
}
