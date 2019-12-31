package com.osir.tmc.container.slot;

import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.util.ItemIndex;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHeat extends SlotItemHandler {
	public SlotHeat(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack != null
				&& ModRecipeMap.REGISTRY_MATERIAL.containsKey(new ItemIndex(stack, ModRecipeMap.VALI_STACK))
				&& super.isItemValid(stack);
	}
}