package me.piitex.os;

import java.io.File;

public class OSPathing {

    public static File getDocumentsDirectory() {
        String userHome = System.getProperty("user.home");
        if (OSUtil.getOS().contains("Windows")) {
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
            jdk.mkdir();
        }

        if (documents) {
            if (!getDocumentsDirectory().exists()) {
                getDocumentsDirectory().mkdir();
            }
        }
        if (appData) {
            if (!getAppDataDirectory().exists()) {
                getAppDataDirectory().mkdir();
            }
        }
    }

}
