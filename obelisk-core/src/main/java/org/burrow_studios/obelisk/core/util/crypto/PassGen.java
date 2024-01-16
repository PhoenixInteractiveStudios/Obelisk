package org.burrow_studios.obelisk.core.util.crypto;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** A simple tool to generate passwords. */
public class PassGen {
    private static final char[] UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWER = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final char[] SYMBOL = "!@#$%^&*()_+-=".toCharArray();

    private PassGen() { }

    /**
     * Generates a password of a given length with at least {@code minDigits} digits and {@code minSymbols} symbols.
     * @param exclude A String containing characters that should not appear in the generated String.
     * @throws IllegalArgumentException if {@code length} is negative or 0 or if {@code minDigits} + {@code minSymbols}
     *                                  is greater than {@code length}.
     */
    public static @NotNull String generate(int length, boolean upper, boolean lower, boolean digits, boolean symbol, int minDigits, int minSymbols, @NotNull String exclude) throws IllegalArgumentException {
        if (length < 1)
            throw new IllegalArgumentException("Length must be at least 1");
        if (minDigits + minSymbols > length)
            throw new IllegalArgumentException("minDigits + minSymbols is bigger than length");
        if (!(upper || lower || digits || symbol))
            throw new IllegalArgumentException("Must at least have one character type enabled");

        if (!digits) minDigits  = 0;
        if (!symbol) minSymbols = 0;

        StringBuilder builder = new StringBuilder();
        SecureRandom  random  = new SecureRandom();

        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < minDigits; i++)
            builder.append(DIGITS[random.nextInt(DIGITS.length)]);
        for (int i = 0; i < minSymbols; i++)
            builder.append(SYMBOL[random.nextInt(SYMBOL.length)]);

        if (upper)
            for (char c : UPPER)
                chars.add(c);
        if (lower)
            for (char c : LOWER)
                chars.add(c);
        if (digits)
            for (char c : DIGITS)
                chars.add(c);
        if (symbol)
            for (char c : SYMBOL)
                chars.add(c);

        for (char exclusionChar : exclude.toCharArray())
            chars.remove(exclusionChar);

        if (chars.isEmpty())
            throw new IllegalArgumentException("All possible symbols are excluded");

        for (int i = minDigits + minSymbols; i < length; i++)
            builder.append(chars.get(random.nextInt(chars.size())));

        // shuffle the string to make sure digits & symbols at the start are distributed
        List<String> string = Arrays.asList(builder.toString().split(""));
        Collections.shuffle(string, random);

        StringBuilder result = new StringBuilder();
        for (String s : string)
            result.append(s);

        return result.toString();
    }

    /**
     * Generates a password of a given length with at least {@code minDigits} digits and {@code minSymbols} symbols.
     * @param exclude A String containing characters that should not appear in the generated String.
     * @throws IllegalArgumentException if {@code length} is negative or 0 or if {@code minDigits} + {@code minSymbols}
     *                                  is greater than {@code length}.
     */
    public static @NotNull String generate(int length, int minDigits, int minSymbols, @NotNull String exclude) throws IllegalArgumentException {
        return generate(length, true, true, true, true, minDigits, minSymbols, exclude);
    }

    /**
     * Generates a password of a given length with at least {@code minDigits} digits and {@code minSymbols} symbols.
     * @throws IllegalArgumentException if {@code length} is negative or 0 or if {@code minDigits} + {@code minSymbols}
     *                                  is greater than {@code length}.
     */
    public static @NotNull String generate(int length, int minDigits, int minSymbols) throws IllegalArgumentException {
        return generate(length, minDigits, minSymbols, "");
    }

    /**
     * Generates a password of a given length with at least 10% digits and 10% symbols.
     * @param exclude A String containing characters that should not appear in the generated String.
     * @throws IllegalArgumentException if {@code length} is negative or 0.
     */
    public static @NotNull String generate(int length, @NotNull String exclude) throws IllegalArgumentException {
        return generate(length, (int) (length / 10.0), (int) (length / 10.0), exclude);
    }

    /**
     * Generates a password of a given length with at least 10% digits and 10% symbols.
     * @throws IllegalArgumentException if {@code length} is negative or 0.
     */
    public static @NotNull String generate(int length) throws IllegalArgumentException {
        return generate(length, "");
    }
}
