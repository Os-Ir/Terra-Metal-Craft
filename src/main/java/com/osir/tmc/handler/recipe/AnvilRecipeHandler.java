package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.recipe.AnvilRecipe;

import gregtech.api.recipes.RecipeBuilder;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AnvilRecipeHandler {
	private static final RecipeBuilder BUILDER = AnvilRecipe.REGISTRY.recipeBuilder();

	public static void register() {
		BUILDER.EUt(1);
		BUILDER.duration(1);

		BUILDER.input("ingotIron", 3);
		BUILDER.outputs(new ItemStack(Items.IRON_AXE));
		BUILDER.buildAndRegister();
	}
}