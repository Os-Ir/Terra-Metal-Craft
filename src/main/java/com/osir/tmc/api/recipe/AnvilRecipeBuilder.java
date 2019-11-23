package com.osir.tmc.api.recipe;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.ImmutableMap;
import com.osir.tmc.api.TMCLog;

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
	protected EnumValidationResult validate() {
		if (this.type.getInputLength() < this.inputs.size()) {
			TMCLog.logger.error("Input size [" + this.inputs.size() + "] is larger than the limit["
					+ this.type.getInputLength() + "]");
			this.recipeStatus = EnumValidationResult.INVALID;
		}
		if (this.outputs.size() > 4) {
			TMCLog.logger.error("Output size [" + this.outputs.size() + "] is larger than the limit[4]");
			this.recipeStatus = EnumValidationResult.INVALID;
		}
		if (this.recipeStatus == EnumValidationResult.INVALID) {
			TMCLog.logger.error("Invalid recipe, read the errors above: {}", this);
		}
		return recipeStatus;
	}

	@Override
	public ValidationResult<Recipe> build() {
		return ValidationResult.newResult(this.validate(), new AnvilRecipe(inputs, outputs, chancedOutputs, fluidInputs,
				fluidOutputs, ImmutableMap.of(), duration, EUt, hidden, this.type));
	}

	@Override
	public AnvilRecipeBuilder copy() {
		return new AnvilRecipeBuilder(this);
	}

	public AnvilRecipeBuilder type(AnvilRecipeType type) {
		this.type = type;
		return this;
	}

	public AnvilRecipeType getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("recipeMap", this.recipeMap).append("inputs", this.inputs)
				.append("outputs", this.outputs).append("chancedOutputs", this.chancedOutputs)
				.append("fluidInputs", this.fluidInputs).append("fluidOutputs", this.fluidOutputs)
				.append("duration", this.duration).append("EUt", this.EUt).append("hidden", this.hidden)
				.append("recipeStatus", this.recipeStatus).append("type", this.type).toString();
	}
}