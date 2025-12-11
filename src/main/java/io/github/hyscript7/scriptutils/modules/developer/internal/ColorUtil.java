package io.github.hyscript7.scriptutils.modules.developer.internal;

public class ColorUtil {
    // TODO: Replace with a library, as I cannot be bothered writing my own parser

    /**
     * Takes in a string with either a color name like "red", "dark blue", "light purple" or
     * a hex color string like 0xFFFFFF or #333333.
     * @param colorString The color string
     * @return An integer representing the color or null (capped to 255,255,255 and 0)
     */
    public static Integer parseColor(String colorString) {
        int radix = colorString.startsWith("#") || colorString.startsWith("0x") ? 16 : 10;
        try {
            return Math.min(0xFFFFFF,Math.abs(Integer.parseInt(colorString, radix)));
        } catch (NumberFormatException ignored) {}

        String cleaned = colorString.toLowerCase().trim()
                .replaceAll("[^a-zA-Z0-9 ]", "");

        boolean dark = cleaned.startsWith("dark ");
        boolean light = cleaned.startsWith("light ");

        cleaned = cleaned.replaceFirst("^(light|dark)\\s+", "");

        Integer color = switch (cleaned) {
            case "black"  -> 0x010101;
            case "red"    -> 0xFF0000;
            case "green"  -> 0x00FF00;
            case "yellow" -> 0xFFFF00;
            case "blue"   -> 0x0000FF;
            case "purple" -> 0xFF00FF;
            case "cyan"   -> 0x00FFFF;
            case "white"  -> 0xFFFFFF;
            default -> null;
        };

        if (color == null) return null;

        if (dark) {
            color = adjustColorBrightness(color, 0.6);
        } else if (light) {
            color = adjustColorBrightness(color, 1.4);
        }

        return color;
    }

    /**
     * Darkens or lightens a color
     * @param rgb The original color
     * @param factor By how many % to change the color (1 = no change, 0.5 = 50% darker, 1.5 = 50% lighter)
     * @return The modified color
     */
    public static int adjustColorBrightness(int rgb, double factor) {
        int r = Math.min(255, (int) (((rgb >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((rgb >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((rgb & 0xFF) * factor));
        return (r << 16) | (g << 8) | b;
    }
}
