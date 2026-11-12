package com.brandon3055.draconicevolution.config.visuals;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.config.ConfigColours;

/**
 * Energy crystal and energy network colours and animation speeds. Client side only. List options
 * have one entry per tier: basic, wyvern, draconic.
 */
@ModConfigContainer(modid = DraconicEvolution.MODID)
public final class EnergyCrystalVisuals {

    public static final String CATEGORY = "Visuals.EnergyCrystals";

    private static final String[] CRYSTAL_COLOURS = {"#00334D", "#780094", "#FF661A"};
    private static final String[] CRYSTAL_COLOURS_NO_SHADERS = {"#0059A6", "#8C4DE6", "#FF922C"};
    private static final String[] PARTICLE_COLOURS = {"#00CCFF", "#CC1AFF", "#FFB333"};
    private static final String[] NO_DEFAULTS = {"", "", ""};
    private static final String RELAY_RING_COLOUR = "#00FFFF";
    private static final String WIRELESS_RING_COLOUR = "#FF0000";
    private static final String WIRELESS_TRANSFER_COLOUR = "#00FFFF";
    private static final String LINK_COLOUR = "#FF0000";

    @ModConfigProperty(
            category = CATEGORY,
            name = "crystalColours",
            comment =
                    "Crystal colour per tier with shaders on. The shader keeps its brightness"
                            + " curve.")
    public static String[] crystalColours = CRYSTAL_COLOURS.clone();

    @ModConfigProperty(
            category = CATEGORY,
            name = "crystalColoursNoShaders",
            comment = "Crystal colour per tier with shaders off (blocks and items).")
    public static String[] crystalColoursNoShaders = CRYSTAL_COLOURS_NO_SHADERS.clone();

    @ModConfigProperty(
            category = CATEGORY,
            name = "beamColours",
            comment =
                    "Energy beam colour per tier, for example #00FFFF. Blank keeps the stock"
                            + " texture.")
    public static String[] beamColours = NO_DEFAULTS.clone();

    @ModConfigProperty(
            category = CATEGORY,
            name = "particleColours",
            comment = "I/O crystal glow and crystal ring particles, per tier.")
    public static String[] particleColours = PARTICLE_COLOURS.clone();

    @ModConfigProperty(
            category = CATEGORY,
            name = "relayRingColour",
            comment = "Inner particle ring of relay crystals.")
    public static String relayRingColour = RELAY_RING_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "wirelessRingColour",
            comment = "Inner particle ring of wireless crystals.")
    public static String wirelessRingColour = WIRELESS_RING_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "wirelessTransferColour",
            comment = "Particles that carry energy from a wireless crystal to its receivers.")
    public static String wirelessTransferColour = WIRELESS_TRANSFER_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "linkColour",
            comment = "Lines to linked receivers, shown while you hold a wrench.")
    public static String linkColour = LINK_COLOUR;

    @ModConfigProperty(
            category = CATEGORY,
            name = "rotationSpeed",
            comment = "Rotation speed of the crystals.")
    public static double rotationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "crystalAnimationSpeed",
            comment = "Speed of the crystal surface animation (shaders on).")
    public static double crystalAnimationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "beamAnimationSpeed",
            comment = "Scroll speed of the energy beams and receiver link lines.")
    public static double beamAnimationSpeed = 1;

    @ModConfigProperty(
            category = CATEGORY,
            name = "ringSpeed",
            comment = "Orbit speed of the crystal ring particles.")
    public static double ringSpeed = 1;

    // Parsed values. Set in load(), read every render frame. Lists are indexed by tier.
    public static float[][] crystalTints;
    public static int[] crystalRGBNoShaders;
    /** -1 means the stock texture, untinted. */
    public static int[] beamRGB;

    public static int[] particleRGB;
    public static int relayRingRGB;
    public static int wirelessRingRGB;
    public static int wirelessTransferRGB;
    public static int linkRGB;

    private EnergyCrystalVisuals() {}

    static void load() {
        int[] crystal = ConfigColours.parseRGBList(crystalColours, CRYSTAL_COLOURS, false);
        crystalTints = new float[crystal.length][];
        for (int i = 0; i < crystal.length; i++) {
            crystalTints[i] = ConfigColours.toFloats(crystal[i]);
        }
        crystalRGBNoShaders =
                ConfigColours.parseRGBList(
                        crystalColoursNoShaders, CRYSTAL_COLOURS_NO_SHADERS, false);
        beamRGB = ConfigColours.parseRGBList(beamColours, NO_DEFAULTS, true);
        particleRGB = ConfigColours.parseRGBList(particleColours, PARTICLE_COLOURS, false);
        relayRingRGB = ConfigColours.parseRGB(relayRingColour, RELAY_RING_COLOUR);
        wirelessRingRGB = ConfigColours.parseRGB(wirelessRingColour, WIRELESS_RING_COLOUR);
        wirelessTransferRGB =
                ConfigColours.parseRGB(wirelessTransferColour, WIRELESS_TRANSFER_COLOUR);
        linkRGB = ConfigColours.parseRGB(linkColour, LINK_COLOUR);
    }
}
