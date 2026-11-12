package com.brandon3055.draconicevolution.config.visuals;

/**
 * Client side colour and animation options, one class per feature in this package.
 *
 * <p>Colours are "#RRGGBB". Ratios are "R:G:B" multipliers. Speeds are multipliers, 1.0 is the
 * default speed. To add a feature: add a class like {@link EnergyCoreVisuals}, then call its load()
 * from {@link #load()} and add its category comment to {@link #COMMENTS}.
 */
public final class Visuals {

    /** Category comments, keyed by category name. */
    public static final String[][] COMMENTS = {
        {
            EnergyCoreVisuals.CATEGORY,
            "Energy core, stabilizer and pylon colours and animation speeds. Client side only."
                    + " Colours are #RRGGBB, ratios are R:G:B multipliers, speeds are multipliers."
        },
        {
            ReactorVisuals.CATEGORY,
            "Reaction reactor colours and animation speeds. Client side only. Colours are"
                    + " #RRGGBB, speeds are multipliers (1.0 is the default speed)."
        },
        {
            EnergyCrystalVisuals.CATEGORY,
            "Energy crystal and energy network colours and animation speeds. Client side only."
                    + " Lists have one entry per tier: basic, wyvern, draconic."
        },
    };

    private Visuals() {}

    /** Parses all visuals options. Called on config load and on config change. */
    public static void load() {
        EnergyCoreVisuals.load();
        ReactorVisuals.load();
        EnergyCrystalVisuals.load();
    }
}
