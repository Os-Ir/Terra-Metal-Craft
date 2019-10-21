package com.osir.tmc.api.anvil;

import java.util.Comparator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.osir.tmc.api.util.ComparatorAnvilRecipe;

import net.minecraft.item.ItemStack;

public class AnvilRecipe {
	protected static final Comparator COMPARATOR = new ComparatorAnvilRecipe();
	protected Pair<ItemStack, ItemStack> input, output;

	public AnvilRecipe(ItemStack inputA, ItemStack inputB, ItemStack outputA, ItemStack outputB) {
		this.input = new ImmutablePair(inputA, inputB);
		this.output = new ImmutablePair(outputA, outputB);
	}

	public AnvilRecipe(Pair<ItemStack, ItemStack> input, Pair<ItemStack, ItemStack> output) {
		this.input = input;
		this.output = output;
	}

	public boolean match(Pair<ItemStack, ItemStack> input) {
		if (input == null) {
			return false;
		}
		ItemStack left = input.getLeft();
		ItemStack right = input.getRight();
		if (left == null || left.isEmpty()) {
			left = ItemStack.EMPTY;
		}
		if (right == null || right.isEmpty()) {
			right = ItemStack.EMPTY;
		}
		if (ItemStack.areItemsEqual(left, this.input.getLeft())) {
			if (ItemStack.areItemsEqual(right, this.input.getRight())) {
				return true;
			}
			return false;
		}
		if (ItemStack.areItemsEqual(right, this.input.getLeft())) {
			if (ItemStack.areItemsEqual(left, this.input.getRight())) {
				return true;
			}
			return false;
		}
		return false;
	}

	public Pair<ItemStack, ItemStack> getInput() {
		return this.input;
	}

	public Pair<ItemStack, ItemStack> getOutput() {
		return this.output;
	}
}