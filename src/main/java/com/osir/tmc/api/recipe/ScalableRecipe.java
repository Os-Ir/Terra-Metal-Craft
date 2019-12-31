package com.osir.tmc.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.osir.tmc.api.TMCLog;

import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.util.EnumValidationResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class ScalableRecipe extends Recipe {
	protected int length;
	protected Map<RecipeValueFormat, Object> value;

	public ScalableRecipe(List<CountableIngredient> inputs, List<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
			List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs, Map<String, Object> recipeProperties,
			int duration, int EUt, boolean hidden, NonNullList<RecipeValueFormat> format, List<Object> value) {
		super(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs, recipeProperties, duration, EUt, hidden);
		if (format.size() != value.size()) {
			throw new IllegalStateException("Extra format and value length mismatch");
		}
		this.value = new HashMap();
		Set<String> cache = new HashSet<String>();
		int cnt = 0;
		for (int i = 0; i < format.size(); i++) {
			RecipeValueFormat fmt = format.get(i);
			if (cache.contains(fmt.getName())) {
				TMCLog.logger.warn("Extra format [" + fmt.getName() + "] has existed");
				continue;
			}
			if (!fmt.validate(value.get(i))) {
				TMCLog.logger.warn("Extra format [" + fmt.getName() + "] doesn't match the object");
				continue;
			}
			cache.add(fmt.getName());
			this.value.put(fmt, value.get(i));
			cnt++;
		}
		this.length = cnt;
	}

	public int length() {
		return this.length;
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
		List<Object> obj = new ArrayList();
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
}