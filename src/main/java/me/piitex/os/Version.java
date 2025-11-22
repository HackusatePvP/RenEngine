package me.piitex.os;

import org.jetbrains.annotations.NotNull;

public class Version implements Comparable {
    private final String prefix;
    private final double version;
    private final String suffix;

    /**
     * Constructor for creating a version object. Used for comparing and updating third party software.
     * <p>
     *     Version Example: a1234b
     * </p>
     *
     * The comparison of versions works by the order of the letters and numbers.
     * Letters will be sorted ascended by alphabetical order. A is 0 and B is 1.
     * The higher the number the greater the version. The version `0` will be the seen as the earliest possible version. The version `a0a` is the same as `0`.
     *
     * @param prefix Optional prefix for the version. Example `a`
     * @param version The version number as an Integer. Example `1234`
     * @param suffix Optional suffix for the version. `Example b`
     */
    public Version(String prefix, int version, String suffix) {
        this.prefix = prefix;
        this.version = version;
        this.suffix = suffix;
    }

    /**
     * Constructor for creating a version object. Used for comparing and updating third party software.
     * <p>
     *     Version Example: 1234
     * </p>
     *
     * The comparison of versions works by the order of the letters and numbers.
     * Letters will be sorted ascended by alphabetical order. A is 0 and B is 1.
     * The higher the number the greater the version. The version `0` will be the seen as the earliest possible version. The version `a0a` is the same as `0`.
     *
     * @param version The version number as an Integer. Example `1234`
     */
    public Version(int version) {
        this.prefix = "";
        this.version = version;
        this.suffix = "";
    }

    public String getPrefix() {
        return prefix;
    }

    public double getVersion() {
        return version;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getCalculatedVersion() {
        int total = 0;
        for (char c : getPrefix().toCharArray()) {
            total += getCharInt(c);
        }
        total += version;
        for (char c : getSuffix().toCharArray()) {
            total += getCharInt(c);
        }
        return total;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        Version other = (Version) o;

        return getCalculatedVersion() - other.getCalculatedVersion();
    }

    private static int getCharInt(char c) {
        char upperCaseLetter = Character.toUpperCase(c);

        // Check if the character is an English alphabet letter
        if (upperCaseLetter >= 'A' && upperCaseLetter <= 'Z') {
            // Calculate the zero-based index: 'A' maps to 0
            return upperCaseLetter - 'A';
        } else {
            // Handle non-alphabet characters (e.g., space, numbers)
            return 0;
        }
    }
}
