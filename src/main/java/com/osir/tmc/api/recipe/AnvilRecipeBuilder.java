package com.osir.tmc.api.recipe;

import com.google.common.collect.ImmutableMap;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.ValidationResult;

public class AnvilRecipeBuilder extends RecipeBuilder<AnvilRecipeBuilder> {
	private AnvilRecipeType type;

	public AnvilRecipeBuilder() {

	}

	public AnvilRecipeBuilder(AnvilRecipe recipe, RecipeMap<AnvilRecipeBuilder> recipeMap) {
		super(recipe, recipeMap);
		this.type = recipe.getType();
	}

	public AnvilRecipeBuilder(AnvilRecipeBuilder builder) {
		super(builder);
		this.type = builder.getType();
	}

	@Override
	public ValidationResult<Recipe> build() {
		return ValidationResult.newResult(EnumValidationResult.VALID, new AnvilRecipe(inputs, outputs, chancedOutputs,
				fluidInputs, fluidOutputs, ImmutableMap.of(), duration, EUt, hidden, this.type));
	}

	@Override
	public AnvilRecipeBuilder copy() {
		return new AnvilRecipeBuilder(this);
	}

	public AnvilRecipeType getType() {
		return this.type;
	}
}