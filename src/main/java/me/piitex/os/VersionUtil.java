package me.piitex.os;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtil {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^([a-zA-Z]*)(\\d+)([a-zA-Z]*)$");

    public static Version parseVersion(String version) {
        if (version.contains("-")) {
            version = version.replace("-", ""); // Dashes are not valid characters for versions.
            // Example: project-1.0-snapshot -> project1.0snapshot
            // Prefix: project
            // Version: 1.0
            // Suffix: snapshot
        }
        version = version.replace(".", "");

        Matcher matcher = VERSION_PATTERN.matcher(version);

        if (matcher.matches()) {
            // Group 1: Prefix (e.g., "v" from "v12a")
            String prefix = matcher.group(1);

            // Group 2: Numerical Value (e.g., "12" from "v12.1.2a")
            // We use Integer.parseInt() on the string captured by this group.
            int numericalValue = Integer.parseInt(matcher.group(2));

            // Group 3: Suffix (e.g., "a" from "v12a")
            String suffix = matcher.group(3);
            return new Version(prefix, numericalValue, suffix);
        } else {
            return null;
        }
    }
}
