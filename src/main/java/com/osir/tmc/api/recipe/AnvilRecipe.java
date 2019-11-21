package com.osir.tmc.api.recipe;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.osir.tmc.api.util.ComparatorAnvilRecipe;

import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AnvilRecipe extends Recipe {
	protected static final Comparator COMPARATOR = new ComparatorAnvilRecipe();
	public static final RecipeMap<AnvilRecipeBuilder> REGISTRY = new RecipeMap<AnvilRecipeBuilder>("anvil", 1, 2, 1, 4,
			0, 0, 0, 0, new AnvilRecipeBuilder());

	private AnvilRecipeType type;

	public AnvilRecipe(List<CountableIngredient> inputs, List<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
			List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs, Map<String, Object> recipeProperties,
			int duration, int EUt, boolean hidden, AnvilRecipeType type) {
		super(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs, recipeProperties, duration, EUt, hidden);
		this.type = type;
	}

	public AnvilRecipeType getType() {
		return this.type;
	}
}