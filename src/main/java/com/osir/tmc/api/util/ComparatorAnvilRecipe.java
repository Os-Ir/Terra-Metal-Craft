package com.osir.tmc.api.util;

import java.util.Comparator;

import net.minecraft.item.ItemStack;

public class ComparatorAnvilRecipe implements Comparator<ItemStack> {
	@Override
	public int compare(ItemStack stackA, ItemStack stackB) {
		int dlt = stackA.getItem().getRegistryName().compareTo(stackB.getItem().getRegistryName());
		if (dlt != 0) {
			return dlt;
		}
		return stackA.getItemDamage() - stackB.getItemDamage();
	}
}