package ec.espol.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceAdjustmentsTest {

    private static void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual),
                () -> "expected " + expected + " but was " + actual);
    }

    // R4. Group discount

    @Test
    void oneMemberGetsNoGroupDiscount() {
        assertAmount("30", PriceAdjustments.applyGroupDiscount(new BigDecimal("30"), 1));
    }

    @Test
    void twoMembersGetTenPercentDiscount() {
        assertAmount("81", PriceAdjustments.applyGroupDiscount(new BigDecimal("90"), 2));
    }

    @Test
    void groupDiscountKeepsDecimals() {
        assertAmount("121.5", PriceAdjustments.applyGroupDiscount(new BigDecimal("135"), 3));
    }

    // R5. Special offer

    @Test
    void amountOfTwoHundredGetsNoOffer() {
        assertAmount("200", PriceAdjustments.applySpecialOffer(new BigDecimal("200")));
    }

    @Test
    void amountAboveTwoHundredSubtractsTwenty() {
        assertAmount("187", PriceAdjustments.applySpecialOffer(new BigDecimal("207")));
    }

    @Test
    void amountOfFourHundredSubtractsOnlyTwenty() {
        assertAmount("380", PriceAdjustments.applySpecialOffer(new BigDecimal("400")));
    }

    @Test
    void amountAboveFourHundredSubtractsOnlyFifty() {
        assertAmount("544", PriceAdjustments.applySpecialOffer(new BigDecimal("594")));
    }

    // R6. Premium surcharge

    @Test
    void noPremiumExtraHasNoSurcharge() {
        assertAmount("286", PriceAdjustments.applyPremiumSurcharge(new BigDecimal("286"), false));
    }

    @Test
    void premiumExtraAddsFifteenPercent() {
        assertAmount("215.05", PriceAdjustments.applyPremiumSurcharge(new BigDecimal("187"), true));
    }
}
