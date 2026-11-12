package com.brandon3055.draconicevolution.config.visuals;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.config.ConfigColours;

/** Energy core, stabilizer and pylon colours and animation speeds. Client side only. */
@ModConfigContainer(modid = DraconicEvolution.MODID)
public final class EnergyCoreVisuals {

    public static final String CATEGORY = "Visuals.EnergyCore";

    private static final String CORE_COLOUR = "#FF4DB3";
    private static final String CORE_FULL_RATIO = "1.0:0.0:0.0";
    private static final String CORE_COLOUR_T8 = "#FF470D";
    private static final String CORE_FULL_RATIO_T8 = "1.0:1.0:1.0";
    private static final String SHELL_COLOUR = "#33FFFF";
    private static final String SHELL_COLOUR_T8 = "#F27300";
    private static final String PYLON_PARTICLE_COLOUR = "#00C8FF";
    private static final String PYLON_INDICATOR_COLOUR = "#9600FF";
    private static final String STABILIZER_PARTICLE_COLOUR = "#FFFFFF";

    @ModConfigProperty(
            category = CATEGORY,
            name = "coreColour",
            comment = "Inner core colour for tiers 1-7 when empty.")
    public static String coreColour = CORE_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "coreFullRatio",
            comment =
                    "Inner core colour for tiers 1-7 when full, as multipliers of coreColour.\n"
                            + "The colour blends from coreColour to this as the core fills.")
    public static String coreFullRatio = CORE_FULL_RATIO;

    @ModConfigProperty(
            category = CATEGORY,
            name = "coreColourT8",
            comment = "Inner core colour for tier 8 when empty.")
    public static String coreColourT8 = CORE_COLOUR_T8;

    @ModConfigProperty(
            category = CATEGORY,
            name = "coreFullRatioT8",
            comment = "Inner core colour for tier 8 when full, as multipliers of coreColourT8.")
    public static String coreFullRatioT8 = CORE_FULL_RATIO_T8;

    @ModConfigProperty(
            category = CATEGORY,
            name = "shellColour",
            comment = "Outer shell colour for tiers 1-7.")
    public static String shellColour = SHELL_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "shellColourT8",
            comment = "Outer shell colour for tier 8.")
    public static String shellColourT8 = SHELL_COLOUR_T8;

    @ModConfigProperty(
            category = CATEGORY,
            name = "tintBeams",
            comment =
                    "Tint the stabilizer beams: the beam centre takes the core colour and the beam"
                            + " edges take the shell colour.")
    public static boolean tintBeams = false;

    @ModConfigProperty(
            category = CATEGORY,
            name = "tintStabilizers",
            comment = "Tint the stabilizer spheres with the shell colour.")
    public static boolean tintStabilizers = false;

    @ModConfigProperty(
            category = CATEGORY,
            name = "pylonColour",
            comment =
                    "Energy pylon sphere colour, for example #0024FF. Blank keeps the stock"
                            + " texture.")
    public static String pylonColour = "";

    @ModConfigProperty(
            category = CATEGORY,
            name = "pylonParticleColour",
            comment = "Energy particles between an energy pylon and the core.")
    public static String pylonParticleColour = PYLON_PARTICLE_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "pylonIndicatorColour",
            comment = "Particle line that shows which core an energy pylon is linked to.")
    public static String pylonIndicatorColour = PYLON_INDICATOR_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "stabilizerParticleColour",
            comment = "Multiplies the colour of the particles around the core stabilizers.")
    public static String stabilizerParticleColour = STABILIZER_PARTICLE_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "coreRotationSpeed",
            comment = "Rotation speed of the core and its shell.")
    public static double coreRotationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "stabilizerSpeed",
            comment = "Animation speed of the stabilizer spheres and beams.")
    public static double stabilizerSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "pylonRotationSpeed",
            comment = "Rotation speed of the energy pylon sphere.")
    public static double pylonRotationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "pylonPulseSpeed",
            comment = "Pulse speed of the energy pylon sphere.")
    public static double pylonPulseSpeed = 1;

    // Parsed values. Set in load(), read every render frame.
    public static int coreRGB;
    public static int coreRGBT8;
    public static int shellRGB;
    public static int shellRGBT8;
    public static float[] coreRatio;
    public static float[] coreRatioT8;
    /** -1 means the stock texture, untinted. */
    public static int pylonRGB;

    public static int pylonParticleRGB;
    public static int pylonIndicatorRGB;
    public static float[] stabilizerParticleRGB;

    private EnergyCoreVisuals() {}

    static void load() {
        coreRGB = ConfigColours.parseRGB(coreColour, CORE_COLOUR);
        coreRatio = ConfigColours.parseRatio(coreFullRatio, CORE_FULL_RATIO);
        coreRGBT8 = ConfigColours.parseRGB(coreColourT8, CORE_COLOUR_T8);
        coreRatioT8 = ConfigColours.parseRatio(coreFullRatioT8, CORE_FULL_RATIO_T8);
        shellRGB = ConfigColours.parseRGB(shellColour, SHELL_COLOUR);
        shellRGBT8 = ConfigColours.parseRGB(shellColourT8, SHELL_COLOUR_T8);
        pylonRGB = pylonColour.trim().isEmpty() ? -1 : ConfigColours.parseRGB(pylonColour, "");
        pylonParticleRGB = ConfigColours.parseRGB(pylonParticleColour, PYLON_PARTICLE_COLOUR);
        pylonIndicatorRGB = ConfigColours.parseRGB(pylonIndicatorColour, PYLON_INDICATOR_COLOUR);
        stabilizerParticleRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(
                                stabilizerParticleColour, STABILIZER_PARTICLE_COLOUR));
    }
}
