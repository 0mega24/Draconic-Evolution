package com.brandon3055.draconicevolution.inventory;

import com.brandon3055.draconicevolution.blocks.tileentity.TileDraconiumChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

/**
 * Created by Brandon on 29/10/2014.
 */
public class InventoryCraftingChest extends InventoryCrafting {
    /**
     * the width of the crafting inventory
     */
    private int inventoryWidth;

    /**
     * Class containing the callbacks for the events on_GUIClosed and
     * on_CraftMaxtrixChanged.
     */
    private Container eventHandler;
    private TileDraconiumChest tile;

    public InventoryCraftingChest(Container par1Container, int size, int height, TileDraconiumChest tile) {
        super(par1Container, size, height);
        this.eventHandler = par1Container;
        this.inventoryWidth = size;
        this.tile = tile;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        // the 9 slots + 1 output slot that's not accessible, we therefore have to add 1 to the slot accessed
        return slot >= getSizeInventory() ? ItemStack.EMPTY : tile.getStackInCraftingSlot(slot + 1);
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < inventoryWidth) {
            int k = row + column * inventoryWidth;
            return getStackInSlot(k);
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    public String getInvName() {
        return "";
    }

    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int par1) {
        return ItemStack.EMPTY;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int slotID, int par2) {
        ItemStack stack = tile.getStackInCraftingSlot(slotID + 1);
        if (!stack.isEmpty()) {
            ItemStack itemstack;

            if (stack.getCount() <= par2) {
                itemstack = stack.copy();
                stack = ItemStack.EMPTY;
                tile.setInventoryCraftingSlotContents(slotID + 1, ItemStack.EMPTY);
                eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
            else {
                itemstack = stack.splitStack(par2);

                if (stack.getCount() == 0) {
                    stack = ItemStack.EMPTY;
                }

                eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        tile.setInventoryCraftingSlotContents(slot + 1, itemstack);
        eventHandler.onCraftMatrixChanged(this);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    @Override
    public void markDirty() {
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    @Override
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer) {
        return true;
    }

    public void openChest() {
    }

    public void closeChest() {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
        return true;
    }
}
