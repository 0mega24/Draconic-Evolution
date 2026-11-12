package com.brandon3055.draconicevolution.utils;

/**
 * Parses the colour config options once, at config load, so that renderers only read ints and
 * floats each frame.
 *
 * <p>Colours are "#RRGGBB". Ratios are "R:G:B" multipliers, for example "1.0:0.5:1.0". A blank or
 * bad value gives the fallback.
 */
public final class ConfigColours {

    private ConfigColours() {}

    public static int parseRGB(String value, int fallback) {
        String hex = value.trim();
        if (hex.startsWith("#")) hex = hex.substring(1);
        try {
            return hex.length() == 6 ? Integer.parseInt(hex, 16) : fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static float[] parseRatio(String value, float[] fallback) {
        String[] parts = value.trim().split(":");
        if (parts.length != 3) return fallback;
        try {
            return new float[] {
                Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2])
            };
        } catch (NumberFormatException e) {
            return fallback;
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
