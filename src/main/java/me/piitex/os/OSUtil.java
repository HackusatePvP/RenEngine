package me.piitex.os;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

/**
 * Simple utility for gathering operating system information.
 */
public class OSUtil {
    private static final SystemInfo SYSTEM_INFO = new SystemInfo();
    private static final OperatingSystem OS = SYSTEM_INFO.getOperatingSystem();

    public static String getOS() {
        return OS.toString(); // Returns something like "Ubuntu 22.04.1 LTS"
    }
}
