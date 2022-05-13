package com.brandon3055.draconicevolution.lib;

import com.brandon3055.brandonscore.handlers.IProcess;
import com.brandon3055.brandonscore.lib.MultiBlockStorage;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCore;
import com.brandon3055.draconicevolution.world.EnergyCoreStructure;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by brandon3055 on 2/11/18.
 */
public class EnergyCoreBuilder implements IProcess {

    private final TileEnergyCore core;
    private final Player player;
    private boolean isDead = false;
    private Map<BlockPos, BlockState> workList = new HashMap<>();
    private LinkedList<BlockPos> workOrder = new LinkedList<>();
    private Level world;

    public EnergyCoreBuilder(TileEnergyCore core, Player player) {
        this.core = core;
        this.player = player;
        this.world = core.getLevel();
        buildWorkList();
    }

    private void buildWorkList() {
        EnergyCoreStructure structure = core.coreStructure;
        MultiBlockStorage storage = structure.getStorageForTier(core.tier.get());
        BlockPos start = core.getBlockPos().offset(structure.getCoreOffset(core.tier.get()));
        Map<BlockPos, Block> structureBlocks = new HashMap<>();
        storage.forEachBlock(start, (key1, value) -> {
            Map<String, Block> blockCache = new HashMap<>();
            structureBlocks.put(key1, blockCache.computeIfAbsent(value, s -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(value))));
        });

        Level world = core.getLevel();
        for (BlockPos key : structureBlocks.keySet()) {
            Block targetBlock = structureBlocks.get(key);
            if (targetBlock == null) continue;
            if (world.isEmptyBlock(key)) {
                workList.put(key, targetBlock.defaultBlockState());
                continue;
            }
            BlockState state = world.getBlockState(key);
            if (state.getBlock() != targetBlock) {
                isDead = true;
                player.sendMessage(new TranslatableComponent("ecore.de.assemble_found_invalid.txt", state.getBlock().getDescriptionId(), key.toString()).withStyle(ChatFormatting.RED), Util.NIL_UUID);
                return;
            }
        }

        workOrder.addAll(workList.keySet());
        workOrder.sort(Comparator.comparingInt(value -> (int) value.distSqr(core.getBlockPos())));
    }

    @Override
    public void updateProcess() {
        if (workOrder.isEmpty() || !player.isAlive()) {
            isDead = true;
            return;
        }

        BlockPos pos = workOrder.poll();
        BlockState state = workList.get(pos);
        if (!world.isEmptyBlock(pos)) {
            if (world.getBlockState(pos).getBlock() == state.getBlock()) {
                return;
            }
            player.sendMessage(new TranslatableComponent("ecore.de.assemble_error_expected_air.txt", pos.toString()).withStyle(ChatFormatting.RED), Util.NIL_UUID);
            isDead = true;
            return;
        }

        ItemStack required = new ItemStack(state.getBlock());
        if (player.getAbilities().instabuild || extractItem(required)) {
            world.setBlockAndUpdate(pos, state);
            SoundType soundtype = state.getBlock().getSoundType(state, world, pos, player);
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        }
        else {
            player.sendMessage(new TranslatableComponent("ecore.de.assemble_missing_required.txt", state.getBlock().getDescriptionId()).withStyle(ChatFormatting.RED), Util.NIL_UUID);
            isDead = true;
        }
    }

    private boolean extractItem(ItemStack toExtract) {
        LazyOptional<IItemHandler> opHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (opHandler.isPresent()) {
            IItemHandler handler = opHandler.orElseThrow(WTFException::new);
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack inSlot = handler.getStackInSlot(i);
                if (!inSlot.isEmpty() && inSlot.getItem() == toExtract.getItem()) {
                    ItemStack extracted = handler.extractItem(i, 1, false);
                    if (!extracted.isEmpty() && extracted.getItem() == toExtract.getItem()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    @Override
    public boolean isDead() {
        return isDead;
    }
}
