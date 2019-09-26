package com.osir.tmc.handler;

import com.osir.tmc.api.heat.HeatRecipe;
import com.osir.tmc.api.heat.HeatRegistry;
import com.osir.tmc.api.heat.MaterialList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HeatableItemHandler {
	public static void setup() {
		HeatRegistry.addRecipe(new HeatRecipe(144, new ItemStack(Items.IRON_INGOT), MaterialList.IRON));
		HeatRegistry.addRecipe(new HeatRecipe(144, new ItemStack(Items.GOLD_INGOT), MaterialList.GOLD));
		HeatRegistry.addRecipe(
				new HeatRecipe(144, new ItemStack(Blocks.SAND), new ItemStack(Blocks.GLASS), MaterialList.SAND));
	}
}