package com.brandon3055.draconicevolution.config;

/**
 * Parses the colour config options once, at config load, so that renderers only read ints and
 * floats each frame.
 *
 * <p>Colours are "#RRGGBB". Ratios are "R:G:B" multipliers, for example "1.0:0.5:1.0". A blank or
 * bad value gives the default.
 */
public final class ConfigColours {

    private ConfigColours() {}

    /** Parses value; if it is blank or bad, parses fallback (the option's default). */
    public static int parseRGB(String value, String fallback) {
        int rgb = parseRGB(value);
        return rgb >= 0 ? rgb : parseRGB(fallback);
    }

    /** Parses value; if it is blank or bad, parses fallback (the option's default). */
    public static float[] parseRatio(String value, String fallback) {
        float[] ratio = parseRatio(value);
        return ratio != null ? ratio : parseRatio(fallback);
    }

    /** Returns the RGB value of "#RRGGBB" or "RRGGBB", or -1 if it is not valid. */
    private static int parseRGB(String value) {
        String hex = value.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (hex.length() != 6) {
            return -1;
        }
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Returns the three multipliers of "R:G:B", or null if it is not valid. */
    private static float[] parseRatio(String value) {
        String[] parts = value.trim().split(":");
        if (parts.length != 3) {
            return null;
        }
        try {
            return new float[] {
                Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2])
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Blends each channel from rgb (progress 0) to rgb * ratio (progress 1). Channels are clamped
     * to 0-255.
     */
    public static int blend(int rgb, float[] ratio, double progress) {
        int r = channel(rgb >> 16 & 0xFF, ratio[0], progress);
        int g = channel(rgb >> 8 & 0xFF, ratio[1], progress);
        int b = channel(rgb & 0xFF, ratio[2], progress);
        return r << 16 | g << 8 | b;
    }

    private static int channel(int value, float ratio, double progress) {
        int out = (int) (value * (1D + (ratio - 1D) * progress));
        return out < 0 ? 0 : out > 255 ? 255 : out;
    }
}
