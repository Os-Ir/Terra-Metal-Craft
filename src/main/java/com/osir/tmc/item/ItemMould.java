package com.osir.tmc.item;

import com.osir.tmc.handler.BlockHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ItemMould extends ItemBlock {
	public ItemMould() {
		super(BlockHandler.MOULD);
		this.setRegistryName("mould");
	}

	@Override
	public ActionResult onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack mould = player.getHeldItem(hand);
		if (mould.getCount() != 1) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		}
		if (mould.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandlerModifiable cap = (IItemHandlerModifiable) mould
					.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			ItemStack stack = cap.getStackInSlot(0);
			if (stack == null || stack.isEmpty()) {
				int idx = player.inventory.currentItem == 8 ? 0 : player.inventory.currentItem + 1;
				ItemStack next = player.inventory.getStackInSlot(idx);
				if (next == null || next.isEmpty()) {
					stack = next = ItemStack.EMPTY;
				} else {
					stack = next.copy();
					next = ItemStack.EMPTY;
				}
				player.inventory.setInventorySlotContents(idx, ItemStack.EMPTY);
			} else {
				player.addItemStackToInventory(stack);
			}
			if (stack == null || stack.isEmpty()) {
				cap.setStackInSlot(0, ItemStack.EMPTY);
			} else {
				cap.setStackInSlot(0, stack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}