package com.osir.tmc.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public ScalableRecipeBuilder(Recipe recipe, RecipeMap<ScalableRecipeBuilder> recipeMap) {
		super(recipe, recipeMap);
		if (recipe instanceof ScalableRecipe) {
			this.value = ((ScalableRecipe) recipe).value;
		}
	}

	public ScalableRecipeBuilder(RecipeBuilder<ScalableRecipeBuilder> recipeBuilder) {
		super(recipeBuilder);
		if (recipeBuilder instanceof ScalableRecipeBuilder) {
			this.value = ((ScalableRecipeBuilder) recipeBuilder).value;
		}
	}

	public ScalableRecipeBuilder() {
		this.value = new HashMap<RecipeValueFormat, Object>();
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

	public ScalableRecipeBuilder addFormat(RecipeValueFormat format) {
		if (this.value.containsKey(format)) {
			throw new IllegalStateException("Extra format [" + format.getName() + "] has existed");
		} else {
			this.value.put(format, format.getDefault());
		}
		return this;
	}

	public ScalableRecipeBuilder deleteFormat(String name) {
		return this.deleteFormat(this.findFormat(name));
	}

	public ScalableRecipeBuilder deleteFormat(RecipeValueFormat format) {
		if (this.value.containsKey(format)) {
			this.value.remove(format);
		} else {
			throw new IllegalStateException("Extra format [" + format.getName() + "] undefined");
		}
		return this;
	}

	public ScalableRecipeBuilder setValue(String name, Object obj) {
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
		return this;
	}

	public ScalableRecipeBuilder setValue(RecipeValueFormat format, Object obj) {
		if (format != null) {
			if (this.value.containsKey(format)) {
				if (!format.validate(obj)) {
					throw new IllegalStateException("Extra format [" + format.getName() + "] doesn't match the object");
				} else {
					this.value.put(format, obj);
				}
			} else {
				TMCLog.logger.warn("Extra format [" + format.getName() + "] undefined");
			}
		}
		return this;
	}

	public Object getValue(RecipeValueFormat format) {
		if (this.value.containsKey(format)) {
			return this.value.get(format);
		}
		return null;
	}

	public Object getValue(String name) {
		RecipeValueFormat format = this.findFormat(name);
		if (format == null) {
			return null;
		}
		if (this.value.containsKey(format)) {
			return this.value.get(format);
		}
		return null;
	}

	public NonNullList<RecipeValueFormat> getFormatList() {
		NonNullList<RecipeValueFormat> format = NonNullList.create();
		Iterator<Entry<RecipeValueFormat, Object>> ite = this.value.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<RecipeValueFormat, Object> entry = ite.next();
			if (!entry.getKey().validate(entry.getValue())) {
				TMCLog.logger.warn("Extra format [" + entry.getKey().getName() + "] doesn't match the object");
				continue;
			}
			format.add(entry.getKey());
		}
		return format;
	}

	public List<Object> getValueList() {
		List<Object> obj = new ArrayList<Object>();
		Iterator<Entry<RecipeValueFormat, Object>> ite = this.value.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<RecipeValueFormat, Object> entry = ite.next();
			if (!entry.getKey().validate(entry.getValue())) {
				TMCLog.logger.warn("Extra format [" + entry.getKey().getName() + "] doesn't match the object");
				continue;
			}
			obj.add(entry.getValue());
		}
		return obj;
	}

	@Override
	protected EnumValidationResult finalizeAndValidate() {
		if (this.validate() == EnumValidationResult.INVALID) {
			return EnumValidationResult.INVALID;
		}
		Iterator<Entry<RecipeValueFormat, Object>> ite = this.value.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<RecipeValueFormat, Object> entry = ite.next();
			if (!entry.getKey().validate(entry.getValue())) {
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