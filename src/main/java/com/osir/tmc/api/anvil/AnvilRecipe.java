package com.osir.tmc.api.anvil;

import java.util.Comparator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.api.util.ComparatorAnvilRecipe;
import com.osir.tmc.handler.CapabilityHandler;

import net.minecraft.item.ItemStack;

public class AnvilRecipe {
	protected static final Comparator COMPARATOR = new ComparatorAnvilRecipe();
	protected AnvilRecipeType type;
	protected Pair<ItemStack, ItemStack> input, output;
	protected Pair<Integer, Integer> num;

	public AnvilRecipe(AnvilRecipeType type, ItemStack inputA, ItemStack inputB, int numA, int numB, ItemStack outputA,
			ItemStack outputB) {
		this.type = type;
		this.input = new ImmutablePair(inputA, inputB);
		this.output = new ImmutablePair(outputA, outputB);
		this.num = new ImmutablePair(numA, numB);
	}

	public AnvilRecipe(AnvilRecipeType type, Pair<ItemStack, ItemStack> input, Pair<Integer, Integer> num,
			Pair<ItemStack, ItemStack> output) {
		this.type = type;
		this.input = input;
		this.output = output;
		this.num = num;
	}

	public boolean accept(Pair<ItemStack, ItemStack> input, AnvilRecipeType type, boolean action) {
		if (input == null || type != this.type) {
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
		if (left.hasCapability(CapabilityHandler.HEATABLE, null)) {
			IHeatable cap = left.getCapability(CapabilityHandler.HEATABLE, null);
			if (type == AnvilRecipeType.WELD) {
				if (!cap.isWeldable()) {
					return false;
				}
			} else if (!cap.isWorkable()) {
				return false;
			}
		}
		if (right.hasCapability(CapabilityHandler.HEATABLE, null)) {
			IHeatable cap = right.getCapability(CapabilityHandler.HEATABLE, null);
			if (type == AnvilRecipeType.WELD) {
				if (!cap.isWeldable()) {
					return false;
				}
			} else if (!cap.isWorkable()) {
				return false;
			}
		}
		if (ItemStack.areItemsEqual(left, this.input.getLeft()) && left.getCount() >= this.num.getLeft()) {
			if (ItemStack.areItemsEqual(right, this.input.getRight()) && right.getCount() >= this.num.getRight()) {
				if (action) {
					left.shrink(this.num.getLeft());
					right.shrink(this.num.getRight());
				}
				return true;
			}
			return false;
		}
		if (ItemStack.areItemsEqual(right, this.input.getLeft()) && right.getCount() >= this.num.getLeft()) {
			if (ItemStack.areItemsEqual(left, this.input.getRight()) && left.getCount() >= this.num.getRight()) {
				if (action) {
					left.shrink(this.num.getRight());
					right.shrink(this.num.getLeft());
				}
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean match(Pair<ItemStack, ItemStack> input, AnvilRecipeType type) {
		if (input == null || type != this.type) {
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

	public AnvilRecipeType getType() {
		return this.type;
	}

	public Pair<ItemStack, ItemStack> getInput() {
		return this.input;
	}

	public Pair<ItemStack, ItemStack> getOutput() {
		return this.output;
	}
}