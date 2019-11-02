package com.osir.tmc.handler;

import com.osir.tmc.api.anvil.AnvilRecipe;
import com.osir.tmc.api.anvil.AnvilRecipeType;
import com.osir.tmc.api.anvil.AnvilRegistry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AnvilRecipeHandler {
	public static void setup() {
		AnvilRegistry.addRecipe(new AnvilRecipe(AnvilRecipeType.WELD, new ItemStack(Items.IRON_INGOT), ItemStack.EMPTY,
				3, 0, new ItemStack(Items.IRON_PICKAXE), ItemStack.EMPTY));
		AnvilRegistry.addRecipe(new AnvilRecipe(AnvilRecipeType.TWINE, new ItemStack(Items.IRON_INGOT), ItemStack.EMPTY,
				1, 0, new ItemStack(Items.IRON_SHOVEL), ItemStack.EMPTY));
		AnvilRegistry.addRecipe(new AnvilRecipe(AnvilRecipeType.BEND, new ItemStack(Items.IRON_INGOT), ItemStack.EMPTY,
				1, 0, new ItemStack(Items.IRON_AXE), ItemStack.EMPTY));
	}
}