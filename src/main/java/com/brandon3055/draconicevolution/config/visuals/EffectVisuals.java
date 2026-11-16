package com.brandon3055.draconicevolution.config.visuals;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.config.ConfigColours;

/** Colours of the other effects in the mod. Client side only. */
@ModConfigContainer(modid = DraconicEvolution.MODID)
public final class EffectVisuals {

    public static final String CATEGORY = "Visuals.Effects";

    private static final String FUSION_ORBIT_COLOUR = "#00FFFF";
    private static final String FUSION_FINISH_COLOUR = "#FF0000";
    private static final String FUSION_SPARK_COLOUR = "#00FFFF";
    private static final String ENERGY_BOLT_COLOUR = "#59A6E6";
    private static final String INFUSER_CHARGE_COLOUR = "#FF3300";
    private static final String INFUSER_IDLE_COLOUR = "#00FFFF";
    private static final String CELESTIAL_WEATHER_COLOUR = "#1A1AFF";
    private static final String CELESTIAL_SUN_COLOUR = "#FFD12C";
    private static final String CELESTIAL_SUN_CORE_COLOUR = "#FFFFFF";
    private static final String CELESTIAL_ORBIT_COLOUR_A = "#E6801A";
    private static final String CELESTIAL_ORBIT_COLOUR_B = "#4D33FF";
    private static final String CELESTIAL_SPARK_COLOUR = "#00FFFF";
    private static final String CHAOS_BEAM_COLOUR = "#C80000";
    private static final String SHIELD_EMPTY_COLOUR = "#FF0000";
    private static final String SHIELD_FULL_COLOUR = "#0000FF";
    private static final String MINING_OUTLINE_COLOUR = "#00FFFF";

    @ModConfigProperty(
            category = CATEGORY,
            name = "fusionOrbitColour",
            comment = "Fusion crafting: ingredients orbiting the core while they charge.")
    public static String fusionOrbitColour = FUSION_ORBIT_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "fusionFinishColour",
            comment =
                    "Fusion crafting: the orbit colour blends to this as the ingredients move into the core.")
    public static String fusionFinishColour = FUSION_FINISH_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "fusionSparkColour",
            comment = "Fusion crafting: sparks from the orbiting ingredients.")
    public static String fusionSparkColour = FUSION_SPARK_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "energyBoltColour",
            comment = "Energy bolts (fusion crafting and the celestial manipulator).")
    public static String energyBoltColour = ENERGY_BOLT_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "infuserChargeColour",
            comment = "Energy infuser particles while it charges an item.")
    public static String infuserChargeColour = INFUSER_CHARGE_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "infuserIdleColour",
            comment = "Energy infuser particles when the item is fully charged.")
    public static String infuserIdleColour = INFUSER_IDLE_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "celestialWeatherColour",
            comment = "Celestial manipulator: weather effect.")
    public static String celestialWeatherColour = CELESTIAL_WEATHER_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "celestialSunColour",
            comment = "Celestial manipulator: sun effect, outer.")
    public static String celestialSunColour = CELESTIAL_SUN_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "celestialSunCoreColour",
            comment = "Celestial manipulator: sun effect, centre.")
    public static String celestialSunCoreColour = CELESTIAL_SUN_CORE_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "celestialOrbitColourA",
            comment = "Celestial manipulator: orbiting effects, first colour.")
    public static String celestialOrbitColourA = CELESTIAL_ORBIT_COLOUR_A;

    @ModConfigProperty(
            category = CATEGORY,
            name = "celestialOrbitColourB",
            comment = "Celestial manipulator: orbiting effects, second colour.")
    public static String celestialOrbitColourB = CELESTIAL_ORBIT_COLOUR_B;

    @ModConfigProperty(
            category = CATEGORY,
            name = "celestialSparkColour",
            comment = "Celestial manipulator: sparks.")
    public static String celestialSparkColour = CELESTIAL_SPARK_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "chaosBeamColour",
            comment = "Chaos crystal and guardian crystal beams.")
    public static String chaosBeamColour = CHAOS_BEAM_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "shieldEmptyColour",
            comment = "Armor shield hit effect when the shield is empty.")
    public static String shieldEmptyColour = SHIELD_EMPTY_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "shieldFullColour",
            comment =
                    "Armor shield hit effect when the shield is full. The colour blends between the two.")
    public static String shieldFullColour = SHIELD_FULL_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "miningOutlineColour",
            comment = "Outline of the blocks a mining tool will break in AOE mode.")
    public static String miningOutlineColour = MINING_OUTLINE_COLOUR;

    // Parsed values as {r, g, b} floats. Set in load(), read every render frame.
    public static float[] fusionOrbitRGB;
    public static float[] fusionFinishRGB;
    public static float[] fusionSparkRGB;
    public static float[] energyBoltRGB;
    public static float[] infuserChargeRGB;
    public static float[] infuserIdleRGB;
    public static float[] celestialWeatherRGB;
    public static float[] celestialSunRGB;
    public static float[] celestialSunCoreRGB;
    public static float[] celestialOrbitRGBA;
    public static float[] celestialOrbitRGBB;
    public static float[] celestialSparkRGB;
    public static float[] chaosBeamRGB;
    public static float[] shieldEmptyRGB;
    public static float[] shieldFullRGB;
    public static float[] miningOutlineRGB;

    private EffectVisuals() {}

    static void load() {
        fusionOrbitRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(fusionOrbitColour, FUSION_ORBIT_COLOUR));
        fusionFinishRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(fusionFinishColour, FUSION_FINISH_COLOUR));
        fusionSparkRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(fusionSparkColour, FUSION_SPARK_COLOUR));
        energyBoltRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(energyBoltColour, ENERGY_BOLT_COLOUR));
        infuserChargeRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(infuserChargeColour, INFUSER_CHARGE_COLOUR));
        infuserIdleRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(infuserIdleColour, INFUSER_IDLE_COLOUR));
        celestialWeatherRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(celestialWeatherColour, CELESTIAL_WEATHER_COLOUR));
        celestialSunRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(celestialSunColour, CELESTIAL_SUN_COLOUR));
        celestialSunCoreRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(celestialSunCoreColour, CELESTIAL_SUN_CORE_COLOUR));
        celestialOrbitRGBA =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(celestialOrbitColourA, CELESTIAL_ORBIT_COLOUR_A));
        celestialOrbitRGBB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(celestialOrbitColourB, CELESTIAL_ORBIT_COLOUR_B));
        celestialSparkRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(celestialSparkColour, CELESTIAL_SPARK_COLOUR));
        chaosBeamRGB =
                ConfigColours.toFloats(ConfigColours.parseRGB(chaosBeamColour, CHAOS_BEAM_COLOUR));
        shieldEmptyRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(shieldEmptyColour, SHIELD_EMPTY_COLOUR));
        shieldFullRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(shieldFullColour, SHIELD_FULL_COLOUR));
        miningOutlineRGB =
                ConfigColours.toFloats(
                        ConfigColours.parseRGB(miningOutlineColour, MINING_OUTLINE_COLOUR));
    }
}
