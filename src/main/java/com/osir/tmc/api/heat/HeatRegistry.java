package com.osir.tmc.api.heat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class HeatRegistry {
	private static final List<HeatRecipe> REGISTRY = new ArrayList<HeatRecipe>();

	public static void addRecipe(HeatRecipe recipe) {
		if (hasRecipe(recipe.getInput())) {
			return;
		}
		REGISTRY.add(recipe);
	}

	public static boolean hasRecipe(ItemStack stack) {
		return findRecipe(stack) != null;
	}

	public static void deleteRecipe(ItemStack stack) {
		HeatRecipe recipe;
		for (int i = 0; i < REGISTRY.size(); i++) {
			recipe = REGISTRY.get(i);
			if (recipe.match(stack)) {
				REGISTRY.remove(i);
				return;
			}
		}
	}

	public static HeatRecipe findRecipe(ItemStack stack) {
		HeatRecipe recipe;
		for (int i = 0; i < REGISTRY.size(); i++) {
			recipe = REGISTRY.get(i);
			if (recipe.match(stack)) {
				return recipe;
			}
		}
		return null;
	}
}