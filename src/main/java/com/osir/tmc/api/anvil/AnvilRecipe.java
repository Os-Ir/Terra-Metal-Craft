package com.osir.tmc.api.anvil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.osir.tmc.api.util.ComparatorAnvilRecipe;

import net.minecraft.item.ItemStack;

public class AnvilRecipe {
	protected static final Comparator COMPARATOR = new ComparatorAnvilRecipe();
	protected List<ItemStack> input, output;

	public AnvilRecipe(ItemStack[] input, ItemStack[] output) {
		this(Arrays.asList(input), Arrays.asList(output));
	}

	public AnvilRecipe(List<ItemStack> input, List<ItemStack> output) {
		this.input = input;
		this.output = output;
		this.input.sort(COMPARATOR);
		this.output.sort(COMPARATOR);
	}

	public boolean match(List<ItemStack> input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		input.equals(this.input);
		return false;
	}

	public List<ItemStack> getInput() {
		return this.input;
	}

	public List<ItemStack> getOutput() {
		return this.output;
	}
}