package ec.espol.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MembershipCalculatorTest {

    // R1. Plans

    @Test
    void unknownPlanIsInvalid() {
        assertEquals(-1, MembershipCalculator.calculateTotal("GOLD", List.of(), 1));
    }

    @Test
    void everyPlanInTheTableIsValid() {
        assertTrue(MembershipCalculator.isValidSelection("BASIC", List.of(), 1));
        assertTrue(MembershipCalculator.isValidSelection("STANDARD", List.of(), 1));
        assertTrue(MembershipCalculator.isValidSelection("PREMIUM", List.of(), 1));
    }

    @Test
    void missingPlanIsInvalid() {
        assertFalse(MembershipCalculator.isValidSelection(null, List.of(), 1));
    }

    // R2. Extras

    @Test
    void unknownExtraIsInvalid() {
        assertFalse(MembershipCalculator.isValidSelection("BASIC", List.of("SAUNA"), 1));
    }

    @Test
    void premiumExtraWithoutPremiumPlanIsInvalid() {
        assertFalse(MembershipCalculator.isValidSelection("STANDARD", List.of("SPECIALIZED_PROGRAM"), 1));
    }

    @Test
    void premiumExtraWithPremiumPlanIsValid() {
        assertTrue(MembershipCalculator.isValidSelection("PREMIUM", List.of("SPA"), 1));
    }

    @Test
    void repeatedExtraIsInvalid() {
        assertFalse(MembershipCalculator.isValidSelection("BASIC", List.of("GROUP_CLASSES", "GROUP_CLASSES"), 1));
    }

    // R3. Subtotal

    @Test
    void subtotalAddsPlanAndExtrasPerMember() {
        BigDecimal subtotal = MembershipCalculator.subtotal(
                Plan.STANDARD, List.of(Extra.GROUP_CLASSES, Extra.PERSONAL_TRAINING), 4);
        assertEquals(0, new BigDecimal("340").compareTo(subtotal));
    }

    @Test
    void negativeMembersAreInvalid() {
        assertFalse(MembershipCalculator.isValidSelection("BASIC", List.of(), -2));
    }

    // R7. Result

    @Test
    void roundsHalfUp() {
        assertEquals(122, MembershipCalculator.roundHalfUp(new BigDecimal("121.5")));
    }

    @Test
    void roundsDownBelowHalf() {
        assertEquals(215, MembershipCalculator.roundHalfUp(new BigDecimal("215.05")));
    }

    @Test
    void intermediateStepsKeepDecimals() {
        Quote quote = MembershipCalculator.quote("PREMIUM", List.of("GROUP_CLASSES", "SPA"), 2).orElseThrow();
        assertEquals(0, new BigDecimal("28.05").compareTo(quote.premiumSurcharge()));
        assertEquals(215, quote.total());
    }

    @Test
    void invalidSelectionHasNoQuote() {
        assertTrue(MembershipCalculator.quote("BASIC", List.of("SPA"), 1).isEmpty());
    }
}
