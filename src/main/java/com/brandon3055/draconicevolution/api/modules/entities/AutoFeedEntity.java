package com.brandon3055.draconicevolution.api.modules.entities;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.client.BCSprites;
import com.brandon3055.brandonscore.client.utils.GuiHelperOld;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.AutoFeedData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class AutoFeedEntity extends ModuleEntity {

    private BooleanProperty consumeFood;
    private float storedFood = 0;

    public AutoFeedEntity(Module<AutoFeedData> module) {
        super(module);
        addProperty(consumeFood = new BooleanProperty("feed_mod.consume_food", true).setFormatter(ConfigProperty.BooleanFormatter.YES_NO));
        this.savePropertiesToItem = true;
    }

    @Override
    public void tick(ModuleContext context) {
        AutoFeedData data = (AutoFeedData) module.getData();
        if (context instanceof StackModuleContext) {
            LivingEntity entity = ((StackModuleContext) context).getEntity();
            if (entity instanceof ServerPlayer && entity.tickCount % 10 == 0 && ((StackModuleContext) context).isEquipped()) {
                ServerPlayer player = (ServerPlayer) entity;
                if (storedFood < data.getFoodStorage() && consumeFood.getValue()) {
                    //Do food consumption
                    for (ItemStack stack : player.inventory.items) {
                        if (!stack.isEmpty() && stack.isEdible()) {
                            FoodProperties food = stack.getItem().getFoodProperties();
                            if (food != null && food.getNutrition() > 0 && food.getEffects().isEmpty()) {
                                double val = food.getNutrition() + food.getSaturationModifier();
                                double rem = storedFood + val - data.getFoodStorage();
                                if (rem <= val * 0.25) {
                                    storedFood = (float) Math.min(storedFood + val, data.getFoodStorage());
                                    entity.level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.25F, (0.95F + (entity.level.random.nextFloat() * 0.1F)));
                                    stack.shrink(1);
                                    break;
                                }
                            }
                        }
                    }
                }
                FoodData foodStats = player.getFoodData();
                if (storedFood > 0 && (foodStats.getFoodLevel() < 20 || foodStats.getSaturationLevel() < 20)) {
                    //Feed player
                    TechLevel tech = module.getModuleTechLevel();
                    double maxSat = entity.tickCount % 20 == 0 && tech == TechLevel.DRACONIC ? 20 : 1;//tech == TechLevel.DRACONIUM ? 1 : tech == TechLevel.WYVERN ? 2 : 4; //Problem is i'm not sure if i want this to essentially be a "Regeneration module"
                    if (foodStats.needsFood() && storedFood > 1) {
                        foodStats.eat((int)consumeFood(Math.min(1, 20 - foodStats.getFoodLevel())), 0);
                    }else if (foodStats.getSaturationLevel() < maxSat && storedFood > 0) {
                        foodStats.saturationLevel += consumeFood(Math.min(1, maxSat - foodStats.getSaturationLevel()));
                    }
                }
            }
        }
    }

    private double consumeFood(double amount) {
        amount = Math.min(amount, storedFood);
        storedFood -= amount;
        return amount;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderSlotOverlay(MultiBufferSource getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        VertexConsumer builder = getter.getBuffer(BCSprites.GUI_TYPE);
        AutoFeedData data = (AutoFeedData) module.getData();
        double progress = storedFood / data.getFoodStorage();
        progress = (int) (progress * 21F);
        progress = (20 - progress) - 1;
        for (int i = 0; i < 10; i++){
            float size = (width - 3) / 10F;
            GuiHelperOld.drawSprite(builder, x + 1 + i * size, y + height - size - 2, size + 1, size + 1, BCSprites.get("bars/food_empty").sprite(), 0);
            if (progress / 2F <= i){
                if (progress / 2F < i){
                    GuiHelperOld.drawSprite(builder, x + 1 + i * size, y + height - size - 2, size + 1, size + 1, BCSprites.get("bars/food_full").sprite(), 0);
                } else {
                    GuiHelperOld.drawSprite(builder, x + 1 + i * size, y + height - size - 2, size + 1, size + 1, BCSprites.get("bars/food_half").sprite(), 0);
                }
            }
        }
    }

    @Override
    public void addToolTip(List<Component> list) {
        list.add(new TranslatableComponent("module.draconicevolution.auto_feed.stored").withStyle(ChatFormatting.GRAY).append(" ").append(new TranslatableComponent("module.draconicevolution.auto_feed.stored.value", (int)storedFood).withStyle(ChatFormatting.DARK_GREEN)));
    }

    @Override
    public void writeToItemStack(ItemStack stack, ModuleContext context) {
        super.writeToItemStack(stack, context);
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putFloat("food", storedFood);
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getOrCreateTag();
            storedFood = nbt.getFloat("food");
        }
    }

    @Override
    public void writeToNBT(CompoundTag compound) {
        super.writeToNBT(compound);
        compound.putFloat("food", storedFood);
    }

    @Override
    public void readFromNBT(CompoundTag compound) {
        super.readFromNBT(compound);
        storedFood = compound.getFloat("food");
    }
}
