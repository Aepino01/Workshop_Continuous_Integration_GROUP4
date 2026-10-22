package ec.espol.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class MembershipCalculatorTest {

    @Test
    void unknownPlanIsInvalid() {
        assertEquals(-1, MembershipCalculator.calculateTotal("GOLD", List.of(), 1));
    }
}
