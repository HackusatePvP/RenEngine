package me.piitex.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class OSPathing {
    private static final Logger logger = LoggerFactory.getLogger(OSPathing.class);

    public static String groupId = "me.piitex.app.App"; // Only used for linux

    public static File getDocumentsDirectory() {
        String userHome = System.getProperty("user.home");
        return new File(userHome + File.separator + "Documents");
    }

    public static File getAppDataDirectory() {
        String userHome = System.getProperty("user.home");
        String os = OSUtil.getOS();

        if (os.equalsIgnoreCase("Windows")) {
            String localAppData = System.getenv("APPDATA");
            if (localAppData != null) {
                return new File(localAppData);
            }
        } else if (os.contains("Linux")) {
            validateAppData();
            File file = new File(userHome + "/.var/app/" + groupId + "/");
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) {
                        logger.info("Created appdata directory...");
                    }
                } catch (IOException e) {
                    logger.error("Could not create appdata folder!", e);
                }
            }
            return new File(userHome + "/.var/app/" + groupId + "/");
        }

        return new File(userHome + "/");
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

    private static void validateAppData() {
        if (OSUtil.getOS().contains("Linux") && groupId.equals("me.piitex.app.App")) {
            throw new RuntimeException("You must set the linux group id for the application! OSPathing.groupId = \"your.group.app\"");
        }
    }

}
