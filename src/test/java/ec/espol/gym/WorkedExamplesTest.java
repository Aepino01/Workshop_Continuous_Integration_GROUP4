package ec.espol.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Integration tests: the worked examples table of the assignment (R9). */
class WorkedExamplesTest {

    private static List<String> extras(String names) {
        return "none".equals(names) ? List.of() : Arrays.asList(names.split(" "));
    }

    @ParameterizedTest(name = "{0} + {1} x {2} = {3}")
    @CsvSource({
        "BASIC,    none,                                            1,  30",
        "STANDARD, PERSONAL_TRAINING,                               1,  70",
        "BASIC,    GROUP_CLASSES,                                   2,  81",
        "BASIC,    GROUP_CLASSES,                                   3, 122",
        "STANDARD, GROUP_CLASSES PERSONAL_TRAINING,                 4, 286",
        "PREMIUM,  GROUP_CLASSES SPA,                               2, 215",
        "PREMIUM,  SPA SPECIALIZED_PROGRAM,                         3, 412",
        "PREMIUM,  SPA SPECIALIZED_PROGRAM PERSONAL_TRAINING,       4, 626",
        "BASIC,    SPA,                                             1,  -1",
        "GOLD,     none,                                            1,  -1",
        "BASIC,    none,                                            0,  -1",
    })
    void matchesTheWorkedExample(String plan, String extras, int members, int expected) {
        assertEquals(expected, MembershipCalculator.calculateTotal(plan, extras(extras), members));
    }
}
