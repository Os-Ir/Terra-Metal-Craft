package com.osir.tmc.handler;

import com.osir.tmc.api.anvil.AnvilRecipe;
import com.osir.tmc.api.anvil.AnvilRecipeType;
import com.osir.tmc.api.anvil.AnvilRegistry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AnvilRecipeHandler {
	public static void setup() {
		AnvilRegistry.addRecipe(new AnvilRecipe(AnvilRecipeType.WORK, new ItemStack(Items.IRON_INGOT), ItemStack.EMPTY,
				3, 0, new ItemStack(Items.IRON_PICKAXE), ItemStack.EMPTY));
	}
}