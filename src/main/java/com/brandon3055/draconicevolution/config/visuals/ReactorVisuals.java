package com.brandon3055.draconicevolution.config.visuals;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.config.ConfigColours;

/** Reaction reactor colours and animation speeds. Client side only. */
@ModConfigContainer(modid = DraconicEvolution.MODID)
public final class ReactorVisuals {

    public static final String CATEGORY = "Visuals.Reactor";

    private static final String CORE_COLOUR = "#FF6626";
    private static final String SHIELD_TINT = "#FFFFFF";
    private static final String SHIELD_COLOUR_NO_SHADERS = "#0000FF";
    private static final String FIELD_BEAM_COLOUR = "#1A80FF";
    private static final String EXTRACT_BEAM_COLOUR = "#FF6626";
    private static final String INJECTOR_BEAM_COLOUR = "#FF1306";

    @ModConfigProperty(
            category = CATEGORY,
            name = "coreColour",
            comment =
                    "Core colour with shaders on. The shader keeps its brightness curve, so hot"
                            + " cores still go towards white.")
    public static String coreColour = CORE_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "shieldTint",
            comment =
                    "Multiplies the shield colour with shaders on. The shield colour also changes"
                            + " with the shield charge.")
    public static String shieldTint = SHIELD_TINT;

    @ModConfigProperty(
            category = CATEGORY,
            name = "shieldColourNoShaders",
            comment = "Shield colour with shaders off.")
    public static String shieldColourNoShaders = SHIELD_COLOUR_NO_SHADERS;

    @ModConfigProperty(
            category = CATEGORY,
            name = "fieldBeamColour",
            comment = "Outer beam from each stabilizer to the core (containment field).")
    public static String fieldBeamColour = FIELD_BEAM_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "extractBeamColour",
            comment = "Inner beam from the core to each stabilizer (energy extraction).")
    public static String extractBeamColour = EXTRACT_BEAM_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "injectorBeamColour",
            comment = "Beam from each energy injector to the core.")
    public static String injectorBeamColour = INJECTOR_BEAM_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "coreAnimationSpeed",
            comment = "Speed of the core surface animation (shaders on).")
    public static double coreAnimationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "shieldAnimationSpeed",
            comment = "Speed of the shield surface animation (shaders on).")
    public static double shieldAnimationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "beamAnimationSpeed",
            comment = "Speed of the beam animation.")
    public static double beamAnimationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "rotationSpeed",
            comment = "Rotation speed of the core and the shield.")
    public static double rotationSpeed = 1;

    // Parsed values. Set in load(), read every render frame.
    public static float[] coreTint;
    public static float[] shieldTintRGB;
    public static int shieldRGBNoShaders;
    public static int fieldBeamRGB;
    public static int extractBeamRGB;
    public static int injectorBeamRGB;
    public static float[] fieldBeamTint;
    public static float[] extractBeamTint;
    public static float[] injectorBeamTint;

    private ReactorVisuals() {}

    static void load() {
        coreTint = ConfigColours.toFloats(ConfigColours.parseRGB(coreColour, CORE_COLOUR));
        shieldTintRGB = ConfigColours.toFloats(ConfigColours.parseRGB(shieldTint, SHIELD_TINT));
        shieldRGBNoShaders =
                ConfigColours.parseRGB(shieldColourNoShaders, SHIELD_COLOUR_NO_SHADERS);
        fieldBeamRGB = ConfigColours.parseRGB(fieldBeamColour, FIELD_BEAM_COLOUR);
        extractBeamRGB = ConfigColours.parseRGB(extractBeamColour, EXTRACT_BEAM_COLOUR);
        injectorBeamRGB = ConfigColours.parseRGB(injectorBeamColour, INJECTOR_BEAM_COLOUR);
        fieldBeamTint = ConfigColours.toFloats(fieldBeamRGB);
        extractBeamTint = ConfigColours.toFloats(extractBeamRGB);
        injectorBeamTint = ConfigColours.toFloats(injectorBeamRGB);
    }
}
