package me.piitex.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class OSPathing {
    private static final Logger logger = LoggerFactory.getLogger(OSPathing.class);

    public static File getDocumentsDirectory() {
        String userHome = System.getProperty("user.home");
        if (OSUtil.getOS().toLowerCase().contains("window")) {
            return new File(userHome + File.separator + "Documents");
        } else {
            return new File(userHome + File.separator + ".local" + File.separator + "share");
        }
    }

    public static File getAppDataDirectory() {
        String userHome = System.getProperty("user.home");
        String os = OSUtil.getOS();

        if (os.equalsIgnoreCase("Windows")) {
            String localAppData = System.getenv("APPDATA");
            if (localAppData != null) {
                return new File(localAppData);
            }
        }

        return new File(userHome + File.separator + ".local" + File.separator + "share");
    }

    public static File getHomeDirectory() {
        return new File(System.getProperty("user.home"));
    }

    public static File getRunningDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    public static void initiateProjectDirectories(String project, boolean documents, boolean appData) {
        File jdk = new File(getRunningDirectory(), "/jdk/");
        if (!jdk.exists()) {
            if (!jdk.mkdir()) {
                logger.error("Failed to create/find JDK directory.");
            }
        }

        if (documents) {
            if (!getDocumentsDirectory().exists()) {
                if (!getDocumentsDirectory().mkdir()) {
                    logger.error("Failed to create/find documents directory.");
                }
            }
        }
        if (appData) {
            if (!getAppDataDirectory().exists()) {
                if (!getAppDataDirectory().mkdir()) {
                    logger.error("Failed to create/find appdata directory.");
                }
            }
        }
    }

}
