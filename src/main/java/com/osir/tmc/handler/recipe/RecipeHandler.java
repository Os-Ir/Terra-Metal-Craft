package com.osir.tmc.handler.recipe;

public class RecipeHandler {
	public static void register() {
		AnvilRecipeHandler.register();
		HeatRecipeHandler.register();
		CraftingRecipeHandler.register();
	}
}