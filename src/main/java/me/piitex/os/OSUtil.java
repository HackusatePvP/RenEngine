package me.piitex.os;

import org.apache.commons.lang3.SystemUtils;

/**
 * Simple utility for gathering operating system information.
 */
public class OSUtil {

    public static String getOS() {
        return SystemUtils.OS_NAME;
    }

    public static String getVersion() {
        return SystemUtils.OS_VERSION;
    }
}
