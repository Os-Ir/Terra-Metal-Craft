package com.osir.tmc.container.slot;

import com.osir.tmc.api.capability.CapabilityList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHeat extends SlotItemHandler {
	public SlotHeat(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.hasCapability(CapabilityList.HEATABLE, null) && super.isItemValid(stack);
	}
}