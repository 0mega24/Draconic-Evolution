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
     * Parses a list of colours, one per tier. A missing, blank or bad entry uses the default at the
     * same index. If allowBlank is true, a blank entry gives -1 instead (meaning "stock").
     */
    public static int[] parseRGBList(String[] values, String[] defaults, boolean allowBlank) {
        int[] out = new int[defaults.length];
        for (int i = 0; i < defaults.length; i++) {
            String value = i < values.length ? values[i] : "";
            if (allowBlank && value.trim().isEmpty()) {
                out[i] = -1;
            } else {
                out[i] = parseRGB(value, defaults[i]);
            }
        }
        return out;
    }

    /** Splits 0xRRGGBB into {r, g, b} floats in the range 0-1, for shader uniforms. */
    public static float[] toFloats(int rgb) {
        return new float[] {
            (rgb >> 16 & 0xFF) / 255F, (rgb >> 8 & 0xFF) / 255F, (rgb & 0xFF) / 255F
        };
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
