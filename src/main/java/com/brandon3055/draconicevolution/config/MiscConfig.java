package com.brandon3055.draconicevolution.config;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.DraconicEvolution;

/** Config category "Misc". Miscellaneous options. */
@ModConfigContainer(modid = DraconicEvolution.MODID)
public final class MiscConfig {

    @ModConfigProperty(
            category = "Misc",
            name = "devLog",
            comment =
                    "This enables dev log output. I primarily use this for development purposes, so it won't be very useful to regular users.")
    public static boolean devLog = false;

    @ModConfigProperty(
            category = "Misc",
            name = "chaosGuardianLoading",
            comment =
                    "Set this to false to disable the Chaos Guardian's chunkloading ability.\nNote. The chaos guardian is ONLY loaded when a player is within a couple hundred blocks.\nThis setting is here to avoid issues where the guardian would fly out of the loaded chunks \nand freeze; especially an issue on servers with reduced render distance.")
    public static boolean chaosGuardianLoading = true;

    private MiscConfig() {}
}
