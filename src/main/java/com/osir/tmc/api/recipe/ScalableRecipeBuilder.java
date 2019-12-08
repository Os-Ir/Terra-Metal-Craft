package com.osir.tmc.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.osir.tmc.api.TMCLog;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.ValidationResult;
import net.minecraft.util.NonNullList;

public class ScalableRecipeBuilder extends RecipeBuilder<ScalableRecipeBuilder> {
	protected Map<RecipeValueFormat, Object> value;
	protected Predicate<ScalableRecipeBuilder> validation;

	public ScalableRecipeBuilder() {
		this((builder) -> true);
	}

	public ScalableRecipeBuilder(Recipe recipe, RecipeMap<ScalableRecipeBuilder> recipeMap) {
		super(recipe, recipeMap);
		if (recipe instanceof ScalableRecipe) {
			this.value = ((ScalableRecipe) recipe).value;
		}
	}

	public ScalableRecipeBuilder(Recipe recipe, RecipeMap<ScalableRecipeBuilder> recipeMap,
			Predicate<ScalableRecipeBuilder> validation) {
		this(recipe, recipeMap);
		this.validation = validation;
	}

	public ScalableRecipeBuilder(RecipeBuilder<ScalableRecipeBuilder> recipeBuilder) {
		super(recipeBuilder);
		if (recipeBuilder instanceof ScalableRecipeBuilder) {
			this.value = ((ScalableRecipeBuilder) recipeBuilder).value;
			this.validation = ((ScalableRecipeBuilder) recipeBuilder).validation;
		}
	}

	public ScalableRecipeBuilder(Predicate<ScalableRecipeBuilder> validation) {
		this.validation = validation;
		this.value = new HashMap();
	}

	public RecipeValueFormat findFormat(String name) {
		Iterator<Entry<RecipeValueFormat, Object>> ite = this.value.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<RecipeValueFormat, Object> entry = ite.next();
			if (entry.getKey().getName().equals(name)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public void addFormat(RecipeValueFormat format) {
		if (this.value.containsKey(format)) {
			throw new IllegalStateException("Extra format [" + format.getName() + "] has existed");
		} else {
			this.value.put(format, format.getDefault());
		}
	}

	public void deleteFormat(String name) {
		this.deleteFormat(this.findFormat(name));
	}

	public void deleteFormat(RecipeValueFormat format) {
		if (this.value.containsKey(format)) {
			this.value.remove(format);
		} else {
			throw new IllegalStateException("Extra format [" + format.getName() + "] undefined");
		}
	}

	public void setValue(String name, Object obj) {
		if (name.equals("EUt")) {
			if (obj instanceof Integer) {
				this.EUt((int) obj);
			} else {
				throw new IllegalStateException("Extra format [EUt] doesn't match the object");
			}
		} else if (name.equals("duration")) {
			if (obj instanceof Integer) {
				this.duration((int) obj);
			} else {
				throw new IllegalStateException("Extra format [duration] doesn't match the object");
			}
		} else {
			RecipeValueFormat format = this.findFormat(name);
			if (format != null) {
				this.setValue(format, obj);
			} else {
				TMCLog.logger.warn("Extra format [" + name + "] undefined");
			}
		}
	}

	public void setValue(RecipeValueFormat format, Object obj) {
		if (format.validate(obj) == EnumValidationResult.INVALID) {
			throw new IllegalStateException("Extra format [" + format.getName() + "] doesn't match the object");
		} else {
			this.value.put(format, obj);
		}
	}

	public Predicate<ScalableRecipeBuilder> getValidation() {
		return this.validation;
	}

	public Map<RecipeValueFormat, Object> getValue() {
		return this.value;
	}

	public NonNullList<RecipeValueFormat> getFormatList() {
		NonNullList<RecipeValueFormat> format = NonNullList.create();
		Iterator<Entry<RecipeValueFormat, Object>> ite = this.value.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<RecipeValueFormat, Object> entry = ite.next();
			if (entry.getKey().validate(entry.getValue()) == EnumValidationResult.INVALID) {
				TMCLog.logger.warn("Extra format [" + entry.getKey().getName() + "] doesn't match the object");
				continue;
			}
			format.add(entry.getKey());
		}
		return format;
	}

	public List<Object> getValueList() {
		List<Object> obj = new ArrayList();
		Iterator<Entry<RecipeValueFormat, Object>> ite = this.value.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<RecipeValueFormat, Object> entry = ite.next();
			if (entry.getKey().validate(entry.getValue()) == EnumValidationResult.INVALID) {
				TMCLog.logger.warn("Extra format [" + entry.getKey().getName() + "] doesn't match the object");
				continue;
			}
			obj.add(entry.getValue());
		}
		return obj;
	}

	@Override
	protected EnumValidationResult finalizeAndValidate() {
		if (this.validate() == EnumValidationResult.INVALID || !this.validation.test(this)) {
			return EnumValidationResult.INVALID;
		}
		Iterator<Entry<RecipeValueFormat, Object>> ite = this.value.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<RecipeValueFormat, Object> entry = ite.next();
			if (entry.getKey().validate(entry.getValue()) == EnumValidationResult.INVALID) {
				return EnumValidationResult.INVALID;
			}
		}
		return EnumValidationResult.VALID;
	}

	@Override
	public ValidationResult<Recipe> build() {
		return ValidationResult.newResult(this.finalizeAndValidate(),
				new ScalableRecipe(this.inputs, this.outputs, this.chancedOutputs, this.fluidInputs, this.fluidOutputs,
						ImmutableMap.of(), this.duration, this.EUt, this.hidden, this.getFormatList(),
						this.getValueList()));
	}

	@Override
	public ScalableRecipeBuilder copy() {
		return new ScalableRecipeBuilder(this);
	}
}