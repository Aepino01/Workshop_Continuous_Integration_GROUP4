package ec.espol.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class MembershipCliTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    private int run(String input) {
        PrintStream out = new PrintStream(output, true, StandardCharsets.UTF_8);
        return MembershipCli.run(new Scanner(input), out);
    }

    private String printed() {
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void confirmedQuoteReturnsTotal() {
        assertEquals(215, run("PREMIUM\nGROUP_CLASSES, SPA\n2\ny\n"));
    }

    @Test
    void showsEveryStepOfTheBreakdown() {
        run("PREMIUM\nGROUP_CLASSES, SPA\n2\ny\n");
        String text = printed();
        assertTrue(text.contains("Subtotal:          230.00"), text);
        assertTrue(text.contains("Group discount:   -23.00"), text);
        assertTrue(text.contains("Special offer:    -20.00"), text);
        assertTrue(text.contains("Premium surcharge: +28.05"), text);
        assertTrue(text.contains("Total:             215"), text);
    }

    @Test
    void answeringNoCancels() {
        assertEquals(-1, run("BASIC\n\n1\nn\n"));
        assertTrue(printed().contains("Cancelled."));
    }

    @Test
    void acceptsLowerCaseAndNoExtras() {
        assertEquals(30, run("basic\n\n1\nY\n"));
    }

    @Test
    void invalidSelectionDoesNotAskForConfirmation() {
        assertEquals(-1, run("GOLD\n\n1\n"));
        assertTrue(printed().contains("Invalid selection."));
    }

    @Test
    void ignoresByteOrderMarkFromPipedInput() {
        assertEquals(30, run("﻿BASIC\n\n1\ny\n"));
    }

    @Test
    void membersThatAreNotANumberAreInvalid() {
        assertEquals(-1, run("BASIC\n\ntwo\n"));
    }
}
