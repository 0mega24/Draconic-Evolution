package com.brandon3055.draconicevolution.config;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.DraconicEvolution;

/** Config category "Client Settings". Client side options. They have no effect on a server. */
@ModConfigContainer(modid = DraconicEvolution.MODID)
public final class ClientConfig {

    @ModConfigProperty(
            category = "Client Settings",
            name = "hudSettings",
            comment =
                    "This is where the settings for the in game hud are stored. You should not need to adjust these unless something breaks.\nWarning: Changing the number of entries in this list will crash your game.")
    public static int[] hudSettings =
            new int[] {
                996, 825, 69, 907, 90, 100, 3, 0, 1, 1, 1, 1
            }; // x, y, x, y, scale, scale, fademode, fademode, rotateArmor, armorText, hudEnabled,
    // shieldEnabled

    @ModConfigProperty(
            category = "Client Settings",
            name = "disable3DModels",
            comment =
                    "Disables the 3D tool and armor models. This is required if you want to use a 2D resource pack.")
    public static boolean disable3DModels = false;

    @ModConfigProperty(
            category = "Client Settings",
            name = "invertDPDSB",
            comment = "Invert Dislocator Pedestal display name shift behavior.")
    public static boolean invertDPDSB = false;

    @ModConfigProperty(
            category = "Client Settings",
            name = "useShaders",
            comment =
                    "Set this to false if your system can not handle the awesomeness that is shaders! (Warning: Will make cool things look horrible!)")
    public static boolean useShaders = true;

    @ModConfigProperty(
            category = "Client Settings",
            name = "useCrystalShaders",
            comment =
                    "This allows you to disable just the Energy Crystal shader. This shader can be a lot lagier than the reactor's shader since there are usually a lot more of them (The fallback crystal texture is not soooo bad...).")
    public static boolean useCrystalShaders = true;

    @ModConfigProperty(
            category = "Client Settings",
            name = "useReactorBeamShaders",
            comment = "Set this to false if you prefer the original look of the reactor beams.")
    public static boolean useReactorBeamShaders = true;

    @ModConfigProperty(
            category = "Client Settings",
            name = "disableLoudCelestialManipulator",
            comment =
                    "If true, the range of the Celestial Manipulator's sound effect will be significantly reduced.")
    public static boolean disableLoudCelestialManipulator = false;

    @ModConfigProperty(
            category = "Client Settings",
            name = "disableDislocatorSound",
            comment =
                    "If true, the item dislocator will not make the pickup sound when it collects items.")
    public static boolean disableDislocatorSound = false;

    @ModConfigProperty(
            category = "Client Settings",
            name = "disableShieldHitEffect",
            comment =
                    "If true, the armor shield will not render when you take damage (this is only a visual change).")
    public static boolean disableShieldHitEffect = false;

    @ModConfigProperty(
            category = "Client Settings",
            name = "disableShieldHitSound",
            comment = "If true, the armor shield hit sound will be disabled.")
    public static boolean disableShieldHitSound = false;

    @ModConfigProperty(
            category = "Client Settings",
            name = "disableCustomArrowModel",
            comment =
                    "If true, the custom arrow models used by DE will be replaced by the vanilla model.",
            requiresMCRestart = true)
    public static boolean disableCustomArrowModel = false;

    @ModConfigProperty(
            category = "Client Settings",
            name = "disableArmorFOVCorrection",
            comment = "This option is mainly here in case there are conflicts with other mods.")
    public static boolean disableArmorFOVCorrection = false;

    private ClientConfig() {}
}
