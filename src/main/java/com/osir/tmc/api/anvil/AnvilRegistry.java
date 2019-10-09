package com.osir.tmc.api.anvil;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class AnvilRegistry {
	private static final List<AnvilRecipe> REGISTRY = new ArrayList<AnvilRecipe>();

	public static void addRecipe(AnvilRecipe recipe) {
		if (hasRecipe(recipe.getInput())) {
			throw new IllegalArgumentException("This anvil recipe has been registered");
		}
		REGISTRY.add(recipe);
	}

	public static boolean hasRecipe(List<ItemStack> stack) {
		return findRecipe(stack) != null;
	}

	public static void deleteRecipe(List<ItemStack> stack) {
		int i;
		AnvilRecipe recipe;
		for (i = 0; i < REGISTRY.size(); i++) {
			recipe = REGISTRY.get(i);
			if (recipe.match(stack)) {
				REGISTRY.remove(i);
				return;
			}
		}
	}

	public static AnvilRecipe findRecipe(List<ItemStack> stack) {
		int i;
		AnvilRecipe recipe;
		for (i = 0; i < REGISTRY.size(); i++) {
			recipe = REGISTRY.get(i);
			if (recipe.match(stack)) {
				return recipe;
			}
		}
		return null;
	}
}