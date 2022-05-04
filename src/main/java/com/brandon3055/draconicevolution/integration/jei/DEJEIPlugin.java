package com.brandon3055.draconicevolution.integration.jei;

import com.brandon3055.brandonscore.client.gui.modulargui.GuiElement;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.api.DraconicAPI;
import com.brandon3055.draconicevolution.client.gui.GuiDraconiumChest;
import com.brandon3055.draconicevolution.client.gui.modular.itemconfig.GuiConfigurableItem;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.inventory.ContainerDraconiumChest;
import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.util.ErrorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by brandon3055 on 24/07/2016.
 */
@JeiPlugin
public class DEJEIPlugin implements IModPlugin {

    public static IJeiHelpers jeiHelpers = null;
    public static IJeiRuntime jeiRuntime = null;

    @Nullable
    private FusionRecipeCategory fusionRecipeCategory;

    public DEJEIPlugin() {
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiScreenHandler(GuiConfigurableItem.class, (gui) -> null);
        registration.addGuiContainerHandler(GuiDraconiumChest.class, new IGuiContainerHandler<GuiDraconiumChest>() {
            @Override
            public Collection<IGuiClickableArea> getGuiClickableAreas(GuiDraconiumChest gui, double mouseX, double mouseY) {
                if (gui.colourDialog.isVisible()) return Collections.emptyList();
                GuiElement<?> craftIcon = gui.craftIcon;
                GuiElement<?> smeltArrow = gui.furnaceProgress;
                IGuiClickableArea craftingArea = IGuiClickableArea.createBasic(craftIcon.xPos() - gui.guiLeft(), craftIcon.yPos() - gui.guiTop(), craftIcon.xSize(), craftIcon.ySize(), VanillaRecipeCategoryUid.CRAFTING);
                IGuiClickableArea smeltingArea = IGuiClickableArea.createBasic(smeltArrow.xPos() - gui.guiLeft(), smeltArrow.yPos() - gui.guiTop(), smeltArrow.xSize(), smeltArrow.ySize(), VanillaRecipeCategoryUid.FURNACE);
                return Lists.newArrayList(craftingArea, smeltingArea);
            }
        });
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(fusionRecipeCategory = new FusionRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ErrorUtil.checkNotNull(fusionRecipeCategory, "fusionRecipeCategory");
        jeiHelpers = registration.getJeiHelpers();

        ClientWorld world = Minecraft.getInstance().level;
        registration.addRecipes(world.getRecipeManager().getAllRecipesFor(DraconicAPI.FUSION_RECIPE_TYPE), RecipeCategoryUids.FUSION_CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IRecipeTransferHandlerHelper transferHelper = registration.getTransferHelper();
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        registration.addRecipeTransferHandler(new FusionRecipeTransferHelper(stackHelper, transferHelper), RecipeCategoryUids.FUSION_CRAFTING);

        //Draconium chest recipe movers
        registration.addRecipeTransferHandler(new IRecipeTransferInfo<ContainerDraconiumChest>() {
            @Override
            public Class<ContainerDraconiumChest> getContainerClass() {
                return ContainerDraconiumChest.class;
            }

            @Override
            public ResourceLocation getRecipeCategoryUid() {
                return VanillaRecipeCategoryUid.CRAFTING;
            }

            @Override
            public boolean canHandle(ContainerDraconiumChest container) {
                return true;
            }

            @Override
            public List<Slot> getRecipeSlots(ContainerDraconiumChest container) {
                return container.craftInputSlots;
            }

            @Override
            public List<Slot> getInventorySlots(ContainerDraconiumChest container) {
                return Stream.of(container.mainSlots, container.playerSlots).flatMap(Collection::stream).collect(Collectors.toList());
            }
        });

        //TODO Look into adding a custom transfer helper that utilizes all of the furnace slots.
        registration.addRecipeTransferHandler(new IRecipeTransferInfo<ContainerDraconiumChest>() {
            @Override
            public Class<ContainerDraconiumChest> getContainerClass() {
                return ContainerDraconiumChest.class;
            }

            @Override
            public ResourceLocation getRecipeCategoryUid() {
                return VanillaRecipeCategoryUid.FURNACE;
            }

            @Override
            public boolean canHandle(ContainerDraconiumChest container) {
                return true;
            }

            @Override
            public List<Slot> getRecipeSlots(ContainerDraconiumChest container) {
                return container.furnaceInputSlots;
            }

            @Override
            public List<Slot> getInventorySlots(ContainerDraconiumChest container) {
                return Stream.of(container.mainSlots, container.playerSlots).flatMap(Collection::stream).collect(Collectors.toList());
            }
        });

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(DEContent.crafting_core), RecipeCategoryUids.FUSION_CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(DEContent.draconium_chest), VanillaRecipeCategoryUid.CRAFTING, VanillaRecipeCategoryUid.FURNACE);

//        if (DEContent.crafting_core.isBlockEnabled()){
//            registration.addRecipeCatalyst(new ItemStack(DEContent.crafting_core), RecipeCategoryUids.FUSION_CRAFTING);
//        }
//        if (DEContent.draconium_chest.isBlockEnabled()) {
//            registration.addRecipeCatalyst(new ItemStack(DEContent.draconium_chest), VanillaRecipeCategoryUid.CRAFTING);
//            registration.addRecipeCatalyst(new ItemStack(DEContent.draconium_chest), VanillaRecipeCategoryUid.FURNACE);
//        }
    }


    //    @Override
//    public void register(IModRegistry registry) {
//        LogHelper.dev("Register JEI");


//        registry.addRecipeClickArea(GuiFusionCraftingCore.class, 81, 45, 18, 22, RecipeCategoryUids.FUSION_CRAFTING);
//        registry.addRecipeClickArea(GuiDraconiumChest.class, 394, 216, 22, 15, VanillaRecipeCategoryUid.CRAFTING);
//        registry.addRecipeClickArea(GuiDraconiumChest.class, 140, 202, 15, 22, VanillaRecipeCategoryUid.SMELTING);

//        hideFeature(DEContent.chaos_crystal);
//        hideFeature(DEContent.placed_item);
//        hideFeature(DEContent.energy_core_structure);
//        hideFeature(DEContent.chaosShardAtmos);

//        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
//        recipeTransferRegistry.addRecipeTransferHandler(new IRecipeTransferInfo<ContainerDraconiumChest>() {
//            @Override
//            public Class<ContainerDraconiumChest> getContainerClass() {
//                return ContainerDraconiumChest.class;
//            }
//
//            @Override
//            public String getRecipeCategoryUid() {
//                return VanillaRecipeCategoryUid.CRAFTING;
//            }
//
//            @Override
//            public boolean canHandle(ContainerDraconiumChest container) {
//                return true;
//            }
//
//            @Override
//            public List<Slot> getRecipeSlots(ContainerDraconiumChest container) {
//                List<Slot> slots = new ArrayList<>();
//                for (Slot slot : container.inventorySlots) {
//                    if (slot.slotNumber >= 268 && slot.slotNumber <= 276) {
//                        slots.add(slot);
//                    }
//                }
//                return slots;
//            }
//
//            @Override
//            public List<Slot> getInventorySlots(ContainerDraconiumChest container) {
//                List<Slot> slots = new ArrayList<>();
//                for (Slot slot : container.inventorySlots) {
//                    if ((slot.slotNumber >= 0 && slot.slotNumber < 260) || slot.slotNumber > 275) {
//                        slots.add(slot);
//                    }
//                }
//                LogHelper.dev("Inventory Slots: " + slots);
//                return slots;
//            }
//        });
//
//        registry.addRecipes(RecipeManager.FUSION_REGISTRY.getRecipes());
//
//        Item borkedSpawner = Item.REGISTRY.getObject(new ResourceLocation("enderio:item_broken_spawner"));
//        if (borkedSpawner != null) {
//            List<EIOSpawnerRecipesWrapper> wrappers = new ArrayList<>();
//            wrappers.add(new EIOSpawnerRecipesWrapper(jeiHelpers.getGuiHelper(), DEFeatures.draconicCore, borkedSpawner));
//            wrappers.add(new EIOSpawnerRecipesWrapper(jeiHelpers.getGuiHelper(), DEFeatures.wyvernCore, borkedSpawner));
//            wrappers.add(new EIOSpawnerRecipesWrapper(jeiHelpers.getGuiHelper(), DEFeatures.awakenedCore, borkedSpawner));
//            wrappers.add(new EIOSpawnerRecipesWrapper(jeiHelpers.getGuiHelper(), DEFeatures.chaoticCore, borkedSpawner));
//            registry.addRecipes(wrappers);
//        }
//
//        iUpgradables.clear();
//        Iterator<ItemStack> i = registry.getIngredientRegistry().getIngredients(ItemStack.class).iterator();
//        while (i.hasNext()) {
//            ItemStack stack = i.next();
//            if (!stack.isEmpty() && stack.getItem() instanceof IUpgradableItem) {
//                if ((EnergyUtils.isEnergyItem(stack) || stack.getItem() instanceof WyvernArmor) && EnergyUtils.getEnergyStored(stack) == 0) {
//                    continue;
//                }
//
//                LogHelper.dev("Add Upgradable: " + stack);
//                iUpgradables.add(stack);
//            }
//        }
//    }

    private void hideFeature(Object feature) {
//        if (ModFeatureParser.isEnabled(feature)) {
//            if (feature instanceof Item) {
//                jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack((Item) feature));
//            }
//            else if (feature instanceof Block) {
//                jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack((Block) feature));
//            }
//        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime iJeiRuntime) {
        jeiRuntime = iJeiRuntime;
    }

    public static void reloadJEI() {
//        if (jeiHelpers != null) {
//            jeiHelpers.reload();
//        }
    }


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(DraconicEvolution.MODID, "jei_plugin");
    }
}
