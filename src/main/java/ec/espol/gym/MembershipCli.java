package ec.espol.gym;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

/**
 * Command-line interface of the gym membership quote (R8).
 * All the calculation is delegated to {@link MembershipCalculator}.
 */
public final class MembershipCli {

    private MembershipCli() {
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
        int result = run(in, System.out);
        System.out.println("Result: " + result);
    }

    /**
     * Asks for plan, extras and members, shows the breakdown and asks for confirmation.
     *
     * @param in  source of the user's answers
     * @param out where the questions and the breakdown are written
     * @return the total, or -1 if the selection is invalid or the user cancels
     */
    public static int run(Scanner in, PrintStream out) {
        out.print("Plan (BASIC, STANDARD, PREMIUM): ");
        String plan = normalize(readLine(in));

        out.print("Extras separated by commas (GROUP_CLASSES, PERSONAL_TRAINING, SPA, "
                + "SPECIALIZED_PROGRAM), empty for none: ");
        List<String> extras = parseExtras(readLine(in));

        out.print("Number of members: ");
        int members = parseMembers(readLine(in));

        Optional<Quote> quote = MembershipCalculator.quote(plan, extras, members);
        if (quote.isEmpty()) {
            out.println("Invalid selection.");
            return MembershipCalculator.INVALID;
        }

        printBreakdown(quote.get(), out);
        out.print("Confirm (y/n): ");
        if (!"y".equalsIgnoreCase(readLine(in).trim())) {
            out.println("Cancelled.");
            return MembershipCalculator.INVALID;
        }
        return quote.get().total();
    }

    private static void printBreakdown(Quote quote, PrintStream out) {
        out.println("Subtotal:          " + money(quote.subtotal()));
        out.println("Group discount:   -" + money(quote.groupDiscount()));
        out.println("Special offer:    -" + money(quote.specialOffer()));
        out.println("Premium surcharge: +" + money(quote.premiumSurcharge()));
        out.println("Total:             " + quote.total());
    }

    private static String money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String readLine(Scanner in) {
        return in.hasNextLine() ? in.nextLine() : "";
    }

    private static String normalize(String text) {
        // Input piped from Windows PowerShell may start with a byte order mark.
        return text.replace("﻿", "").trim().toUpperCase(Locale.ROOT);
    }

    private static List<String> parseExtras(String line) {
        List<String> extras = new ArrayList<>();
        for (String part : line.split(",")) {
            String name = normalize(part);
            if (!name.isEmpty()) {
                extras.add(name);
            }
        }
        return extras;
    }

    private static int parseMembers(String line) {
        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
