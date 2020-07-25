package com.osir.tmc.container.slot;

import com.osir.tmc.api.capability.ModCapabilities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHeat extends SlotItemHandler {
	public SlotHeat(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.hasCapability(ModCapabilities.HEATABLE, null) && super.isItemValid(stack);
	}
}