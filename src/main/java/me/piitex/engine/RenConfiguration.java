package me.piitex.engine;

public class RenConfiguration {
    private static int width = 1920;
    private static int height = 1080;
    private static double currentWindowWidth;
    private static double currentWindowHeight;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static double getWidthScale() {
        return currentWindowWidth / width;
    }

    public static double getHeightScale() {
        return currentWindowHeight / height;
    }

    public static double getCurrentWindowWidth() {
        return currentWindowWidth;
    }

    public static void setCurrentWindowWidth(double currentWindowWidth) {
        RenConfiguration.currentWindowWidth = currentWindowWidth;
    }

    public static double getCurrentWindowHeight() {
        return currentWindowHeight;
    }

    public static void setCurrentWindowHeight(double currentWindowHeight) {
        RenConfiguration.currentWindowHeight = currentWindowHeight;
    }

    public static void setWidth(int width) {
        RenConfiguration.width = width;
    }

    public static void setHeight(int height) {
        RenConfiguration.height = height;
    }
}
