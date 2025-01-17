package com.brandon3055.draconicevolution.api.modules.data;

import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Map;

/**
 * Created by brandon3055 on 3/5/20.
 */
@Deprecated
public class ProjAccuracyData implements ModuleData<ProjAccuracyData> {
    private final float accuracy;

    public ProjAccuracyData(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getAccuracy() {
        return accuracy > 1 ? 1 : accuracy;
    }

    @Override
    public ProjAccuracyData combine(ProjAccuracyData other) {
        return new ProjAccuracyData(accuracy + other.accuracy);
    }

    @Override
    public void addInformation(Map<Component, Component> map, ModuleContext context, boolean stack) {
        map.put(new TranslatableComponent("module.draconicevolution.proj_accuracy.name"), new TranslatableComponent("module.draconicevolution.proj_accuracy.value", (int)(getAccuracy() * 100)));
    }
}
