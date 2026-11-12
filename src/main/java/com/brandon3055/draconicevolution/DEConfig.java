package com.brandon3055.draconicevolution;

import com.brandon3055.brandonscore.handlers.FileHandler;
import com.brandon3055.brandonscore.registry.IModConfigHelper;
import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.draconicevolution.config.TweaksConfig;
import com.brandon3055.draconicevolution.config.visuals.Visuals;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by brandon3055 on 24/3/2016. The config helper for Draconic Evolution: the config file,
 * the category comments and the load hooks. The options are in the {@code config} package.
 */
@ModConfigContainer(modid = DraconicEvolution.MODID)
public class DEConfig implements IModConfigHelper {

    public static Map<String, String> comments = new HashMap<String, String>();

    static {
        comments.put(
                "items",
                "Allows you to disable any item in the mod. Note that disabling an item will automatically\ndisable its recipe and all recipes that use it. (Requires game restart)\nTo disable an item set its value to false");
        comments.put(
                "blocks",
                "Allows you to disable any block in the mod. Note that disabling an block will automatically\ndisable its recipe and all recipes that use it. (Requires game restart)\nTo disable a block set its value to false");
        comments.put("World", "This category contains config properties related to world gen.");
        comments.put("Tweaks", "Just what the name says. Tweaks. Allows you to tweak stuff.");
        comments.put(
                "Client Settings",
                "These are client side properties that have no effect server side.");
        comments.put(
                "Stat Tweaks",
                "These allow you to tweak the stats of the tools, weapons and armor.");
        comments.put("Misc", "Just some misc settings.");
        for (String[] comment : Visuals.COMMENTS) {
            comments.put(comment[0], comment[1]);
        }
    }

    @Override
    public Configuration createConfiguration(FMLPreInitializationEvent event) {
        return new Configuration(
                new File(FileHandler.brandon3055Folder, "DraconicEvolution.cfg"), true);
    }

    @Override
    public String getCategoryComment(String category) {
        return comments.getOrDefault(category, "");
    }

    @Override
    public void onConfigChanged(String propertyName, String propertyCategory) {
        loadToolStats();
        Visuals.load();
    }

    @Override
    public void onConfigLoaded() {
        TweaksConfig.load();
        loadToolStats();
        Visuals.load();
    }

    private void loadToolStats() {
        DEFeatures.draconicAxe.loadStatConfig();
        DEFeatures.draconicBow.loadStatConfig();
        DEFeatures.draconicHoe.loadStatConfig();
        DEFeatures.draconicPick.loadStatConfig();
        DEFeatures.draconicShovel.loadStatConfig();
        DEFeatures.draconicStaffOfPower.loadStatConfig();
        DEFeatures.draconicSword.loadStatConfig();
        DEFeatures.wyvernAxe.loadStatConfig();
        DEFeatures.wyvernBow.loadStatConfig();
        DEFeatures.wyvernPick.loadStatConfig();
        DEFeatures.wyvernShovel.loadStatConfig();
        DEFeatures.wyvernSword.loadStatConfig();
    }
}

// todo energy core storage values and ability to disable tier 8
// todo tool and armor stats and energy
// todo
// todo
