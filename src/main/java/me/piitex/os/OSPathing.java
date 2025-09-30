package me.piitex.os;

import java.io.File;

public class OSPathing {

    public static File getDocuments() {
        String userHome = System.getProperty("user.home");
        return new File(userHome + File.separator + "Documents");
    }

    public static File getAppData() {
        String userHome = System.getProperty("user.home");
        String os = OSUtility.getOS();

        if (os.equalsIgnoreCase("Windows")) {
            String localAppData = System.getenv("APPDATA");
            if (localAppData != null) {
                return new File(localAppData);
            }
        }

        return new File(userHome + File.separator + ".local" + File.separator + "share");
    }

    public static File getHome() {
        return new File(System.getProperty("user.home"));
    }

}
