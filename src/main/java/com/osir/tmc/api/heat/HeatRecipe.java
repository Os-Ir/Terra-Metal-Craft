package com.osir.tmc.api.heat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HeatRecipe {
	private int unit;
	private HeatMaterial material;
	private ItemStack input, output;

	public HeatRecipe(int unit, ItemStack input, HeatMaterial material) {
		this(unit, input, ItemStack.EMPTY, material);
	}

	public HeatRecipe(int unit, ItemStack input, ItemStack output, HeatMaterial material) {
		this.unit = unit;
		this.input = input;
		this.output = output;
		this.material = material;
	}

	public boolean match(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		Item item = stack.getItem();
		if (item != input.getItem()) {
			return false;
		}
		if (item.getHasSubtypes() && stack.getItemDamage() != input.getItemDamage()) {
			return false;
		}
		return true;
	}

	public HeatMaterial getMaterial() {
		return this.material;
	}

	public int getUnit() {
		return this.unit;
	}

	public float getSpecificHeat() {
		return this.material.getSpecificHeat();
	}

	public float getMeltTemp() {
		return this.material.getMeltTemp();
	}

	public ItemStack getInput() {
		return this.input;
	}

	public ItemStack getOutput() {
		return this.output;
	}
}